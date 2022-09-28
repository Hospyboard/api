package com.hospyboard.api.app.core.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "hospyboard")
@Getter
@Setter
public class HospyboardConfig {
    private String urlDashboard;
}
