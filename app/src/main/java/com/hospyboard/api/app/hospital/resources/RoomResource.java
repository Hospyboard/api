package com.hospyboard.api.app.hospital.resources;

import com.hospyboard.api.app.hospital.dto.RoomDTO;
import com.hospyboard.api.app.hospital.services.RoomService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Set;

@RestController
@RequestMapping("hospital/room")
public class RoomResource {

    private final RoomService roomService;

    public RoomResource(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public Set<RoomDTO> getAllRooms() throws Exception {
        return roomService.getAllRooms();
    }

    @PostMapping
    @Transactional
    public RoomDTO createRoom(@RequestBody RoomDTO roomDTO) throws Exception {
        return this.roomService.createNewRoom(roomDTO);
    }

}
