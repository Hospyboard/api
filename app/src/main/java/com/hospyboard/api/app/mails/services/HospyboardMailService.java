package com.hospyboard.api.app.mails.services;

import com.hospyboard.api.app.mails.dtos.HospyboardMailDTO;
import fr.funixgaming.api.core.mail.dtos.ApiMailDTO;
import fr.funixgaming.api.core.mail.services.ApiMailService;
import lombok.Getter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
@Getter
public class HospyboardMailService extends ApiMailService {

    private final Queue<HospyboardMailDTO> mailQueue = new LinkedList<>();

    public HospyboardMailService(JavaMailSender mailSender) {
        super(mailSender);
    }

    @Scheduled(fixedRate = 10000)
    public void processMails() {
        ApiMailDTO mailDTO = mailQueue.poll();

        while (mailDTO != null) {
            super.sendMail(mailDTO);
            mailDTO = mailQueue.poll();
        }
    }

}
