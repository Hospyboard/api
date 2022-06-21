package com.hospyboard.api.app.hospital.mappers;

import com.hospyboard.api.app.hospital.dto.ServiceDTO;
import com.hospyboard.api.app.hospital.entity.Service;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = RoomMapper.class)
public interface ServiceMapper extends ApiMapper<Service, ServiceDTO> {
    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    Service toEntity(ServiceDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    ServiceDTO toDto(Service entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(Service request, Service toPatch);
}
