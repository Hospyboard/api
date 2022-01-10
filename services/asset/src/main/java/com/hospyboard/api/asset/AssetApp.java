package com.hospyboard.api.asset;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hospyboard.api")
public class AssetApp {
    public static void main(String[] args) {
        SpringApplication.run(AssetApp.class, args);
    }
}
