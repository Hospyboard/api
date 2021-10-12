package com.hospyboard.api.hospital.services;

import com.hospyboard.api.hospital.dto.RoomDTO;
import com.hospyboard.api.hospital.entity.Room;
import com.hospyboard.api.hospital.mappers.RoomMapper;
import com.hospyboard.api.hospital.respository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoomService {

    private final RoomMapper roomMapper;
    private final RoomRepository roomRepository;

    public RoomService(RoomMapper roomMapper,
                       RoomRepository roomRepository) {
        this.roomMapper = roomMapper;
        this.roomRepository = roomRepository;
    }

    public Set<RoomDTO> getAllRooms() {
        final Iterable<Room> rooms = this.roomRepository.findAll();
        final Set<RoomDTO> toReturn = new HashSet<>();

        for (final Room room : rooms) {
            toReturn.add(this.roomMapper.toDto(room));
        }
        return toReturn;
    }

    public RoomDTO createNewRoom(final RoomDTO data) {
        Room toCreate = this.roomMapper.toEntity(data);
        toCreate = this.roomRepository.save(toCreate);

        return this.roomMapper.toDto(toCreate);
    }

}
