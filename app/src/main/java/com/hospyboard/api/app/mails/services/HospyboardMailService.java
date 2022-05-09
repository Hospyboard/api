package com.hospyboard.api.app.mails.services;

import com.hospyboard.api.app.mails.dtos.HospyboardMailDTO;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.core.mail.services.ApiMailService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
@Getter
@Service
public class HospyboardMailService extends ApiMailService {

    private final Queue<HospyboardMailDTO> mailQueue = new LinkedList<>();

    public HospyboardMailService(JavaMailSender mailSender) {
        super(mailSender);
    }

    @Scheduled(fixedRate = 10000)
    public void processMails() {
        HospyboardMailDTO mailDTO = mailQueue.poll();

        while (mailDTO != null) {
            try {
                super.sendMail(mailDTO);
            } catch (ApiException e) {
                mailQueue.add(mailDTO);
                log.error("Erreur lors de l'envoi d'un mail : {}", e.getMessage());
            }

            mailDTO = mailQueue.poll();
        }
    }

}
