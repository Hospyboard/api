package com.hospyboard.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospyboard.api.core.JsonHelper;
import com.hospyboard.api.user.config.JWTConfig;
import com.hospyboard.api.user.dto.UserAuthDTO;
import com.hospyboard.api.user.dto.UserCreationDTO;
import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.dto.UserTokenDTO;
import com.hospyboard.api.user.entity.User;
import com.hospyboard.api.user.enums.UserRole;
import com.hospyboard.api.user.repository.UserRepository;
import com.hospyboard.api.user.ressources.UserResource;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserAuthTests {

    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserAuthTests(MockMvc mockMvc,
                         ObjectMapper objectMapper,
                         UserRepository userRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
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

    @Test
    public void testPatchUser() throws Exception {
        final UserTokenDTO credentials = generateToken();
        final UserDTO userDTO = getUserFromToken(credentials);
        final UserDTO patchUser = new UserDTO();

        final Optional<User> user = this.userRepository.findByUuid(userDTO.getId());
        if (user.isPresent()) {
            final User userFind = user.get();
            userFind.setRole(UserRole.ADMIN);
            this.userRepository.saveAndFlush(userFind);
        } else {
            fail("user not found");
        }

        patchUser.setId(userDTO.getId());
        patchUser.setFirstName("heyChanged");

        final MvcResult result = this.mockMvc.perform(patch("/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(JsonHelper.toJson(objectMapper, patchUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        final UserDTO res = JsonHelper.fromJson(objectMapper, result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(res.getFirstName(), patchUser.getFirstName());
    }

    @Test
    public void testPutUser() throws Exception {
        final UserTokenDTO credentials = generateToken();
        final UserDTO changedUser = getUserFromToken(credentials);

        final Optional<User> user = this.userRepository.findByUuid(changedUser.getId());
        if (user.isPresent()) {
            final User userFind = user.get();
            userFind.setRole(UserRole.ADMIN);
            this.userRepository.saveAndFlush(userFind);
        } else {
            fail("user not found");
        }

        changedUser.setFirstName("ouioui");
        changedUser.setLastName("nonnon");
        changedUser.setPassword("oui");
        changedUser.setPasswordConfirmation("oui");

        final MvcResult result = this.mockMvc.perform(put("/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(JsonHelper.toJson(objectMapper, changedUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        final UserDTO res = JsonHelper.fromJson(objectMapper, result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(res.getId(), changedUser.getId());
        assertEquals(res.getUsername(), changedUser.getUsername());
        assertEquals(res.getFirstName(), changedUser.getFirstName());
        assertEquals(res.getLastName(), changedUser.getLastName());
        assertEquals(res.getEmail(), changedUser.getEmail());
        assertEquals(res.getRole(), changedUser.getRole());
        assertNull(res.getPassword());
        assertNull(res.getPasswordConfirmation());
    }

    @Test
    public void testUpdateUserNoAdminCallerFail() throws Exception {
        final UserTokenDTO credentials = generateToken();
        final UserDTO changedUser = getUserFromToken(credentials);

        this.mockMvc.perform(patch("/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(JsonHelper.toJson(objectMapper, changedUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateWithPasswordAndMissmatchFail() throws Exception {
        final UserTokenDTO credentials = generateToken();
        final UserDTO userDTO = getUserFromToken(credentials);
        final UserDTO patchUser = new UserDTO();

        final Optional<User> user = this.userRepository.findByUuid(userDTO.getId());
        if (user.isPresent()) {
            final User userFind = user.get();
            userFind.setRole(UserRole.ADMIN);
            this.userRepository.saveAndFlush(userFind);
        } else {
            fail("user not found");
        }

        patchUser.setId(userDTO.getId());
        patchUser.setFirstName("heyChanged");
        patchUser.setPassword("oui");
        patchUser.setPasswordConfirmation("non");

        this.mockMvc.perform(patch("/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(JsonHelper.toJson(objectMapper, patchUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateWithIdNotFound() throws Exception {
        final UserTokenDTO credentials = generateToken();
        final UserDTO userDTO = getUserFromToken(credentials);
        final UserDTO patchUser = new UserDTO();

        final Optional<User> user = this.userRepository.findByUuid(userDTO.getId());
        if (user.isPresent()) {
            final User userFind = user.get();
            userFind.setRole(UserRole.ADMIN);
            this.userRepository.saveAndFlush(userFind);
        } else {
            fail("user not found");
        }

        patchUser.setId(UUID.randomUUID().toString());

        this.mockMvc.perform(patch("/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(JsonHelper.toJson(objectMapper, patchUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    private UserTokenDTO generateToken() throws Exception {
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

        return JsonHelper.fromJson(objectMapper, mvcResult.getResponse().getContentAsString(), UserTokenDTO.class);
    }

    private UserDTO getUserFromToken(final UserTokenDTO userTokenDTO) throws Exception {
        final MvcResult mvcResultGet = this.mockMvc.perform(get("/user")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userTokenDTO.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        return JsonHelper.fromJson(objectMapper, mvcResultGet.getResponse().getContentAsString(), UserDTO.class);
    }

}
