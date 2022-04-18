package com.hospyboard.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospyboard.api.core.JsonHelper;
import com.hospyboard.api.user.config.JWTConfig;
import com.hospyboard.api.user.dto.UserAuthDTO;
import com.hospyboard.api.user.dto.UserCreationDTO;
import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.dto.UserTokenDTO;
import com.hospyboard.api.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    public void testFailRegisterPasswordsNotMatch() throws Exception {
        final UserCreationDTO userCreationDTO = new UserCreationDTO();

        userCreationDTO.setUsername("testUser2");
        userCreationDTO.setFirstName("testFirstName");
        userCreationDTO.setLastName("testLastName");
        userCreationDTO.setEmail("test@gmail.com");
        userCreationDTO.setPassword("12345");
        userCreationDTO.setPasswordConfirmation("123456");

        this.mockMvc.perform(post(JWTConfig.REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, userCreationDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateDoubleAccountWithSameUsername() throws Exception {
        final UserCreationDTO userCreationDTO = new UserCreationDTO();

        userCreationDTO.setUsername("testUser1");
        userCreationDTO.setFirstName("testFirstName");
        userCreationDTO.setLastName("testLastName");
        userCreationDTO.setEmail("test@gmail.com");
        userCreationDTO.setPassword("12345");
        userCreationDTO.setPasswordConfirmation("12345");

        this.mockMvc.perform(post(JWTConfig.REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, userCreationDTO)))
                .andExpect(status().isOk());
        this.mockMvc.perform(post(JWTConfig.REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, userCreationDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginSuccess() throws Exception {
        final UserCreationDTO userCreationDTO = new UserCreationDTO();
        userCreationDTO.setUsername("loginTest1");
        userCreationDTO.setFirstName("testFirstName");
        userCreationDTO.setLastName("testLastName");
        userCreationDTO.setEmail("test@gmail.com");
        userCreationDTO.setPassword("12345");
        userCreationDTO.setPasswordConfirmation("12345");

        this.mockMvc.perform(post(JWTConfig.REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, userCreationDTO)))
                .andExpect(status().isOk());

        final UserAuthDTO authDTO = new UserAuthDTO();
        authDTO.setUsername(userCreationDTO.getUsername());
        authDTO.setPassword(userCreationDTO.getPassword());

        MvcResult mvcResult = this.mockMvc.perform(post(JWTConfig.LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, authDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final UserTokenDTO result = JsonHelper.fromJson(objectMapper, mvcResult.getResponse().getContentAsString(), UserTokenDTO.class);
        assertNotNull(result.getExpiresInSeconds());
        assertNotNull(result.getToken());
    }

    @Test
    public void testFailLoginPasswordNotMatch() throws Exception {
        final UserCreationDTO userCreationDTO = new UserCreationDTO();
        userCreationDTO.setUsername("loginTest2");
        userCreationDTO.setFirstName("testFirstName");
        userCreationDTO.setLastName("testLastName");
        userCreationDTO.setEmail("test@gmail.com");
        userCreationDTO.setPassword("12345");
        userCreationDTO.setPasswordConfirmation("12345");

        this.mockMvc.perform(post(JWTConfig.REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, userCreationDTO)))
                .andExpect(status().isOk());

        final UserAuthDTO authDTO = new UserAuthDTO();
        authDTO.setUsername(userCreationDTO.getUsername());
        authDTO.setPassword("WrongPassword");

        this.mockMvc.perform(post(JWTConfig.LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, authDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFailLoginUserUnknown() throws Exception {
        final UserAuthDTO authDTO = new UserAuthDTO();
        authDTO.setUsername("NoAccountHere");
        authDTO.setPassword("password");

        this.mockMvc.perform(post(JWTConfig.LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, authDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGettingPatientConnected() throws Exception {
        final UserCreationDTO userCreationDTO = new UserCreationDTO();
        userCreationDTO.setUsername(UUID.randomUUID().toString());
        userCreationDTO.setFirstName("testFirstName");
        userCreationDTO.setLastName("testLastName");
        userCreationDTO.setEmail("test@gmail.com");
        userCreationDTO.setPassword("12345");
        userCreationDTO.setPasswordConfirmation("12345");

        this.mockMvc.perform(post(JWTConfig.REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, userCreationDTO)))
                .andExpect(status().isOk());

        final UserAuthDTO authDTO = new UserAuthDTO();
        authDTO.setUsername(userCreationDTO.getUsername());
        authDTO.setPassword(userCreationDTO.getPassword());

        MvcResult mvcResult = this.mockMvc.perform(post(JWTConfig.LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, authDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final UserTokenDTO result = JsonHelper.fromJson(objectMapper, mvcResult.getResponse().getContentAsString(), UserTokenDTO.class);

        final MvcResult mvcResultGet = this.mockMvc.perform(get("/user")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + result.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        final UserDTO account = JsonHelper.fromJson(objectMapper, mvcResultGet.getResponse().getContentAsString(), UserDTO.class);
        assertNotNull(account.getId());
        assertEquals(account.getUsername(), userCreationDTO.getUsername());
        assertEquals(account.getFirstName(), userCreationDTO.getFirstName());
        assertEquals(account.getLastName(), userCreationDTO.getLastName());
        assertEquals(account.getEmail(), userCreationDTO.getEmail());
        assertEquals(account.getRole(), UserRole.PATIENT);
        assertNull(account.getPassword());
        assertNull(account.getPasswordConfirmation());
    }

    @Test
    public void testGettingActualUserNoAuthError() throws Exception {
        this.mockMvc.perform(get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testInvalidTokenAccess() throws Exception {
        this.mockMvc.perform(get("/user")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer qlkjd75")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
