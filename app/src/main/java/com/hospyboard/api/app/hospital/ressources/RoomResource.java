package com.hospyboard.api.app.hospital.ressources;

import com.hospyboard.api.app.hospital.dto.RoomDTO;
import com.hospyboard.api.app.hospital.services.RoomsService;
import fr.funixgaming.api.core.crud.resources.ApiResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("room")
public class RoomResource extends ApiResource<RoomDTO, RoomsService> {
    public RoomResource(RoomsService roomsService) {
        super(roomsService);
    }
}
