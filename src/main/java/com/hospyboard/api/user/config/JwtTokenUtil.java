package com.hospyboard.api.user.config;

import com.hospyboard.api.user.dto.UserTokenDTO;
import com.hospyboard.api.user.entity.User;
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
        final Instant expiresAt = now.plusSeconds(EXPIRATION_SECONDS_TOKEN);

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
            return true;//TODO add checking of tokens unvalidated (like password reseted or username changed)
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature - {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty - {}", ex.getMessage());
        }
        return false;
    }
}