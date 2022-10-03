package com.hospyboard.api.app.user;

import com.hospyboard.api.app.configs.MailTestConfig;
import com.hospyboard.api.app.core.JsonHelper;
import com.hospyboard.api.app.core.UserHelper;
import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.dto.UserForgotPasswordDTO;
import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.entity.UserPasswordReset;
import com.hospyboard.api.app.user.repository.UserForgotPasswordResetRepository;
import com.hospyboard.api.app.user.repository.UserRepository;
import com.icegreen.greenmail.util.GreenMail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(MailTestConfig.class)
public class UserResetPasswordTests {

    private final MockMvc mockMvc;
    private final UserHelper userHelper;
    private final UserForgotPasswordResetRepository forgotPasswordResetRepository;
    private final UserRepository userRepository;
    private final JsonHelper jsonHelper;
    private final GreenMail mailHelper;
    private final String route = "/user/forgotPassword";

    @Autowired
    public UserResetPasswordTests(MockMvc mockMvc,
                                  UserHelper userHelper,
                                  JsonHelper jsonHelper,
                                  UserRepository userRepository,
                                  GreenMail mailHelper,
                                  UserForgotPasswordResetRepository userForgotPasswordResetRepository) {
        this.mockMvc = mockMvc;
        this.userHelper = userHelper;
        this.jsonHelper = jsonHelper;
        this.userRepository = userRepository;
        this.forgotPasswordResetRepository = userForgotPasswordResetRepository;
        this.mailHelper = mailHelper;
    }

    @Test
    public void testResetPassword() throws Exception {
        final UserTokenDTO userTokenDTO = this.userHelper.generatePatientToken();
        final UserForgotPasswordDTO forgotPasswordDTO = new UserForgotPasswordDTO();

        forgotPasswordDTO.setEmail(userTokenDTO.getUser().getEmail());
        this.mockMvc.perform(post(route)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonHelper.toJson(forgotPasswordDTO))
        ).andExpect(status().isOk());
        assertTrue(mailHelper.waitForIncomingEmail(15000, 1));

        final UserForgotPasswordDTO forgotPasswordDTO1 = new UserForgotPasswordDTO();
        forgotPasswordDTO1.setCode(getCode(userTokenDTO.getUser()).getCode());
        forgotPasswordDTO1.setPassword("OuiSCXE234sdf");
        forgotPasswordDTO1.setPasswordConfirmation("OuiSCXE234sdf");

        this.mockMvc.perform(post(route + "/change")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonHelper.toJson(forgotPasswordDTO1))
        ).andExpect(status().isOk());

        this.mockMvc.perform(post(route + "/change")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonHelper.toJson(forgotPasswordDTO1))
        ).andExpect(status().isBadRequest());
    }

    private UserPasswordReset getCode(final UserDTO userDTO) throws Exception {
        final Optional<User> search = this.userRepository.findByUuid(userDTO.getId().toString());

        if (search.isPresent()) {
            final Optional<UserPasswordReset> search2 = this.forgotPasswordResetRepository.findUserPasswordResetByUser(search.get());

            if (search2.isPresent()) {
                return search2.get();
            } else {
                throw new Exception("Erreur pas de reset code.");
            }
        } else {
            throw new Exception("Error user not found.");
        }
    }

}
