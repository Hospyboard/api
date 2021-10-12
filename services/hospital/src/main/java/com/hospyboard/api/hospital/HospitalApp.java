package com.hospyboard.api.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hospyboard.api")
public class HospitalApp {
    public static void main(String[] args) {
        SpringApplication.run(HospitalApp.class, args);
    }
}
