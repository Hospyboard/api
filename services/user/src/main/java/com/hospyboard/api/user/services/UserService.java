package com.hospyboard.api.user.services;

import com.auth0.jwt.JWT;
import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.dto.UserLoginDTO;
import com.hospyboard.api.user.dto.UserRegisterDTO;
import com.hospyboard.api.user.entity.UserEntity;
import com.hospyboard.api.user.exception.RegisterAuthException;
import com.hospyboard.api.user.mappers.UserMapper;
import com.hospyboard.api.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthService authService;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       AuthService authService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authService = authService;
    }

    public UserDTO register(final UserRegisterDTO request) throws RegisterAuthException {
        final UserEntity userEntity = authService.createAccount(request);

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
}
