package com.hospyboard.api.app.hospital;

import com.hospyboard.api.app.hospital.dto.HospitalDTO;
import com.hospyboard.api.app.hospital.dto.ServiceDTO;
import com.hospyboard.api.app.hospital.entity.Hospital;
import com.hospyboard.api.app.hospital.services.HospitalsService;
import com.hospyboard.api.app.hospital.services.ServicesService;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@AutoConfigureMockMvc
public class ServicesServiceTest {

    @Autowired
    private ServicesService service;

    @Autowired
    private HospitalsService hospitalsService;

    @Test
    public void testServiceCreation() {
        final ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setName("test service");
        serviceDTO.setHospital(createHospital());

        final ServiceDTO res = service.create(serviceDTO);
        assertEquals(serviceDTO.getName(), res.getName());
        assertEquals(serviceDTO.getHospital().getId(), res.getHospital().getId());
    }

    @Test
    public void testServiceCreationNoHospital() {
        final ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setName("test service");

        try {
            service.create(serviceDTO);
            fail("error needed");
        } catch (ApiBadRequestException ignored) {
        } catch (ApiNotFoundException e) {
            fail(e);
        }
    }

    @Test
    public void testServiceCreationNoHospitalId() {
        final HospitalDTO hospitalDTO = new HospitalDTO();
        final ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setName("test service");
        serviceDTO.setHospital(hospitalDTO);

        try {
            service.create(serviceDTO);
            fail("error needed");
        } catch (ApiBadRequestException ignored) {
        } catch (ApiNotFoundException e) {
            fail(e);
        }
    }

    @Test
    public void testServiceCreationHospitalIdNotFound() {
        final HospitalDTO hospitalDTO = new HospitalDTO();
        final ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setName("test service");
        hospitalDTO.setId(UUID.randomUUID());
        serviceDTO.setHospital(hospitalDTO);

        try {
            service.create(serviceDTO);
            fail("error needed");
        } catch (ApiNotFoundException ignored) {
        } catch (ApiBadRequestException e) {
            fail(e);
        }
    }

    @Test
    public void testUpdate() {
        final ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setName("test service");
        serviceDTO.setHospital(createHospital());

        final ServiceDTO res = service.create(serviceDTO);
        res.setName("test patch");

        final ServiceDTO res2 = service.update(res);
        assertEquals(res.getName(), res2.getName());
    }

    @Test
    public void testUpdateNoId() {
        final ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setName("test service");
        serviceDTO.setHospital(createHospital());

        final ServiceDTO res = service.create(serviceDTO);
        res.setId(null);

        try {
            service.update(res);
            fail("need error");
        } catch (ApiBadRequestException ignored) {
        } catch (ApiNotFoundException e) {
            fail(e);
        }
    }

    @Test
    public void testUpdateIdNotFound() {
        final ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setName("test service");
        serviceDTO.setHospital(createHospital());

        final ServiceDTO res = service.create(serviceDTO);
        res.setId(UUID.randomUUID());

        try {
            service.update(res);
            fail("need error");
        } catch (ApiNotFoundException ignored) {
        } catch (ApiBadRequestException e) {
            fail(e);
        }
    }

    private HospitalDTO createHospital() {
        Hospital hospital = new Hospital();
        hospital.setName("Hospital test");
        hospital.setAddress("hospital address");

        hospital = this.hospitalsService.getRepository().save(hospital);
        return this.hospitalsService.getMapper().toDto(hospital);
    }

}
