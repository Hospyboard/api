package com.hospyboard.api.core.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "crypt")
@Getter
@Setter
public class CryptSecretConfig {
    private String secret;
}
