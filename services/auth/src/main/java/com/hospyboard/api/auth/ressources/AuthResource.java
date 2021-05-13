package com.hospyboard.api.auth.ressources;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class AuthResource {

    @RequestMapping("/")
    public String home() {
        return "test ok.";
    }

}
