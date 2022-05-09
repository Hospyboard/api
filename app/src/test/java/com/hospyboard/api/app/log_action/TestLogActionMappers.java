package com.hospyboard.api.app.log_action;

import com.hospyboard.api.app.log_action.dtos.LogActionDTO;
import com.hospyboard.api.app.log_action.entities.LogAction;
import com.hospyboard.api.app.log_action.mappers.LogActionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestLogActionMappers {

    @Autowired
    private LogActionMapper logActionMapper;

    @Test
    public void mappingToEntity() {
        final Date date = Date.from(Instant.now());
        final LogActionDTO logActionDTO = new LogActionDTO();

        logActionDTO.setHttpMethod("POST");
        logActionDTO.setIp("127.0.0.1");
        logActionDTO.setRoute("localhost/user");
        logActionDTO.setId(UUID.randomUUID());
        logActionDTO.setWhen(date);
        logActionDTO.setCreatedAt(date);
        logActionDTO.setUpdatedAt(date);

        final LogAction logAction = logActionMapper.toEntity(logActionDTO);

        assertEquals(logActionDTO.getHttpMethod(), logAction.getHttpMethod());
        assertEquals(logActionDTO.getIp(), logAction.getIp());
        assertEquals(logActionDTO.getRoute(), logAction.getRoute());
        assertEquals(logActionDTO.getWhen(), logAction.getWhen());
        assertEquals(logActionDTO.getIp(), logAction.getIp());
        assertEquals(logActionDTO.getCreatedAt(), logAction.getCreatedAt());
        assertEquals(logActionDTO.getUpdatedAt(), logAction.getUpdatedAt());
        assertEquals(logActionDTO.getId(), logAction.getUuid());
    }

    @Test
    public void mappingToDTO() {
        final Date date = Date.from(Instant.now());
        final LogAction logAction = new LogAction();

        logAction.setHttpMethod("POST");
        logAction.setIp("127.0.0.1");
        logAction.setRoute("localhost/user");
        logAction.setUuid(UUID.randomUUID());
        logAction.setId(1L);
        logAction.setWhen(date);
        logAction.setCreatedAt(date);
        logAction.setUpdatedAt(date);

        final LogActionDTO logActionDTO = logActionMapper.toDto(logAction);

        assertEquals(logActionDTO.getHttpMethod(), logAction.getHttpMethod());
        assertEquals(logActionDTO.getIp(), logAction.getIp());
        assertEquals(logActionDTO.getRoute(), logAction.getRoute());
        assertEquals(logActionDTO.getWhen(), logAction.getWhen());
        assertEquals(logActionDTO.getIp(), logAction.getIp());
        assertEquals(logActionDTO.getCreatedAt(), logAction.getCreatedAt());
        assertEquals(logActionDTO.getUpdatedAt(), logAction.getUpdatedAt());
        assertEquals(logActionDTO.getId(), logAction.getUuid());
    }

    @Test
    public void mappingPatch() {
        final Date date = Date.from(Instant.now());
        final String ip = "uneIP";

        final LogAction logAction = new LogAction();

        logAction.setHttpMethod("POST");
        logAction.setIp(ip);
        logAction.setRoute("localhost/user");
        logAction.setUuid(UUID.randomUUID());
        logAction.setId(1L);
        logAction.setWhen(date);
        logAction.setCreatedAt(date);
        logAction.setUpdatedAt(date);

        final LogAction logAction2 = new LogAction();

        logAction2.setHttpMethod("POST");
        logAction2.setIp("127.0.0.1");
        logAction2.setRoute("localhost/user");
        logAction2.setUuid(UUID.randomUUID());
        logAction2.setId(1L);
        logAction2.setWhen(date);
        logAction2.setCreatedAt(date);
        logAction2.setUpdatedAt(date);

        logActionMapper.patch(logAction, logAction2);

        assertEquals(logAction2.getHttpMethod(), logAction.getHttpMethod());
        assertEquals(logAction2.getIp(), ip);
        assertEquals(logAction2.getRoute(), logAction.getRoute());
        assertEquals(logAction2.getWhen(), logAction.getWhen());
        assertEquals(logAction2.getIp(), logAction.getIp());
        assertEquals(logAction2.getCreatedAt(), logAction.getCreatedAt());
        assertEquals(logAction2.getUpdatedAt(), logAction.getUpdatedAt());
        assertEquals(logAction2.getId(), logAction.getId());
    }

}
