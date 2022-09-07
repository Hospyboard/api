package com.hospyboard.api.app.hospital.mappers;

import com.hospyboard.api.app.hospital.dto.ServiceDTO;
import com.hospyboard.api.app.hospital.entity.ServiceEntity;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {RoomMapper.class})
public interface ServiceMapper extends ApiMapper<ServiceEntity, ServiceDTO> {
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "hospital.uuid", source = "hospital.id")
    @Mapping(target = "hospital.id", ignore = true)
    ServiceEntity toEntity(ServiceDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "hospital.id", source = "hospital.uuid")
    @Mapping(target = "hospital.services", ignore = true)
    ServiceDTO toDto(ServiceEntity entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "hospital", ignore = true)
    void patch(ServiceEntity request, @MappingTarget ServiceEntity toPatch);
}
