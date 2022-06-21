package com.hospyboard.api.app.hospital;

import com.hospyboard.api.app.hospital.dto.HospitalDTO;
import com.hospyboard.api.app.hospital.entity.Hospital;
import com.hospyboard.api.app.hospital.mappers.HospitalMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TestHospitalMapper {

    @Autowired
    private HospitalMapper hospitalMapper;

    @Test
    public void testCreate() {
        final HospitalDTO hospitalDTO = new HospitalDTO();

        hospitalDTO.setAddress("12 rue du test");
        hospitalDTO.setName("HospitalTest");
        hospitalDTO.setCreatedAt(Date.from(Instant.now()));
        hospitalDTO.setUpdatedAt(Date.from(Instant.now()));
        hospitalDTO.setId(UUID.randomUUID());

        final Hospital hospital = hospitalMapper.toEntity(hospitalDTO);
        assertEquals(hospitalDTO.getAddress(), hospital.getAddress());
        assertEquals(hospitalDTO.getName(), hospital.getName());
        assertEquals(hospital.getCreatedAt().getTime(), hospital.getCreatedAt().getTime());
        assertEquals(hospital.getUpdatedAt().getTime(), hospital.getUpdatedAt().getTime());
        assertEquals(hospitalDTO.getId(), hospital.getUuid());
    }

}
