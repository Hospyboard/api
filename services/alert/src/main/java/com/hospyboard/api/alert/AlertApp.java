package com.hospyboard.api.alert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hospyboard.api")
public class AlertApp {
    public static void main(String[] args) {
        SpringApplication.run(AlertApp.class, args);
    }
}
