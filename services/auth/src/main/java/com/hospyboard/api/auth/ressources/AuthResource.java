package com.hospyboard.api.auth.ressources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthResource {

    @RequestMapping("/")
    public String home() {
        return "test ok.";
    }

}
