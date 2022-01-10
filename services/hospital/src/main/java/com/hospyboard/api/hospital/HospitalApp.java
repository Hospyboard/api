package com.hospyboard.api.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.hospyboard.api")
@EnableSwagger2
public class HospitalApp {
    public static void main(String[] args) {
        SpringApplication.run(HospitalApp.class, args);
    }
}
