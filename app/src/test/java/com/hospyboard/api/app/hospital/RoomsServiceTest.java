package com.hospyboard.api.app.hospital;

import com.hospyboard.api.app.core.exceptions.BadRequestException;
import com.hospyboard.api.app.hospital.dto.HospitalDTO;
import com.hospyboard.api.app.hospital.dto.RoomDTO;
import com.hospyboard.api.app.hospital.dto.ServiceDTO;
import com.hospyboard.api.app.hospital.dto.requests.LinkRoomAndPatientDTO;
import com.hospyboard.api.app.hospital.services.HospitalsService;
import com.hospyboard.api.app.hospital.services.RoomsService;
import com.hospyboard.api.app.hospital.services.ServicesService;
import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.repository.UserRepository;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomsServiceTest {

    private final RoomsService roomsService;
    private final ServicesService servicesService;
    private final HospitalsService hospitalsService;
    private final UserRepository userRepository;

    @Autowired
    public RoomsServiceTest(RoomsService roomsService,
                            ServicesService servicesService,
                            HospitalsService hospitalsService,
                            UserRepository userRepository) {
        this.roomsService = roomsService;
        this.servicesService = servicesService;
        this.hospitalsService = hospitalsService;
        this.userRepository = userRepository;
    }

    @Test
    public void testRoomCreation() {
        final RoomDTO roomDTO = new RoomDTO();
        roomDTO.setService(createService());
        roomDTO.setName("Room test");

        final RoomDTO res = roomsService.create(roomDTO);
        assertEquals(roomDTO.getService().getId(), res.getService().getId());
        assertNotNull(res.getId());
        assertEquals(roomDTO.getName(), res.getName());
    }

    @Test
    public void testRoomCreationServiceNotFound() {
        try {
            final ServiceDTO serviceDTO = new ServiceDTO();
            serviceDTO.setId(UUID.randomUUID());

            final RoomDTO roomDTO = new RoomDTO();
            roomDTO.setService(serviceDTO);
            roomDTO.setName("oui");

            roomsService.create(roomDTO);
            fail("Service found");
        } catch (ApiNotFoundException ignored) {
        } catch (BadRequestException e) {
            fail(e);
        }
    }

    @Test
    public void testRoomCreationNoIdService() {
        try {
            final RoomDTO roomDTO = new RoomDTO();
            roomDTO.setName("oui");

            roomsService.create(roomDTO);
            fail("Service found");
        } catch (ApiBadRequestException ignored) {
        } catch (ApiNotFoundException e) {
            fail(e);
        }
    }

    @Test
    public void testPatchRoom() {
        final RoomDTO roomDTO = new RoomDTO();
        roomDTO.setService(createService());
        roomDTO.setName("Room test");

        final RoomDTO res = roomsService.create(roomDTO);
        res.setName("Room test patched");

        final RoomDTO resPatch = roomsService.update(res);
        assertEquals(res.getService().getId(), resPatch.getService().getId());
        assertEquals(res.getId(), resPatch.getId());
        assertEquals(res.getName(), resPatch.getName());
    }

    @Test
    public void testPatchMultiple() {
        final String newName = "LA ROOM DE FOU";
        final List<RoomDTO> roomDTOS = new ArrayList<>();

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setService(createService());
        roomDTO.setName("Room test");

        RoomDTO res = roomsService.create(roomDTO);
        roomDTOS.add(res);

        roomDTO = new RoomDTO();
        roomDTO.setService(createService());
        roomDTO.setName("Room test");

        res = roomsService.create(roomDTO);
        roomDTOS.add(res);

        for (final RoomDTO get : roomDTOS) {
            get.setName(newName);
        }

        final List<RoomDTO> resList = roomsService.update(roomDTOS);
        for (final RoomDTO get2 : resList) {
            assertEquals(get2.getName(), newName);
        }
    }

    @Test
    public void testPatchNoId() {
        final RoomDTO roomDTO = new RoomDTO();
        roomDTO.setService(createService());
        roomDTO.setName("Room test");

        final RoomDTO res = roomsService.create(roomDTO);

        try {
            res.setId(null);
            roomsService.update(res);
            fail("id found");
        } catch (ApiBadRequestException ignored) {
        } catch (ApiNotFoundException e) {
            fail(e);
        }
    }

    @Test
    public void testPatchIdNotFound() {
        final RoomDTO roomDTO = new RoomDTO();
        roomDTO.setService(createService());
        roomDTO.setName("Room test");

        final RoomDTO res = roomsService.create(roomDTO);

        try {
            res.setId(UUID.randomUUID());
            roomsService.update(res);
            fail("id found");
        } catch (ApiNotFoundException ignored) {
        } catch (ApiBadRequestException e) {
            fail(e);
        }
    }

    @Test
    public void testGetAll() {
        final RoomDTO roomDTO = new RoomDTO();
        roomDTO.setService(createService());
        roomDTO.setName("Room test");

        final RoomDTO res = roomsService.create(roomDTO);

        for (final RoomDTO search : roomsService.getAll("0", "100", null, null).getContent()) {
            if (search.getId().equals(res.getId())) {
                return;
            }
        }
        fail("id not found");
    }

    @Test
    public void testFindById() {
        final RoomDTO roomDTO = new RoomDTO();
        roomDTO.setService(createService());
        roomDTO.setName("Room test");

        final RoomDTO res = roomsService.create(roomDTO);
        final RoomDTO search = roomsService.findById(res.getId().toString());
        assertEquals(res.getId(), search.getId());
    }

    @Test
    public void testFindByIdFail() {
        assertNull(roomsService.findById(UUID.randomUUID().toString()));
    }

    @Test
    public void testGetRoomWithUsers() {
        final RoomDTO roomDTO = new RoomDTO();
        roomDTO.setService(createService());
        roomDTO.setName("JE SUIS UNE RECHERCHE");

        final RoomDTO res = roomsService.create(roomDTO);

        User user = new User();
        user.setPassword("123111");
        user.setEmail("1oui@test.fr");
        user.setFirstName("oui1");
        user.setLastName("sq1");
        user.setRole("ADMIN");
        user.setRoomUuid(res.getId());
        user.setUsername("ddddmflfj");
        user = this.userRepository.save(user);

        final RoomDTO search = roomsService.findById(res.getId().toString());
        if (search == null) {
            fail("room not found");
        }

        for (final UserDTO userDTO : search.getPatients()) {
            if (userDTO.getId().equals(user.getUuid())) {
                return;
            }
        }
        fail("user not found");
    }

    @Test
    public void testAddUserToRoom() {
        final RoomDTO roomDTO = new RoomDTO();
        roomDTO.setService(createService());
        roomDTO.setName("JE SUIS UNE RECHERCHE");

        final RoomDTO room = roomsService.create(roomDTO);

        User user = new User();
        user.setPassword("123");
        user.setEmail("oui@test.fr");
        user.setFirstName("oui");
        user.setLastName("sq");
        user.setRole("ADMIN");
        user.setUsername("dmflfj");
        user = this.userRepository.save(user);

        final LinkRoomAndPatientDTO linkRoomAndPatientDTO = new LinkRoomAndPatientDTO(room.getId(), user.getUuid());
        final RoomDTO result = roomsService.addPatient(linkRoomAndPatientDTO);
        assertEquals(1, result.getPatients().size());
        assertEquals(user.getUuid(), result.getPatients().get(0).getId());
    }

    public ServiceDTO createService() {
        HospitalDTO hospital = new HospitalDTO();
        hospital.setAddress("Test adress");
        hospital.setName("Hospital Test name");
        hospital = hospitalsService.create(hospital);

        ServiceDTO service = new ServiceDTO();
        service.setHospital(hospital);
        service.setName("Service test name");
        service = servicesService.create(service);

        return service;
    }

}
