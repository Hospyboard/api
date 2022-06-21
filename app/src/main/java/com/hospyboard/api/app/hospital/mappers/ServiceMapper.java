package com.hospyboard.api.app.hospital.mappers;

import com.hospyboard.api.app.hospital.dto.ServiceDTO;
import com.hospyboard.api.app.hospital.entity.Service;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {RoomMapper.class})
public interface ServiceMapper extends ApiMapper<Service, ServiceDTO> {
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "hospital.uuid", source = "hospital.id")
    @Mapping(target = "hospital.id", ignore = true)
    Service toEntity(ServiceDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "hospital.id", source = "hospital.uuid")
    ServiceDTO toDto(Service entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(Service request, @MappingTarget Service toPatch);
}
