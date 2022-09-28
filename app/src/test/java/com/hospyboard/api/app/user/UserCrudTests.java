package com.hospyboard.api.app.user;

import com.hospyboard.api.app.core.JsonHelper;
import com.hospyboard.api.app.core.UserHelper;
import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.dto.UserResetPasswordDTO;
import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.hospyboard.api.app.user.enums.UserRole;
import com.hospyboard.api.app.user.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserCrudTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonHelper jsonHelper;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private UserService userService;

    @Test
    public void testPatchUser() throws Exception {
        final UserTokenDTO credentials = userHelper.generateAdminToken();
        final UserDTO userDTO = credentials.getUser();
        final UserDTO patchUser = new UserDTO();

        patchUser.setId(userDTO.getId());
        patchUser.setFirstName("heyChanged");
        patchUser.setPassword("heyChanged2");
        patchUser.setPasswordConfirmation("heyChanged2");

        this.mockMvc.perform(patch(UserAuthTests.ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(jsonHelper.toJson(patchUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        final UserDTO res = userService.update(patchUser);
        assertEquals(res.getFirstName(), patchUser.getFirstName());
    }

    @Test
    public void testPatchUserFailPasswordsNoMatch() throws Exception {
        final UserTokenDTO credentials = userHelper.generateAdminToken();
        final UserDTO userDTO = credentials.getUser();
        final UserDTO patchUser = new UserDTO();

        patchUser.setId(userDTO.getId());
        patchUser.setPassword("heyChanged");
        patchUser.setPasswordConfirmation("heyChanged2");

        this.mockMvc.perform(patch(UserAuthTests.ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(jsonHelper.toJson(patchUser))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testPatchUserInvalidAuth() throws Exception {
        this.mockMvc.perform(patch(UserAuthTests.ROUTE)
                .content(jsonHelper.toJson(new UserDTO()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());

        this.mockMvc.perform(patch(UserAuthTests.ROUTE)
                .content(jsonHelper.toJson(new UserDTO()))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userHelper.generatePatientToken().getToken())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testPatchMultipleValid() throws Exception {
        final String replacement = "ça alors";
        final UserTokenDTO credentials = userHelper.generateAdminToken();
        final List<UserDTO> users = userHelper.generateUsers(5, UserRole.PATIENT);

        for (UserDTO user : users) {
            user.setInfos(replacement);
        }

        this.mockMvc.perform(patch(UserAuthTests.ROUTE + "/batch")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(jsonHelper.toJson(users))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        for (final UserDTO user : userService.update(users)) {
            assertEquals(replacement, user.getInfos());
        }
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

        this.mockMvc.perform(post(UserAuthTests.ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(jsonHelper.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        request.setUsername("funix2");
        final UserDTO res = userService.create(request);
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

        final UserDTO res = jsonHelper.fromJson(result.getResponse().getContentAsString(), UserDTO.class);
        assertNotNull(res.getId());
    }

    @Test
    public void testGetAll() throws Exception {
        final UserTokenDTO credentials = userHelper.generateAdminToken();

        this.mockMvc.perform(get(UserAuthTests.ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
        ).andExpect(status().isOk());
    }

    @Test
    public void resetPassword() throws Exception {
        final UserTokenDTO credentials = userHelper.generatePatientToken();
        final UserResetPasswordDTO resetPasswordDTO = new UserResetPasswordDTO();

        resetPasswordDTO.setNewPassword("12345");
        resetPasswordDTO.setNewPasswordConfirmation("12345");

        this.mockMvc.perform(patch(UserAuthTests.ROUTE + "changePassword")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(jsonHelper.toJson(resetPasswordDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void resetPasswordNoMatch() throws Exception {
        final UserTokenDTO credentials = userHelper.generatePatientToken();
        final UserResetPasswordDTO resetPasswordDTO = new UserResetPasswordDTO();

        resetPasswordDTO.setNewPassword("1234522");
        resetPasswordDTO.setNewPasswordConfirmation("12345");

        this.mockMvc.perform(patch(UserAuthTests.ROUTE + "changePassword")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(jsonHelper.toJson(resetPasswordDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void resetPasswordActualPasswordNoMatch() throws Exception {
        final UserTokenDTO credentials = userHelper.generatePatientToken();
        final UserResetPasswordDTO resetPasswordDTO = new UserResetPasswordDTO();

        resetPasswordDTO.setNewPassword("1234522");
        resetPasswordDTO.setNewPasswordConfirmation("12345");

        this.mockMvc.perform(patch(UserAuthTests.ROUTE + "changePassword")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + credentials.getToken())
                .content(jsonHelper.toJson(resetPasswordDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }
}
