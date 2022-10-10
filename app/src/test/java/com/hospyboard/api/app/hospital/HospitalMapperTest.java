package com.hospyboard.api.app.hospital;

import com.hospyboard.api.app.hospital.dto.HospitalDTO;
import com.hospyboard.api.app.hospital.entity.Hospital;
import com.hospyboard.api.app.hospital.mappers.HospitalMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureMockMvc
public class HospitalMapperTest {

    private final HospitalMapper hospitalMapper;

    @Autowired
    public HospitalMapperTest(HospitalMapper hospitalMapper) {
        this.hospitalMapper = hospitalMapper;
    }

    @Test
    public void testCreateEntity() {
        final HospitalDTO hospitalDTO = new HospitalDTO();

        hospitalDTO.setAddress("12 rue du test");
        hospitalDTO.setName("HospitalTest");
        hospitalDTO.setCreatedAt(Date.from(Instant.now()));
        hospitalDTO.setUpdatedAt(Date.from(Instant.now()));
        hospitalDTO.setId(UUID.randomUUID());

        final Hospital hospital = hospitalMapper.toEntity(hospitalDTO);
        assertEquals(hospitalDTO.getAddress(), hospital.getAddress());
        assertEquals(hospitalDTO.getName(), hospital.getName());
        assertEquals(hospitalDTO.getCreatedAt().getTime(), hospital.getCreatedAt().getTime());
        assertEquals(hospitalDTO.getUpdatedAt().getTime(), hospital.getUpdatedAt().getTime());
        assertEquals(hospitalDTO.getId(), hospital.getUuid());

        assertNull(hospitalMapper.toEntity(null));
    }

    @Test
    public void testCreateDto() {
        final Hospital hospital = new Hospital();

        hospital.setAddress("12 rue du test");
        hospital.setName("HospitalTest");
        hospital.setCreatedAt(Date.from(Instant.now()));
        hospital.setUpdatedAt(Date.from(Instant.now()));
        hospital.setId(1L);
        hospital.setUuid(UUID.randomUUID());

        final HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);
        assertEquals(hospitalDTO.getAddress(), hospital.getAddress());
        assertEquals(hospitalDTO.getName(), hospital.getName());
        assertEquals(hospitalDTO.getCreatedAt().getTime(), hospital.getCreatedAt().getTime());
        assertEquals(hospitalDTO.getUpdatedAt().getTime(), hospital.getUpdatedAt().getTime());
        assertEquals(hospitalDTO.getId(), hospital.getUuid());

        assertNull(hospitalMapper.toDto(null));
    }

    @Test
    public void testPatch() {
        final Hospital hospital = new Hospital();

        hospital.setAddress("12 rue du test");
        hospital.setName("HospitalTest");
        hospital.setCreatedAt(Date.from(Instant.now()));
        hospital.setUpdatedAt(Date.from(Instant.now()));
        hospital.setId(1L);
        hospital.setUuid(UUID.randomUUID());

        final Hospital hospital2 = new Hospital();
        hospital2.setAddress("12 rue du test2");
        hospital2.setName("HospitalTest2");
        hospital2.setCreatedAt(Date.from(Instant.now()));
        hospital2.setUpdatedAt(Date.from(Instant.now()));
        hospital2.setId(1L);
        hospital2.setUuid(UUID.randomUUID());

        hospitalMapper.patch(hospital2, hospital);
        assertEquals(hospital2.getAddress(), hospital.getAddress());
        assertEquals(hospital2.getName(), hospital.getName());
        assertEquals(hospital2.getCreatedAt().getTime(), hospital.getCreatedAt().getTime());
        assertEquals(hospital2.getUpdatedAt().getTime(), hospital.getUpdatedAt().getTime());
        assertEquals(hospital2.getId(), hospital.getId());

        hospitalMapper.patch(null, new Hospital());
        hospitalMapper.patch(new Hospital(), new Hospital());
    }

    @Test
    public void testPatchNull() {
        final Hospital hospital = new Hospital();
    }

}
