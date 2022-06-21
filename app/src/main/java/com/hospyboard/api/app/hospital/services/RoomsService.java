package com.hospyboard.api.app.hospital.services;

import com.hospyboard.api.app.hospital.dto.RoomDTO;
import com.hospyboard.api.app.hospital.entity.Room;
import com.hospyboard.api.app.hospital.mappers.RoomMapper;
import com.hospyboard.api.app.hospital.repositories.RoomRepository;
import fr.funixgaming.api.core.crud.services.ApiService;
import org.springframework.stereotype.Service;

@Service
public class RoomsService extends ApiService<RoomDTO, Room, RoomMapper, RoomRepository> {

    public RoomsService(RoomRepository repository,
                        RoomMapper mapper) {
        super(repository, mapper);
    }

}
