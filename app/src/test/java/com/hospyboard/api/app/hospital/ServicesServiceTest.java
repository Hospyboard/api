package com.hospyboard.api.app.hospital;

import com.hospyboard.api.app.hospital.dto.HospitalDTO;
import com.hospyboard.api.app.hospital.dto.RoomDTO;
import com.hospyboard.api.app.hospital.dto.ServiceDTO;
import com.hospyboard.api.app.hospital.services.HospitalsService;
import com.hospyboard.api.app.hospital.services.RoomsService;
import com.hospyboard.api.app.hospital.services.ServicesService;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@AutoConfigureMockMvc
public class ServicesServiceTest {

    private final ServicesService service;
    private final HospitalsService hospitalsService;
    private final RoomsService roomsService;

    @Autowired
    public ServicesServiceTest(ServicesService servicesService,
                               HospitalsService hospitalsService,
                               RoomsService roomsService) {
        this.service = servicesService;
        this.hospitalsService = hospitalsService;
        this.roomsService = roomsService;
    }

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

    @Test
    public void testRemove() {
        final ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setName("test service");
        serviceDTO.setHospital(createHospital());

        final ServiceDTO res = service.create(serviceDTO);
        final RoomDTO roomDTO = createRoom(res);

        service.delete(res.getId().toString());

        try {
            roomsService.findById(roomDTO.getId().toString());
            fail("found");
        } catch (ApiNotFoundException ignored) {
        }
    }

    public HospitalDTO createHospital() {
        HospitalDTO hospital = new HospitalDTO();
        hospital.setName("Hospital test");
        hospital.setAddress("hospital address");

        return this.hospitalsService.create(hospital);
    }

    public RoomDTO createRoom(final ServiceDTO serviceDTO) {
        final RoomDTO roomDTO = new RoomDTO();
        roomDTO.setService(serviceDTO);
        roomDTO.setName("Chambre test a supprimer");

        return roomsService.create(roomDTO);
    }

}
