package com.hospyboard.api.app.mails.resources;

import com.hospyboard.api.app.mails.dtos.HospyboardMailDTO;
import com.hospyboard.api.app.mails.services.HospyboardMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mail")
@RequiredArgsConstructor
public class HospyboardMailResource {

    private final HospyboardMailService mailService;

    @PostMapping
    public void sendMail(@RequestBody HospyboardMailDTO request) {
        mailService.getMailQueue().add(request);
    }

}
