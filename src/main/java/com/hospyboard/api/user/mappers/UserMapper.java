package com.hospyboard.api.user.mappers;

import com.hospyboard.api.user.dto.UserCreationDTO;
import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.entity.User;
import com.hospyboard.api.user.enums.UserRole;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserDTO dto);

    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "passwordConfirmation", ignore = true)
    UserDTO toDto(User entity);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = UserRole.PATIENT)
    User fromUserCreationToEntity(UserCreationDTO userCreationDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "authorities", ignore = true)
    void patchUser(User newUser, @MappingTarget User oldUser);
}
