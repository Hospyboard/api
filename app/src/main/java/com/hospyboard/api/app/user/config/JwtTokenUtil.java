package com.hospyboard.api.app.user.config;

import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.entity.UserToken;
import com.hospyboard.api.app.user.mappers.UserTokenMapper;
import com.hospyboard.api.app.user.repository.UserRepository;
import com.hospyboard.api.app.user.repository.UserTokenRepository;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.Key;
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
    private final Key jwtSecretKey;

    public JwtTokenUtil(UserTokenRepository tokenRepository,
                        UserRepository userRepository,
                        UserTokenMapper tokenMapper) {
        this.tokenMapper = tokenMapper;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtSecretKey = getJwtCryptKey();
    }

    private static Key getJwtCryptKey() {
        final File keyFile = new File("secretJwt.key");
        Key key = getKeyFromFile(keyFile);

        if (key == null) {
            key = generateNewKey();
            saveKey(key, keyFile);
            return key;
        } else {
            return key;
        }
    }

    @Nullable
    private static Key getKeyFromFile(final File keyFile) throws ApiException {
        try {
            if (keyFile.exists()) {
                final String content = Files.readString(keyFile.toPath(), StandardCharsets.UTF_8);

                if (!Strings.isEmpty(content)) {
                    final byte[] decodedKey = Decoders.BASE64.decode(content);
                    return Keys.hmacShaKeyFor(decodedKey);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new ApiException("Une erreur est survenue lors du chargement de la clé secrete pour les tokens JWT.", e);
        }
    }

    private static Key generateNewKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
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

    private static void saveKey(final Key key, final File keyFile) {
        try {
            if (!keyFile.exists() && !keyFile.createNewFile()) {
                throw new IOException("Creation file failed.");
            }

            final String encodedKey = Encoders.BASE64.encode(key.getEncoded());
            Files.writeString(keyFile.toPath(), encodedKey, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new ApiException("Impossible de sauvegarder la clé de cryptage pour les tokens JWT.");
        }
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
                .signWith(jwtSecretKey)
                .compact());

        final UserTokenDTO tokenDTO = tokenMapper.toDto(tokenRepository.save(userToken));
        return tokenDTO;
    }

    public boolean isTokenValid(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(token);
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

    @Transactional
    public void invalidateTokens(final UUID userUuid) {
        final Optional<User> search = userRepository.findByUuid(userUuid.toString());

        if (search.isPresent()) {
            final User user = search.get();
            final Set<UserToken> userTokens = this.tokenRepository.findAllByUser(user);

            this.tokenRepository.deleteAll(userTokens);
        }
    }

    @Nullable
    private UserToken getToken(final String token) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
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
