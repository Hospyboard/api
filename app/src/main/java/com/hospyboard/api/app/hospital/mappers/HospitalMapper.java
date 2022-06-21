package com.hospyboard.api.app.hospital.mappers;

import com.hospyboard.api.app.hospital.dto.HospitalDTO;
import com.hospyboard.api.app.hospital.entity.Hospital;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = ServiceMapper.class)
public interface HospitalMapper extends ApiMapper<Hospital, HospitalDTO> {

    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    Hospital toEntity(HospitalDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    HospitalDTO toDto(Hospital entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(Hospital request, @MappingTarget Hospital toPatch);

}
