package com.hospyboard.api.app.file_storage;

import com.hospyboard.api.app.core.JsonHelper;
import com.hospyboard.api.app.core.UserHelper;
import com.hospyboard.api.app.file_storage.dtos.FileDTO;
import com.hospyboard.api.app.user.dto.UserTokenDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FileStorageResourceTest {

    private final MockMvc mockMvc;
    private final JsonHelper jsonHelper;
    private final UserTokenDTO adminToken;

    @Autowired
    public FileStorageResourceTest(MockMvc mockMvc,
                                   JsonHelper jsonHelper,
                                   UserHelper userHelper) throws Exception {
        this.mockMvc = mockMvc;
        this.jsonHelper = jsonHelper;
        this.adminToken = userHelper.generateAdminToken();
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get("/files")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken.getToken())
        ).andExpect(status().isOk());
    }

    @Test
    void testUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());

        mockMvc.perform(multipart("/files")
                .file(file)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken.getToken())
        ).andExpect(status().isOk());
    }

    @Test
    void testDownload() throws Exception {
        MockMultipartFile file = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());

        MvcResult result = mockMvc.perform(multipart("/files")
                .file(file)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken.getToken())
        ).andExpect(status().isOk()).andReturn();
        final FileDTO fileDTO = jsonHelper.fromJson(result.getResponse().getContentAsString(), FileDTO.class);

        mockMvc.perform(delete("/files?id=" + fileDTO.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken.getToken())
        ).andExpect(status().isOk());
    }

}
