package com.hospyboard.api.app.core;

import com.hospyboard.api.app.user.UserAuthTests;
import com.hospyboard.api.app.user.dto.UserAuthDTO;
import com.hospyboard.api.app.user.dto.UserCreationDTO;
import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.hospyboard.api.app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@RequiredArgsConstructor
public class UserHelper {

    private final MockMvc mockMvc;
    private final JsonHelper objectMapper;
    private final UserRepository userRepository;

    public UserTokenDTO generatePatientToken() throws Exception {
        return generateToken("patient");
    }

    public UserTokenDTO generateAdminToken() throws Exception {
        return generateToken("admin");
    }

    private UserTokenDTO generateToken(final String username) throws Exception {
        if (userRepository.findByUsername(username).isEmpty()) {
            final UserCreationDTO userCreationDTO = new UserCreationDTO();
            userCreationDTO.setUsername(username);
            userCreationDTO.setFirstName("testFirstName");
            userCreationDTO.setLastName("testLastName");
            userCreationDTO.setEmail("test@gmail.com");
            userCreationDTO.setPassword("12345");
            userCreationDTO.setPasswordConfirmation("12345");

            this.mockMvc.perform(post(UserAuthTests.ROUTE + "register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.toJson(userCreationDTO)))
                    .andExpect(status().isOk());
        }

        final UserAuthDTO authDTO = new UserAuthDTO();
        authDTO.setUsername(username);
        authDTO.setPassword("12345");

        MvcResult mvcResult = this.mockMvc.perform(post(UserAuthTests.ROUTE + "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.toJson(authDTO)))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.fromJson(mvcResult.getResponse().getContentAsString(), UserTokenDTO.class);
    }

}
