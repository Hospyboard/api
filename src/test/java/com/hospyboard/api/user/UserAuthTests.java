package com.hospyboard.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospyboard.api.core.JsonHelper;
import com.hospyboard.api.user.config.JWTConfig;
import com.hospyboard.api.user.dto.UserCreationDTO;
import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserAuthTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserAuthTests(MockMvc mockMvc,
                         ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        final UserCreationDTO userCreationDTO = new UserCreationDTO();

        userCreationDTO.setUsername("testUser");
        userCreationDTO.setFirstName("testFirstName");
        userCreationDTO.setLastName("testLastName");
        userCreationDTO.setEmail("test@gmail.com");
        userCreationDTO.setPassword("12345");
        userCreationDTO.setPasswordConfirmation("12345");

        MvcResult mvcResult = this.mockMvc.perform(post(JWTConfig.REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.toJson(objectMapper, userCreationDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final UserDTO result = JsonHelper.fromJson(objectMapper, mvcResult.getResponse().getContentAsString(), UserDTO.class);
        
        assertNotNull(result.getId());
        assertEquals(userCreationDTO.getUsername(), result.getUsername());
        assertEquals(userCreationDTO.getFirstName(), result.getFirstName());
        assertEquals(userCreationDTO.getLastName(), result.getLastName());
        assertEquals(userCreationDTO.getEmail(), result.getEmail());
        assertEquals(UserRole.PATIENT, result.getRole());
        assertNull(result.getPassword());
        assertNull(result.getPasswordConfirmation());
    }

}
