package com.hospyboard.api.user.services;

import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.dto.UserLoginDTO;
import com.hospyboard.api.user.dto.UserRegisterDTO;
import com.hospyboard.api.user.entity.UserEntity;
import com.hospyboard.api.user.mappers.UserMapper;
import com.hospyboard.api.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO register(final UserRegisterDTO request) {
        final UserEntity userEntity = new UserEntity();

        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(request.getPassword());
        userEntity.setToken(generateFakeToken());

        return userMapper.toDto(userRepository.save(userEntity));
    }

    public UserDTO login(final UserLoginDTO request) throws Exception {
        final UserEntity userEntity = userRepository.getAuthEntityByEmail(request.getEmail());

        if (userEntity == null) {
            //TODO custom throw avec code d'erreur 404
            throw new Exception("User not exists");
        }
        return new UserDTO()
                .setEmail(request.getEmail())
                .setId(userEntity.getUuid())
                .setToken(userEntity.getToken());
    }

    //TODO Remove ça quand on aura imeplémenté le JWT
    private String generateFakeToken() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 20;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
