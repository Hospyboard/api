package com.hospyboard.api.alert;

import io.swagger.annotations.SwaggerDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.hospyboard.api")
@EnableSwagger2
public class AlertApp {
    public static void main(String[] args) {
        SpringApplication.run(AlertApp.class, args);
    }
}
