package com.hospyboard.api.app.alert;

import com.hospyboard.api.app.alert.dto.AlertDTO;
import com.hospyboard.api.app.alert.enums.AlertImportance;
import com.hospyboard.api.app.alert.enums.AlertStatus;
import com.hospyboard.api.app.alert.enums.AlertType;
import com.hospyboard.api.app.core.JsonHelper;
import com.hospyboard.api.app.core.UserHelper;
import com.hospyboard.api.app.user.dto.UserTokenDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AlertCrudTests {

    private static final String ROUTE = "/alert";
    private final MockMvc mockMvc;
    private final JsonHelper jsonHelper;
    private final UserTokenDTO patientToken;
    private final UserTokenDTO adminToken;

    @Autowired
    public AlertCrudTests(MockMvc mockMvc,
                          JsonHelper jsonHelper,
                          UserHelper userHelper) throws Exception {
        this.mockMvc = mockMvc;
        this.jsonHelper = jsonHelper;
        this.patientToken = userHelper.generatePatientToken();
        this.adminToken = userHelper.generateAdminToken();
    }

    @Test
    public void testAlertCreation() throws Exception {
        final AlertDTO alertDTO = new AlertDTO();
        alertDTO.setImportance(AlertImportance.URGENT);
        alertDTO.setType(AlertType.BODY_ISSUE);
        alertDTO.setInfos("mal de crane");

        final MvcResult mvcResult = mockMvc.perform(post(ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + patientToken.getToken())
                .content(jsonHelper.toJson(alertDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        final AlertDTO response = convert(mvcResult);

        assertNull(response.getStaff());
        assertEquals(patientToken.getUser().getId(), response.getPatient().getId());
        assertEquals(alertDTO.getImportance(), response.getImportance());
        assertEquals(alertDTO.getType(), response.getType());
        assertEquals(alertDTO.getInfos(), response.getInfos());
        assertEquals(AlertStatus.PENDING, response.getStatus());
    }

    @Test
    public void testPatchPatient() throws Exception {
        AlertDTO alertDTO = new AlertDTO();
        alertDTO.setImportance(AlertImportance.URGENT);
        alertDTO.setType(AlertType.BODY_ISSUE);
        alertDTO.setInfos("mal de crane");

        MvcResult mvcResult = mockMvc.perform(post(ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + patientToken.getToken())
                .content(jsonHelper.toJson(alertDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        alertDTO = convert(mvcResult);

        alertDTO.setImportance(AlertImportance.BIT_URGENT);

        mvcResult = mockMvc.perform(patch(ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + patientToken.getToken())
                .content(jsonHelper.toJson(alertDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        final AlertDTO response = convert(mvcResult);
        assertNull(response.getStaff());
        assertEquals(patientToken.getUser().getId(), response.getPatient().getId());
        assertEquals(alertDTO.getImportance(), response.getImportance());
        assertEquals(alertDTO.getType(), response.getType());
        assertEquals(alertDTO.getInfos(), response.getInfos());
        assertEquals(alertDTO.getStatus(), response.getStatus());
    }

    @Test
    public void testPatchAdmin() throws Exception {
        AlertDTO alertDTO = new AlertDTO();
        alertDTO.setImportance(AlertImportance.URGENT);
        alertDTO.setType(AlertType.BODY_ISSUE);
        alertDTO.setInfos("mal de crane");

        MvcResult mvcResult = mockMvc.perform(post(ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + patientToken.getToken())
                .content(jsonHelper.toJson(alertDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        alertDTO = convert(mvcResult);

        alertDTO.setImportance(AlertImportance.BIT_URGENT);
        alertDTO.setStatus(AlertStatus.IN_PROGRESS);

        mvcResult = mockMvc.perform(patch(ROUTE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken.getToken())
                .content(jsonHelper.toJson(alertDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        final AlertDTO response = convert(mvcResult);
        assertEquals(adminToken.getUser().getId(), response.getStaff().getId());
        assertEquals(patientToken.getUser().getId(), response.getPatient().getId());
        assertEquals(alertDTO.getImportance(), response.getImportance());
        assertEquals(alertDTO.getType(), response.getType());
        assertEquals(alertDTO.getInfos(), response.getInfos());
        assertEquals(alertDTO.getStatus(), response.getStatus());
    }

    private AlertDTO convert(final MvcResult result) throws Exception {
        return jsonHelper.fromJson(result.getResponse().getContentAsString(), AlertDTO.class);
    }

}
