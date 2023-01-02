package com.hospyboard.api.app.core.configs;

import fr.funixgaming.api.core.utils.network.IPUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "hospyboard")
@Getter
@Setter
public class HospyboardConfig {
    private String urlDashboard;

    @Bean
    public IPUtils ipUtils() {
        return new IPUtils(true);
    }

}
