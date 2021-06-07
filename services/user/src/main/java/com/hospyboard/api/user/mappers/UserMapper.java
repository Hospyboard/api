package com.hospyboard.api.user.mappers;

import com.hospyboard.api.user.dto.AuthDTO;
import com.hospyboard.api.user.entity.AuthEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    //@Mapping(target = "uuid", source = "id")
    AuthEntity toEntity(AuthDTO dto);

    AuthDTO toDto(AuthEntity entity);
}
