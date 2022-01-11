package com.hospyboard.api.user.mappers;

import com.hospyboard.api.user.dto.UserCreationDTO;
import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserDTO dto);

    User fromUserCreationToEntity(UserCreationDTO userCreationDTO);

    @Mapping(target = "id", source = "uuid")
    UserDTO toDto(User entity);
}
