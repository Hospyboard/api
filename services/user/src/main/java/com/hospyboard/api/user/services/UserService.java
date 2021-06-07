package com.hospyboard.api.user.services;

import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.entity.UserEntity;
import com.hospyboard.api.user.mappers.UserMapper;
import com.hospyboard.api.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public UserDTO login(final UserDTO response) {
        UserEntity userEntity = userRepository.getAuthEntityByUuid(response.getEmail());

        if (userEntity == null) {
            userEntity = new UserEntity()
                    .setEmail(response.getEmail())
                    .setPassword(response.getPassword());
        }
        return userMapper.toDto(userRepository.save(userEntity));
    }

}
