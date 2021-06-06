package com.hospyboard.api.auth.mappers;

import com.hospyboard.api.auth.dto.AuthDTO;
import com.hospyboard.api.auth.entity.AuthEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    //@Mapping(target = "uuid", source = "id")
    AuthEntity toEntity(AuthDTO dto);

    AuthDTO toDto(AuthEntity entity);
}
