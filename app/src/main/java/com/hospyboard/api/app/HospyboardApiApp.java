package com.hospyboard.api.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.hospyboard.api")
@EnableSwagger2
public class HospyboardApiApp {
    public static void main(String[] args) {
        SpringApplication.run(HospyboardApiApp.class, args);
    }
}
