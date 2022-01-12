package com.hospyboard.api.user.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JWTConfig {
    public static final String REGISTER_URL = "/user/register";
    public static final String LOGIN_URL = "/user/login";

    private String secret;
}
