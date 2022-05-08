package com.hospyboard.api.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.hospyboard.api")
public class HospyboardApiApp {
    public static void main(String[] args) {
        SpringApplication.run(HospyboardApiApp.class, args);
    }
}
