package com.hospyboard.api.app.user.config;

import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.entity.UserToken;
import com.hospyboard.api.app.user.mappers.UserTokenMapper;
import com.hospyboard.api.app.user.repository.UserRepository;
import com.hospyboard.api.app.user.repository.UserTokenRepository;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Slf4j
@Component
public class JwtTokenUtil {
    private final static String ISSUER = "Hospyboard";
    private final static Integer EXPIRATION_SECONDS_TOKEN = 604800; //1 Week

    private final UserTokenMapper tokenMapper;
    private final UserTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final String jwtSecret;

    public JwtTokenUtil(JWTConfig jwtConfig,
                        UserTokenRepository tokenRepository,
                        UserRepository userRepository,
                        UserTokenMapper tokenMapper) {
        this.jwtSecret = jwtConfig.getSecret();
        this.tokenMapper = tokenMapper;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    public UserTokenDTO generateAccessToken(final User user) {
        final Instant now = Instant.now();
        final Instant expiresAt = now.plusSeconds(EXPIRATION_SECONDS_TOKEN - 20);
        final UserToken userToken = new UserToken();

        user.setLastLoginAt(Date.from(Instant.now()));
        userRepository.save(user);

        userToken.setUser(user);
        userToken.setUuid(UUID.randomUUID());
        userToken.setExpirationDate(Date.from(expiresAt));
        userToken.setToken(Jwts.builder()
                .setSubject(userToken.getUuid().toString())
                .setIssuer(ISSUER)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiresAt))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact());

        return tokenMapper.toDto(tokenRepository.save(userToken));
    }

    public boolean isTokenValid(final String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            final UserToken userToken = getToken(token);

            if (userToken == null) {
                throw new ApiForbiddenException("Votre token d'accès est invalide.");
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void invalidateTokens(final UUID userUuid) {
        final Optional<User> search = this.userRepository.findByUuid(userUuid.toString());

        if (search.isPresent()) {
            final User user = search.get();
            final Set<UserToken> userTokens = this.tokenRepository.findAllByUser(user);

            this.tokenRepository.deleteAll(userTokens);
        }
    }

    public UsernamePasswordAuthenticationToken authenticateToken(final String token) {
        final UserToken userToken = this.getToken(token);
        final User user;

        if (userToken == null) {
            user = null;
        } else {
            user = userToken.getUser();
        }

        return new UsernamePasswordAuthenticationToken(
                user,
                null,
                user == null ? List.of() : user.getAuthorities()
        );
    }

    @Nullable
    private UserToken getToken(final String token) {
        final Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        final String tokenUuid = claims.getSubject();
        final Optional<UserToken> search = tokenRepository.findByUuid(tokenUuid);

        if (search.isPresent()) {
            return search.get();
        } else {
            return null;
        }
    }
}
