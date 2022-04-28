package com.hospyboard.api.app.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospyboard.api.app.core.JsonHelper;
import com.hospyboard.api.app.user.dto.UserAuthDTO;
import com.hospyboard.api.app.user.dto.UserCreationDTO;
import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.enums.UserRole;
import com.hospyboard.api.app.user.repository.UserRepository;
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
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserCrudTests {

    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserCrudTests(MockMvc mockMvc,
                         ObjectMapper objectMapper,
                         UserRepository userRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    @Test
    public void testPatchUser() throws Exception {
        final UserTokenDTO credentials = generateToken();
        final UserDTO userDTO = getUserFromToken(credentials);
        final UserDTO patchUser = new UserDTO();

        final Optional<User> user = this.userRepository.findByUuid(userDTO.getId().toString());
        if (user.isPresent()) {
            final User userFind = user.get();
            userFind.setRole(UserRole.ADMIN);
            this.userRepository.save(userFind);
        } else {
            fail("user not found");
        }

        patchUser.setId(userDTO.getId());
        patchUser.setFirstName("heyChanged");

        final MvcResult result = this.mockMvc.perform(patch(UserAuthTests.ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(JsonHelper.toJson(objectMapper, patchUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        final UserDTO res = JsonHelper.fromJson(objectMapper, result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(res.getFirstName(), patchUser.getFirstName());
    }

    @Test
    public void testUserCreation() throws Exception {
        final UserTokenDTO credentials = generateToken();
        final UserDTO changedUser = getUserFromToken(credentials);

        final Optional<User> user = this.userRepository.findByUuid(changedUser.getId().toString());
        if (user.isPresent()) {
            final User userFind = user.get();
            userFind.setRole(UserRole.ADMIN);
            this.userRepository.save(userFind);
        } else {
            fail("user not found");
        }

        final UserDTO request = new UserDTO();
        request.setUsername("funix");
        request.setFirstName("funixName");
        request.setLastName("funixLast");
        request.setEmail("email@funix.fr");
        request.setPassword("1234567");
        request.setRole(UserRole.ADMIN);

        final MvcResult result = this.mockMvc.perform(post(UserAuthTests.ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(JsonHelper.toJson(objectMapper, request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        final UserDTO res = JsonHelper.fromJson(objectMapper, result.getResponse().getContentAsString(), UserDTO.class);
        assertNotNull(res.getId());
        assertEquals(res.getUsername(), request.getUsername());
        assertEquals(res.getFirstName(), request.getFirstName());
        assertEquals(res.getLastName(), request.getLastName());
        assertEquals(res.getEmail(), request.getEmail());
        assertEquals(res.getRole(), request.getRole());
        assertNull(res.getPassword());
        assertNull(res.getPasswordConfirmation());
    }

    @Test
    public void testUpdateUserNoAdminCallerFail() throws Exception {
        final UserTokenDTO credentials = generateToken();
        final UserDTO changedUser = getUserFromToken(credentials);

        this.mockMvc.perform(patch(UserAuthTests.ROUTE)
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

        final Optional<User> user = this.userRepository.findByUuid(userDTO.getId().toString());
        if (user.isPresent()) {
            final User userFind = user.get();
            userFind.setRole(UserRole.ADMIN);
            this.userRepository.save(userFind);
        } else {
            fail("user not found");
        }

        patchUser.setId(userDTO.getId());
        patchUser.setFirstName("heyChanged");
        patchUser.setPassword("oui");
        patchUser.setPasswordConfirmation("non");

        this.mockMvc.perform(patch(UserAuthTests.ROUTE)
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

        final Optional<User> user = this.userRepository.findByUuid(userDTO.getId().toString());
        if (user.isPresent()) {
            final User userFind = user.get();
            userFind.setRole(UserRole.ADMIN);
            this.userRepository.save(userFind);
        } else {
            fail("user not found");
        }

        patchUser.setId(UUID.randomUUID());

        this.mockMvc.perform(patch(UserAuthTests.ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(JsonHelper.toJson(objectMapper, patchUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void deleteUser() throws Exception {
        final UserTokenDTO credentials = generateToken();
        final UserDTO userDTO = getUserFromToken(credentials);

        final Optional<User> user = this.userRepository.findByUuid(userDTO.getId().toString());
        if (user.isPresent()) {
            final User userFind = user.get();
            userFind.setRole(UserRole.ADMIN);
            this.userRepository.save(userFind);
        } else {
            fail("user not found");
        }

        this.mockMvc.perform(delete(UserAuthTests.ROUTE + "?id=" + userDTO.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
        ).andExpect(status().isOk());

        this.mockMvc.perform(get(UserAuthTests.ROUTE + userDTO.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteUserWithNoAdmin() throws Exception {
        final UserTokenDTO credentials = generateToken();
        final UserDTO userDTO = getUserFromToken(credentials);

        this.mockMvc.perform(delete(UserAuthTests.ROUTE + "?id=" + userDTO.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testGetUser() throws Exception {
        final UserTokenDTO credentials = generateToken();
        final UserDTO userDTO = getUserFromToken(credentials);

        MvcResult result = this.mockMvc.perform(get(UserAuthTests.ROUTE + userDTO.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
        ).andExpect(status().isOk()).andReturn();

        final UserDTO res = JsonHelper.fromJson(objectMapper, result.getResponse().getContentAsString(), UserDTO.class);
        assertNotNull(res.getId());
    }

    @Test
    public void testGetAll() throws Exception {
        final UserTokenDTO credentials = generateToken();

        this.mockMvc.perform(get(UserAuthTests.ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
        ).andExpect(status().isOk());
    }

    private UserTokenDTO generateToken() throws Exception {
        final UserCreationDTO userCreationDTO = new UserCreationDTO();
        userCreationDTO.setUsername(UUID.randomUUID().toString());
        userCreationDTO.setFirstName("testFirstName");
        userCreationDTO.setLastName("testLastName");
        userCreationDTO.setEmail("test@gmail.com");
        userCreationDTO.setPassword("12345");
        userCreationDTO.setPasswordConfirmation("12345");

        this.mockMvc.perform(post(UserAuthTests.ROUTE + "register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, userCreationDTO)))
                .andExpect(status().isOk());

        final UserAuthDTO authDTO = new UserAuthDTO();
        authDTO.setUsername(userCreationDTO.getUsername());
        authDTO.setPassword(userCreationDTO.getPassword());

        MvcResult mvcResult = this.mockMvc.perform(post(UserAuthTests.ROUTE + "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, authDTO)))
                .andExpect(status().isOk())
                .andReturn();

        return JsonHelper.fromJson(objectMapper, mvcResult.getResponse().getContentAsString(), UserTokenDTO.class);
    }

    private UserDTO getUserFromToken(final UserTokenDTO userTokenDTO) throws Exception {
        final MvcResult mvcResultGet = this.mockMvc.perform(get(UserAuthTests.ROUTE + "session")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userTokenDTO.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        return JsonHelper.fromJson(objectMapper, mvcResultGet.getResponse().getContentAsString(), UserDTO.class);
    }
}
