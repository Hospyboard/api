package com.hospyboard.api.app.user.services;

import com.hospyboard.api.app.alert.repository.AlertRepository;
import com.hospyboard.api.app.core.exceptions.ForbiddenException;
import com.hospyboard.api.app.user.config.JwtTokenUtil;
import com.hospyboard.api.app.user.dto.UserCreationDTO;
import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.dto.UserResetPasswordDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.enums.UserRole;
import com.hospyboard.api.app.user.exception.RegisterHospyboardException;
import com.hospyboard.api.app.user.exception.UserUpdateException;
import com.hospyboard.api.app.user.mappers.UserMapper;
import com.hospyboard.api.app.user.repository.UserRepository;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService extends ApiService<UserDTO, User, UserMapper, UserRepository> implements UserDetailsService {

    private final CurrentUser currentUser;
    private final UserMapper userMapper;

    private final JwtTokenUtil tokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final AlertRepository alertRepository;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       JwtTokenUtil tokenUtil,
                       CurrentUser currentUser,
                       PasswordEncoder passwordEncoder,
                       AlertRepository alertRepository) {
        super(userRepository, userMapper);
        this.userMapper = userMapper;
        this.currentUser = currentUser;
        this.tokenUtil = tokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.alertRepository = alertRepository;
    }

    @Transactional
    public UserDTO updateUser(final UserDTO request) {
        final UserDTO currentUser = this.currentUser.getCurrentUser();

        if (!currentUser.getRole().equals(UserRole.ADMIN)) {
            throw new ForbiddenException("Impossible de mettre à jour les comptes utilisateurs. Vous devez être admin.");
        }
        if (request.getId() == null) {
            throw new UserUpdateException("L'utilisateur que vous voulez mettre à jour ne possède pas d'id.");
        }

        if (!Strings.isEmpty(request.getPassword()) && !Strings.isEmpty(request.getPasswordConfirmation())) {
            if (request.getPassword().equals(request.getPasswordConfirmation())) {
                request.setPassword(passwordEncoder.encode(request.getPassword()));
                tokenUtil.invalidateTokens(request.getId());
            } else {
                throw new UserUpdateException("Les mots de passe ne correspondent pas.");
            }
        }

        final UserDTO res = super.update(request);

        if (res == null) {
            throw new ApiNotFoundException("L'utilisateur n'existe pas");
        } else {
            return res;
        }
    }

    @Transactional
    public UserDTO register(final UserCreationDTO userCreationDTO) {
        if (userCreationDTO.getPassword().equals(userCreationDTO.getPasswordConfirmation())) {
            final Optional<User> optUser = super.getRepository().findByUsername(userCreationDTO.getUsername());

            if (optUser.isPresent()) {
                throw new RegisterHospyboardException("Ce nom d'utilisateur existe déjà.");
            }

            final User user = this.userMapper.fromUserCreationToEntity(userCreationDTO);
            final Optional<User> admin = this.getRepository().findByUsername("admin");
            final Optional<User> hospital = this.getRepository().findByUsername("hospital");

            if (userCreationDTO.getUsername().equalsIgnoreCase("admin") && admin.isEmpty()) {
                user.setRole(UserRole.ADMIN);
            } else if (userCreationDTO.getUsername().equalsIgnoreCase("hospital") && hospital.isEmpty()) {
                user.setRole(UserRole.HOSPYTAL_WORKER);
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return this.userMapper.toDto(super.getRepository().save(user));
        } else {
            throw new RegisterHospyboardException("Vos mots de passe ne correspondent pas.");
        }
    }

    @Transactional
    public void changePassword(final UserResetPasswordDTO request) throws ApiBadRequestException {
        final UserDTO userDTO = this.currentUser.getCurrentUser();
        final Optional<User> search = this.getRepository().findByUuid(userDTO.getId().toString());

        if (search.isPresent()) {
            final User user = search.get();

            if (request.getNewPassword().equals(request.getNewPasswordConfirmation())) {
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                tokenUtil.invalidateTokens(userDTO.getId());
                super.getRepository().save(user);
            } else {
                throw new ApiBadRequestException("Vos nouveaux mots de passe ne correspondent pas.");
            }

        }
    }

    @Override
    @Transactional
    public void delete(String id) {
        final Optional<User> search = super.getRepository().findByUuid(id);

        if (search.isPresent()) {
            final User user = search.get();

            this.alertRepository.deleteAllByPatient(user);
            this.alertRepository.deleteAllByStaff(user);
        } else {
            throw new ApiNotFoundException("L'utilisateur que vous souhaitez supprimer n'existe pas.");
        }

        this.tokenUtil.invalidateTokens(UUID.fromString(id));
        super.delete(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return super.getRepository()
                .findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("Utilisateur non trouvé: %s", username))
                );
    }
}
