package com.hospyboard.api.app.user;

import com.hospyboard.api.app.core.JsonHelper;
import com.hospyboard.api.app.user.dto.UserAuthDTO;
import com.hospyboard.api.app.user.dto.UserCreationDTO;
import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.enums.UserRole;
import com.hospyboard.api.app.user.repository.UserRepository;
import com.hospyboard.api.app.user.services.CurrentUser;
import com.hospyboard.api.app.user.services.UserService;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserAuthTests {

    public static final String ROUTE = "/user/";
    private final MockMvc mockMvc;
    private final JsonHelper objectMapper;
    private final CurrentUser currentUser;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserAuthTests(MockMvc mockMvc,
                         JsonHelper objectMapper,
                         CurrentUser currentUser,
                         UserService userService,
                         UserRepository userRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.currentUser = currentUser;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        new UserRole();
        final UserCreationDTO userCreationDTO = new UserCreationDTO();

        userCreationDTO.setUsername("testUser");
        userCreationDTO.setFirstName("testFirstName");
        userCreationDTO.setLastName("testLastName");
        userCreationDTO.setEmail("test@gmail.com");
        userCreationDTO.setPassword("12345");
        userCreationDTO.setPasswordConfirmation("12345");

        MvcResult mvcResult = this.mockMvc.perform(post(ROUTE + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.toJson(userCreationDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final UserDTO result = objectMapper.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class);

        assertNotNull(result.getId());
        assertNotNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
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

        this.mockMvc.perform(post(ROUTE + "register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.toJson(userCreationDTO)))
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

        this.mockMvc.perform(post(ROUTE + "register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.toJson(userCreationDTO)))
                .andExpect(status().isOk());
        this.mockMvc.perform(post(ROUTE + "register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.toJson(userCreationDTO)))
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

        this.mockMvc.perform(post(ROUTE + "register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.toJson(userCreationDTO)))
                .andExpect(status().isOk());

        final UserAuthDTO authDTO = new UserAuthDTO();
        authDTO.setUsername(userCreationDTO.getUsername());
        authDTO.setPassword(userCreationDTO.getPassword());

        MvcResult mvcResult = this.mockMvc.perform(post(ROUTE + "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.toJson(authDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final UserTokenDTO result = objectMapper.fromJson(mvcResult.getResponse().getContentAsString(), UserTokenDTO.class);
        assertNotNull(result.getExpirationDate());
        assertNotNull(result.getToken());
        assertNotNull(result.getUser());
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

        this.mockMvc.perform(post(ROUTE + "register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.toJson(userCreationDTO)))
                .andExpect(status().isOk());

        final UserAuthDTO authDTO = new UserAuthDTO();
        authDTO.setUsername(userCreationDTO.getUsername());
        authDTO.setPassword("WrongPassword");

        this.mockMvc.perform(post(ROUTE + "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.toJson(authDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFailLoginUserUnknown() throws Exception {
        final UserAuthDTO authDTO = new UserAuthDTO();
        authDTO.setUsername("NoAccountHere");
        authDTO.setPassword("password");

        this.mockMvc.perform(post(ROUTE + "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.toJson(authDTO)))
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

        this.mockMvc.perform(post(ROUTE + "register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.toJson(userCreationDTO)))
                .andExpect(status().isOk());

        final UserAuthDTO authDTO = new UserAuthDTO();
        authDTO.setUsername(userCreationDTO.getUsername());
        authDTO.setPassword(userCreationDTO.getPassword());

        MvcResult mvcResult = this.mockMvc.perform(post(ROUTE + "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.toJson(authDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final UserTokenDTO result = objectMapper.fromJson(mvcResult.getResponse().getContentAsString(), UserTokenDTO.class);

        final MvcResult mvcResultGet = this.mockMvc.perform(get(ROUTE + "session")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + result.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        final UserDTO account = objectMapper.fromJson(mvcResultGet.getResponse().getContentAsString(), UserDTO.class);
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
    public void testLogout() throws Exception {
        final UserCreationDTO userCreationDTO = new UserCreationDTO();
        userCreationDTO.setUsername(UUID.randomUUID().toString());
        userCreationDTO.setFirstName("testFirstName");
        userCreationDTO.setLastName("testLastName");
        userCreationDTO.setEmail("test@gmail.com");
        userCreationDTO.setPassword("12345");
        userCreationDTO.setPasswordConfirmation("12345");

        this.mockMvc.perform(post(ROUTE + "register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.toJson(userCreationDTO)))
                .andExpect(status().isOk());

        final UserAuthDTO authDTO = new UserAuthDTO();
        authDTO.setUsername(userCreationDTO.getUsername());
        authDTO.setPassword(userCreationDTO.getPassword());

        MvcResult mvcResult = this.mockMvc.perform(post(ROUTE + "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.toJson(authDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final UserTokenDTO result = objectMapper.fromJson(mvcResult.getResponse().getContentAsString(), UserTokenDTO.class);
        this.mockMvc.perform(get(ROUTE + "logout")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + result.getToken()))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(ROUTE + "session")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + result.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testGettingActualUserNoAuthError() throws Exception {
        this.mockMvc.perform(get(ROUTE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testInvalidTokenAccess() throws Exception {
        this.mockMvc.perform(get(ROUTE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer qlkjd75")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void serviceGetUserNoAuth() {
        try {
            this.currentUser.getCurrentUser();
            fail();
        } catch (ApiForbiddenException ignored) {
        }
    }

    @Test
    public void testLoadByUsername() {
        User user = new User();
        user.setUsername("USERtESTGGGG");
        user.setPassword("passss");
        user.setFirstName("oui");
        user.setLastName("oui");
        user.setEmail("oui.gmail.com");
        user.setRole(UserRole.PATIENT);

        user = this.userRepository.save(user);
        final UserDetails res = this.userService.loadUserByUsername(user.getUsername());
        assertEquals(user.getUsername(), res.getUsername());
        assertEquals(user.getPassword(), res.getPassword());
    }
}
