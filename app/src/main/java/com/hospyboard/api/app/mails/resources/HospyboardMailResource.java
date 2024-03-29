package com.hospyboard.api.app.mails.resources;

import com.hospyboard.api.app.mails.dtos.HospyboardMailDTO;
import com.hospyboard.api.app.mails.services.HospyboardMailService;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
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

    @PostMapping("contact")
    public void sendContactMail(@RequestBody HospyboardMailDTO request) {
        final HospyboardMailDTO mail = new HospyboardMailDTO();
        mail.setTo("hospyboard_2023@labeip.epitech.eu");
        mail.setFrom("hospyboard@funixgaming.fr");

        if (Strings.isEmpty(request.getSubject())) {
            throw new ApiBadRequestException("No subject");
        } else if (Strings.isEmpty(request.getText())) {
            throw new ApiBadRequestException("No text");
        }

        mail.setText(request.getText());
        mail.setSubject(request.getSubject());

        mailService.getMailQueue().add(mail);
    }

}
