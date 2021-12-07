package com.hospyboard.api.log_action.resources;

import com.hospyboard.api.core.db_converters.EncryptionDatabaseInstant;
import com.hospyboard.api.core.db_converters.EncryptionDatabaseString;
import com.hospyboard.api.log_actions.dto.HospyboardActionDTO;
import com.hospyboard.api.log_actions.resources.HospyboardActionResource;
import com.hospyboard.api.log_actions.services.HospyboardActionsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HospyboardActionResource.class)
@AutoConfigureMockMvc
public class TestActionsResource {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HospyboardActionsService service;
    @MockBean
    private EncryptionDatabaseString encryptionDatabaseString;
    @MockBean
    private EncryptionDatabaseInstant encryptionDatabaseInstant;

    @Test
    public void testAddNewAction() throws Exception {
        final HospyboardActionDTO dto = new HospyboardActionDTO();

        dto.setRequestedAt(Instant.now());
        dto.setRouteName("/test");
        dto.setService("UNIT SPRING TEST");
        dto.setUserUuid(UUID.randomUUID().toString());

        when(service.doAction(any(HospyboardActionDTO.class))).thenReturn(dto);
        when(encryptionDatabaseInstant.convertToDatabase(anyString())).thenReturn("String");
        when(encryptionDatabaseString.convertToDatabase(anyString())).thenReturn("String");

        this.mockMvc.perform(post("/log"))
                .andExpect(status().isOk());
    }

}
