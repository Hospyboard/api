package com.hospyboard.api.user.services;

import com.hospyboard.api.user.dto.UserCreationDTO;
import com.hospyboard.api.user.dto.UserDTO;
import com.hospyboard.api.user.entity.User;
import com.hospyboard.api.user.exception.LoginHospyboardException;
import com.hospyboard.api.user.exception.RegisterHospyboardException;
import com.hospyboard.api.user.mappers.UserMapper;
import com.hospyboard.api.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final CurrentUser currentUser;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       CurrentUser currentUser,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.currentUser = currentUser;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Nullable
    public UserDTO getActualUser() {
        return this.currentUser.getCurrentUser();
    }

    @Transactional
    public UserDTO createNewUser(final UserCreationDTO userCreationDTO) {
        if (userCreationDTO.getPassword().equals(userCreationDTO.getPasswordConfirmation())) {
            final User user = this.userMapper.fromUserCreationToEntity(userCreationDTO);

            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return this.userMapper.toDto(this.userRepository.save(user));
        } else {
            throw new RegisterHospyboardException("Vos mots de passe ne correspondent pas.");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("Utilisateur non trouvé: %s", username))
                );
    }
}
