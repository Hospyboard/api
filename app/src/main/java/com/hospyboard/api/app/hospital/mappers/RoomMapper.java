package com.hospyboard.api.app.hospital.mappers;

import com.hospyboard.api.app.hospital.dto.RoomDTO;
import com.hospyboard.api.app.hospital.entity.Room;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RoomMapper extends ApiMapper<Room, RoomDTO> {

    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    Room toEntity(RoomDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    RoomDTO toDto(Room entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(Room request, Room toPatch);

}
