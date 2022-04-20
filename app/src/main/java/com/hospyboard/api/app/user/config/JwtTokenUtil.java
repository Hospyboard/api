package com.hospyboard.api.app.user.config;

import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.hospyboard.api.app.user.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenUtil {
    private final static String ISSUER = "Hospyboard";
    private final static Integer EXPIRATION_SECONDS_TOKEN = 604800; //1 Week
    private final String jwtSecret;

    public JwtTokenUtil(JWTConfig jwtConfig) {
        this.jwtSecret = jwtConfig.getSecret();
    }

    public UserTokenDTO generateAccessToken(final User user) {
        final Instant now = Instant.now();
        final Instant expiresAt = now.plusSeconds(EXPIRATION_SECONDS_TOKEN - 20);

        return new UserTokenDTO(Jwts.builder()
                .setSubject(String.format("%s,%s", user.getUuid(), user.getUsername()))
                .setIssuer(ISSUER)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiresAt))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact(),
                expiresAt.getEpochSecond() - now.getEpochSecond());
    }

    public String getUserUuid(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject().split(",")[0];
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject().split(",")[1];
    }

    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;//TODO add checking of tokens unvalidated (like password reset or username changed)
        } catch (JwtException e) {
            if (e instanceof ExpiredJwtException) {
                log.error("Votre token de session a expiré. {}", e.getMessage());
            } else {
                log.error("Votre token d'accès est invalide. {}", e.getMessage());
            }
            return false;
        } catch (IllegalArgumentException e) {
            log.error("Votre token d'accès est invalide. {}", e.getMessage());
            return false;
        }
    }
}
