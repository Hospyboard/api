package com.hospyboard.api.app.user.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JWTConfig {
    public static final String REGISTER_URL = "/user/register";
    public static final String LOGIN_URL = "/user/login";

    private String secret;
}
