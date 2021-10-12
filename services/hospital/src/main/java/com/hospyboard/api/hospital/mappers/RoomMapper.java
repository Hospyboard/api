package com.hospyboard.api.hospital.mappers;

import com.hospyboard.api.hospital.dto.RoomDTO;
import com.hospyboard.api.hospital.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    Room toEntity(RoomDTO dto);

    RoomDTO toDto(Room room);
}
