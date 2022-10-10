package com.hospyboard.api.app.core;

import com.hospyboard.api.app.hospital.repositories.HospitalRepository;
import com.hospyboard.api.app.hospital.repositories.RoomRepository;
import com.hospyboard.api.app.hospital.repositories.ServiceRepository;
import com.hospyboard.api.app.user.UserAuthTests;
import com.hospyboard.api.app.user.dto.UserAuthDTO;
import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.enums.UserRole;
import com.hospyboard.api.app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@RequiredArgsConstructor
public class UserHelper {

    private final MockMvc mockMvc;
    private final JsonHelper objectMapper;
    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;
    private final ServiceRepository serviceRepository;
    private final RoomRepository roomRepository;

    public UserTokenDTO generatePatientToken() throws Exception {
        return generateToken("patient", UserRole.PATIENT);
    }

    public UserTokenDTO generateHospitalToken() throws Exception {
        return generateToken("hospital", UserRole.HOSPYTAL_WORKER);
    }

    public UserTokenDTO generateAdminToken() throws Exception {
        return generateToken("admin", UserRole.ADMIN);
    }

    public List<UserDTO> generateUsers(int amount, String role) throws Exception {
        final List<UserDTO> users = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            users.add(generateToken(UUID.randomUUID().toString(), role).getUser());
        }
        return users;
    }

    public UserTokenDTO generateToken(final String username, final String role) throws Exception {
        final String password = "passwordOfTestAccount22ZZ";
        final Optional<User> search = userRepository.findByUsername(username);
        final User user;

        if (search.isEmpty()) {
            final User userRequest = new User();
            userRequest.setUsername(username);
            userRequest.setFirstName("testFirstName");
            userRequest.setLastName("testLastName");
            userRequest.setEmail("test@gmail.com");
            userRequest.setRole(role);
            userRequest.setPassword(password);

            user = userRepository.saveAndFlush(userRequest);
        } else {
            user = search.get();
        }

        final UserAuthDTO authDTO = new UserAuthDTO();
        authDTO.setUsername(user.getUsername());
        authDTO.setPassword(user.getPassword());

        MvcResult mvcResult = this.mockMvc.perform(post(UserAuthTests.ROUTE + "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.toJson(authDTO)))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.fromJson(mvcResult.getResponse().getContentAsString(), UserTokenDTO.class);
    }

}
