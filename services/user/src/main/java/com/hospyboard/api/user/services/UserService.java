package com.hospyboard.api.user.services;

import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.dto.UserLoginDTO;
import com.hospyboard.api.user.entity.UserEntity;
import com.hospyboard.api.user.mappers.UserMapper;
import com.hospyboard.api.user.repository.UserRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final CurrentUser currentUser;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       CurrentUser currentUser) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.currentUser = currentUser;
    }

    @Nullable
    public UserDTO getActualUser() {
        return this.currentUser.getCurrentUser();
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
