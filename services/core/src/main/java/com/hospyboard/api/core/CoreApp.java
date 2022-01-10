package com.hospyboard.api.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hospyboard.api")
public class CoreApp {
    public static void main(String[] args) {
        SpringApplication.run(CoreApp.class, args);
    }
}
