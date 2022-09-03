package com.hospyboard.api.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.hospyboard.api", "fr.funixgaming.api.core.utils.encryption"})
public class HospyboardApiApp {
    public static void main(String[] args) {
        SpringApplication.run(HospyboardApiApp.class, args);
    }
}
