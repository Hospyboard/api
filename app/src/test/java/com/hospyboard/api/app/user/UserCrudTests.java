package com.hospyboard.api.app.user;

import com.hospyboard.api.app.core.JsonHelper;
import com.hospyboard.api.app.core.UserHelper;
import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.hospyboard.api.app.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserCrudTests {

    private final MockMvc mockMvc;
    private final JsonHelper objectMapper;
    private final UserHelper userHelper;

    @Autowired
    public UserCrudTests(MockMvc mockMvc,
                         JsonHelper objectMapper,
                         UserHelper userHelper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userHelper = userHelper;
    }

    @Test
    public void testPatchUser() throws Exception {
        final UserTokenDTO credentials = userHelper.generateAdminToken();
        final UserDTO userDTO = credentials.getUser();
        final UserDTO patchUser = new UserDTO();

        patchUser.setId(userDTO.getId());
        patchUser.setFirstName("heyChanged");

        final MvcResult result = this.mockMvc.perform(patch(UserAuthTests.ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(objectMapper.toJson(patchUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        final UserDTO res = objectMapper.fromJson(result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(res.getFirstName(), patchUser.getFirstName());
    }

    @Test
    public void testUserCreation() throws Exception {
        final UserTokenDTO credentials = userHelper.generateAdminToken();

        final UserDTO request = new UserDTO();
        request.setUsername("funix");
        request.setFirstName("funixName");
        request.setLastName("funixLast");
        request.setEmail("email@funix.fr");
        request.setPassword("1234567");
        request.setRole(UserRole.ADMIN);
        request.setInfos("très naze comme patient");

        final MvcResult result = this.mockMvc.perform(post(UserAuthTests.ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(objectMapper.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        final UserDTO res = objectMapper.fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8), UserDTO.class);
        assertNotNull(res.getId());
        assertEquals(res.getUsername(), request.getUsername());
        assertEquals(res.getFirstName(), request.getFirstName());
        assertEquals(res.getLastName(), request.getLastName());
        assertEquals(res.getEmail(), request.getEmail());
        assertEquals(res.getRole(), request.getRole());
        assertEquals(res.getInfos(), request.getInfos());
        assertNull(res.getPassword());
        assertNull(res.getPasswordConfirmation());
    }

    @Test
    public void testUpdateUserNoAdminCallerFail() throws Exception {
        final UserTokenDTO credentials = userHelper.generatePatientToken();
        final UserDTO userDTO = credentials.getUser();

        this.mockMvc.perform(patch(UserAuthTests.ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(objectMapper.toJson(userDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateWithPasswordAndMissmatchFail() throws Exception {
        final UserTokenDTO credentials = userHelper.generateAdminToken();
        final UserDTO userDTO = credentials.getUser();
        final UserDTO patchUser = new UserDTO();

        patchUser.setId(userDTO.getId());
        patchUser.setFirstName("heyChanged");
        patchUser.setPassword("oui");
        patchUser.setPasswordConfirmation("non");

        this.mockMvc.perform(patch(UserAuthTests.ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(objectMapper.toJson(patchUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateWithIdNotFound() throws Exception {
        final UserTokenDTO credentials = userHelper.generateAdminToken();
        final UserDTO patchUser = new UserDTO();

        patchUser.setId(UUID.randomUUID());

        this.mockMvc.perform(patch(UserAuthTests.ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(objectMapper.toJson(patchUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateWithNoId() throws Exception {
        final UserTokenDTO credentials = userHelper.generateAdminToken();
        final UserDTO patchUser = new UserDTO();

        patchUser.setId(null);

        this.mockMvc.perform(patch(UserAuthTests.ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(objectMapper.toJson(patchUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void deleteUser() throws Exception {
        final UserTokenDTO credentials = userHelper.generateAdminToken();
        final UserDTO userDTO = credentials.getUser();

        this.mockMvc.perform(delete(UserAuthTests.ROUTE + "?id=" + userDTO.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
        ).andExpect(status().isOk());

        this.mockMvc.perform(get(UserAuthTests.ROUTE + userDTO.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteUserWithNoAdmin() throws Exception {
        final UserTokenDTO credentials = userHelper.generatePatientToken();
        final UserDTO userDTO = credentials.getUser();

        this.mockMvc.perform(delete(UserAuthTests.ROUTE + "?id=" + userDTO.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testGetUser() throws Exception {
        final UserTokenDTO credentials = userHelper.generateAdminToken();
        final UserDTO userDTO = credentials.getUser();

        MvcResult result = this.mockMvc.perform(get(UserAuthTests.ROUTE + userDTO.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
        ).andExpect(status().isOk()).andReturn();

        final UserDTO res = objectMapper.fromJson(result.getResponse().getContentAsString(), UserDTO.class);
        assertNotNull(res.getId());
    }

    @Test
    public void testGetAll() throws Exception {
        final UserTokenDTO credentials = userHelper.generateAdminToken();

        this.mockMvc.perform(get(UserAuthTests.ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
        ).andExpect(status().isOk());
    }
}
