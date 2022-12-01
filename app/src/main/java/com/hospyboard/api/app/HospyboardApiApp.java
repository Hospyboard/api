package com.hospyboard.api.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
@EnableAsync
@EnableScheduling
@SpringBootApplication(scanBasePackages = {
        "com.hospyboard.api",
        "fr.funixgaming.api.core.utils.encryption",
        "fr.funixgaming.api.core.utils.network",
        "fr.funixgaming.api.core.config"
})
public class HospyboardApiApp {
    public static void main(String[] args) {
        SpringApplication.run(HospyboardApiApp.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
        log.info("Spring app running timezone CET: {}", new Date());
    }
}
