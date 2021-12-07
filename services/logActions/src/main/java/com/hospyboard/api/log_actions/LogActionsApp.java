package com.hospyboard.api.log_actions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hospyboard.api")
public class LogActionsApp {
    public static void main(String[] args) {
        SpringApplication.run(LogActionsApp.class, args);
    }
}
