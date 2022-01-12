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
    private final String jwtSecret;

    public JwtTokenUtil(JWTConfig jwtConfig) {
        this.jwtSecret = jwtConfig.getSecret();
    }

    public UserTokenDTO generateAccessToken(final User user) {
        final Instant expiresAt = Instant.ofEpochMilli(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000); // 1 week
        return new UserTokenDTO(Jwts.builder()
                .setSubject(String.format("%s,%s", user.getUuid(), user.getUsername()))
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(expiresAt))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact(),
                expiresAt);
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
            return true;
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