package com.hospyboard.api.app.user.config;

import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.entity.UserToken;
import com.hospyboard.api.app.user.mappers.UserTokenMapper;
import com.hospyboard.api.app.user.repository.UserTokenRepository;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class JwtTokenUtil {
    private final static String ISSUER = "Hospyboard";
    private final static Integer EXPIRATION_SECONDS_TOKEN = 604800; //1 Week

    private final UserTokenMapper tokenMapper;
    private final UserTokenRepository tokenRepository;
    private final String jwtSecret;

    public JwtTokenUtil(JWTConfig jwtConfig,
                        UserTokenRepository tokenRepository,
                        UserTokenMapper tokenMapper) {
        this.jwtSecret = jwtConfig.getSecret();
        this.tokenMapper = tokenMapper;
        this.tokenRepository = tokenRepository;
    }

    public UserTokenDTO generateAccessToken(final User user) {
        final Instant now = Instant.now();
        final Instant expiresAt = now.plusSeconds(EXPIRATION_SECONDS_TOKEN - 20);
        final UserToken userToken = new UserToken();

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
                throw new ApiBadRequestException("Votre token d'accès est invalide.");
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
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
