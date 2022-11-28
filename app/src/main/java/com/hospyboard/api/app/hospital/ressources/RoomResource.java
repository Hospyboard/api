package com.hospyboard.api.app.hospital.ressources;

import com.hospyboard.api.app.hospital.dto.RoomDTO;
import com.hospyboard.api.app.hospital.dto.requests.LinkRoomAndPatientDTO;
import com.hospyboard.api.app.hospital.services.RoomsService;
import fr.funixgaming.api.core.crud.resources.ApiResource;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("room")
public class RoomResource extends ApiResource<RoomDTO, RoomsService> {
    public RoomResource(RoomsService roomsService) {
        super(roomsService);
    }

    @PostMapping("addPatient")
    public RoomDTO addPatient(@RequestBody @Valid final LinkRoomAndPatientDTO linkRoomAndPatientDTO) {
        return getService().setPatient(linkRoomAndPatientDTO);
    }

    @DeleteMapping("removePatient")
    public void removePatient(@RequestParam String patientUuid) {
        final LinkRoomAndPatientDTO request = new LinkRoomAndPatientDTO();
        request.setPatientId(UUID.fromString(patientUuid));

        getService().setPatient(request);
    }
}
