package com.hospyboard.api.app.user.services;

import com.hospyboard.api.app.core.exceptions.BadRequestException;
import com.hospyboard.api.app.core.exceptions.ForbiddenException;
import com.hospyboard.api.app.user.dto.UserCreationDTO;
import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.enums.UserRole;
import com.hospyboard.api.app.user.exception.RegisterHospyboardException;
import com.hospyboard.api.app.user.exception.UserUpdateException;
import com.hospyboard.api.app.user.mappers.UserMapper;
import com.hospyboard.api.app.user.repository.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final CurrentUser currentUser;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       CurrentUser currentUser,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.currentUser = currentUser;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO getActualUser() {
        final UserDTO userDTO = this.currentUser.getCurrentUser();

        if (userDTO == null) {
            throw new BadRequestException("Impossible de récupérer l'utilisateur connecté. Veuillez vérifier que vous êtes bien connecté.");
        } else {
            return userDTO;
        }
    }

    @Transactional
    public UserDTO updateUser(final UserDTO request) {
        final UserDTO currentUser = this.currentUser.getCurrentUser();

        if (currentUser != null && !currentUser.getRole().equals(UserRole.ADMIN)) {
            throw new ForbiddenException("Impossible de mettre à jour les comptes utilisateurs. Vous devez être admin.");
        }
        if (Strings.isEmpty(request.getId())) {
            throw new UserUpdateException("L'utilisateur que vous voulez mettre à jour ne possède pas d'id.");
        }

        if (!Strings.isEmpty(request.getPassword()) && !Strings.isEmpty(request.getPasswordConfirmation())) {
            if (request.getPassword().equals(request.getPasswordConfirmation())) {
                request.setPassword(passwordEncoder.encode(request.getPassword()));
                //TODO invalid JWT token of user password edited
            } else {
                throw new UserUpdateException("Les mots de passe ne correspondent pas.");
            }
        }

        final Optional<User> user = this.userRepository.findByUuid(request.getId());
        if (user.isPresent()) {
            final User userFind = user.get();
            final User userPatch = this.userMapper.toEntity(request);

            userPatch.setId(null);
            this.userMapper.patchUser(userPatch, userFind);
            return this.userMapper.toDto(this.userRepository.save(userFind));
        } else {
            throw new UserUpdateException("L'id que vous avez fourni n'existe pas en base de donnée.");
        }
    }

    @Transactional
    public UserDTO createNewUser(final UserCreationDTO userCreationDTO) {
        if (userCreationDTO.getPassword().equals(userCreationDTO.getPasswordConfirmation())) {
            final Optional<User> optUser = this.userRepository.findByUsername(userCreationDTO.getUsername());

            if (optUser.isPresent()) {
                throw new RegisterHospyboardException("Ce nom d'utilisateur existe déjà.");
            }

            final User user = this.userMapper.fromUserCreationToEntity(userCreationDTO);

            user.setPassword(passwordEncoder.encode(user.getPassword()));
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