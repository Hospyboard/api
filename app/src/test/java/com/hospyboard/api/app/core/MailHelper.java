package com.hospyboard.api.app.core;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MailHelper {

    private final GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withDisabledAuthentication());

    private MailHelper() {
        greenMail.start();
    }

}
