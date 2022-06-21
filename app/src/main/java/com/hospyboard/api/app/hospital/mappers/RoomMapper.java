package com.hospyboard.api.app.hospital.mappers;

import com.hospyboard.api.app.hospital.dto.RoomDTO;
import com.hospyboard.api.app.hospital.entity.Room;
import com.hospyboard.api.app.user.mappers.UserMapper;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface RoomMapper extends ApiMapper<Room, RoomDTO> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "service.uuid", source = "service.id")
    @Mapping(target = "service.id", ignore = true)
    @Mapping(target = "service.hospital.id", ignore = true)
    Room toEntity(RoomDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "service.id", source = "service.uuid")
    @Mapping(target = "service.hospital.id", source = "service.hospital.uuid")
    RoomDTO toDto(Room entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(Room request, @MappingTarget Room toPatch);

}
