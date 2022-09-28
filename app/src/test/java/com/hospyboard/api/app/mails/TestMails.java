package com.hospyboard.api.app.mails;

import com.hospyboard.api.app.configs.MailTestConfig;
import com.hospyboard.api.app.core.JsonHelper;
import com.hospyboard.api.app.core.UserHelper;
import com.hospyboard.api.app.mails.dtos.HospyboardMailDTO;
import com.hospyboard.api.app.mails.services.HospyboardMailService;
import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.icegreen.greenmail.util.GreenMail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(MailTestConfig.class)
public class TestMails {

    private static final String ROUTE = "/mail";

    private final MockMvc mockMvc;
    private final JsonHelper jsonHelper;
    private final UserTokenDTO adminToken;
    private final HospyboardMailService mailService;
    private final GreenMail mailHelper;

    @Autowired
    public TestMails(MockMvc mockMvc,
                     UserHelper userHelper,
                     JsonHelper jsonHelper,
                     GreenMail mailHelper,
                     HospyboardMailService mailService) throws Exception {
        this.mockMvc = mockMvc;
        this.mailService = mailService;
        this.jsonHelper = jsonHelper;
        this.adminToken = userHelper.generateAdminToken();
        this.mailHelper = mailHelper;
    }

    @Test
    public void sendSimpleMail() throws Exception {
        final HospyboardMailDTO mailDTO = new HospyboardMailDTO();
        mailDTO.setFrom("test@test.com");
        mailDTO.setTo("antoine@test.com");
        mailDTO.setSubject("test mail");
        mailDTO.setText("oui");

        mockMvc.perform(post(ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken.getToken())
                .content(jsonHelper.toJson(mailDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        assertTrue(mailHelper.waitForIncomingEmail(15000, 1));

        final Instant limit = Instant.now().plusSeconds(20);
        while (mailService.getMailQueue().size() > 0 && Instant.now().isBefore(limit)) ;
        assertEquals(0, mailService.getMailQueue().size());
    }

}
