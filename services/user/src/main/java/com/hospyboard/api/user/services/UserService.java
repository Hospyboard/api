package com.hospyboard.api.user.services;

import com.hospyboard.api.user.dto.UserDTO;
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
}
