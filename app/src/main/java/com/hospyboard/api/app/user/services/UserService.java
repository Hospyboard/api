package com.hospyboard.api.app.user.services;

import com.hospyboard.api.app.alert.repository.AlertRepository;
import com.hospyboard.api.app.core.configs.HospyboardConfig;
import com.hospyboard.api.app.file_storage.services.FileStorageService;
import com.hospyboard.api.app.hospital.entity.Room;
import com.hospyboard.api.app.hospital.services.RoomsService;
import com.hospyboard.api.app.mails.dtos.HospyboardMailDTO;
import com.hospyboard.api.app.mails.services.HospyboardMailService;
import com.hospyboard.api.app.user.config.JwtTokenUtil;
import com.hospyboard.api.app.user.dto.UserCreationDTO;
import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.dto.UserForgotPasswordDTO;
import com.hospyboard.api.app.user.dto.UserResetPasswordDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.entity.UserPasswordReset;
import com.hospyboard.api.app.user.enums.UserRole;
import com.hospyboard.api.app.user.exception.RegisterHospyboardException;
import com.hospyboard.api.app.user.exception.UserUpdateException;
import com.hospyboard.api.app.user.mappers.UserMapper;
import com.hospyboard.api.app.user.repository.UserForgotPasswordResetRepository;
import com.hospyboard.api.app.user.repository.UserRepository;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import fr.funixgaming.api.core.utils.string.PasswordGenerator;
import lombok.NonNull;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UserService extends ApiService<UserDTO, User, UserMapper, UserRepository> implements UserDetailsService {

    private final CurrentUser currentUser;
    private final UserMapper userMapper;

    private final JwtTokenUtil tokenUtil;
    private final AlertRepository alertRepository;
    private final FileStorageService fileStorageService;
    private final RoomsService roomsService;
    private final UserForgotPasswordResetRepository passwordResetRepository;
    private final HospyboardMailService mailService;
    private final HospyboardConfig hospyboardConfig;
    private final UserUtils userUtils;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       JwtTokenUtil tokenUtil,
                       CurrentUser currentUser,
                       AlertRepository alertRepository,
                       FileStorageService fileStorageService,
                       RoomsService roomsService,
                       HospyboardConfig hospyboardConfig,
                       UserUtils userUtils,
                       UserForgotPasswordResetRepository passwordResetRepository,
                       HospyboardMailService mailService) {
        super(userRepository, userMapper);
        this.userMapper = userMapper;
        this.currentUser = currentUser;
        this.tokenUtil = tokenUtil;
        this.roomsService = roomsService;
        this.alertRepository = alertRepository;
        this.fileStorageService = fileStorageService;
        this.passwordResetRepository = passwordResetRepository;
        this.hospyboardConfig = hospyboardConfig;
        this.mailService = mailService;
        this.userUtils = userUtils;
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
                user.setRole(UserRole.HOSPITAL_WORKER);
            }

            final UserDTO tmpDto = new UserDTO();
            tmpDto.setPassword(userCreationDTO.getPassword());
            userUtils.checkUserPassword(tmpDto);

            user.setPassword(user.getPassword());
            final UserDTO userDTO = this.userMapper.toDto(super.getRepository().save(user));

            if (userDTO.getLastLoginAt() != null) {
                userDTO.setLastLoginAt(Date.from(userDTO.getLastLoginAt().toInstant().plus(1, ChronoUnit.HOURS)));
            }
            if (userDTO.getUpdatedAt() != null) {
                userDTO.setUpdatedAt(Date.from(userDTO.getUpdatedAt().toInstant().plus(1, ChronoUnit.HOURS)));
            }
            return userDTO;
        } else {
            throw new RegisterHospyboardException("Vos mots de passe ne correspondent pas.");
        }
    }

    @Transactional
    public void changePassword(final UserResetPasswordDTO request) throws ApiBadRequestException {
        final UserDTO tmpDto = new UserDTO();
        tmpDto.setPassword(request.getNewPassword());
        userUtils.checkUserPassword(tmpDto);

        final UserDTO userDTO = this.currentUser.getCurrentUser();
        final Optional<User> search = this.getRepository().findByUuid(userDTO.getId().toString());

        if (search.isPresent()) {
            final User user = search.get();

            if (request.getNewPassword().equals(request.getNewPasswordConfirmation())) {
                user.setPassword(request.getNewPassword());
                tokenUtil.invalidateTokens(userDTO.getId());
                super.getRepository().save(user);
            } else {
                throw new ApiBadRequestException("Vos nouveaux mots de passe ne correspondent pas.");
            }

        }
    }

    public void resetPassword(final UserForgotPasswordDTO request) {
        if (request.isPasswordSet()) {
            if (request.getCode() == null) {
                throw new ApiBadRequestException("Le code entré est invalide.");
            } else {
                passwordReset(request);
            }
        } else {
            sendMailPasswordReset(request);
        }
    }

    private void passwordReset(final UserForgotPasswordDTO request) {
        final Optional<UserPasswordReset> search = this.passwordResetRepository.findUserPasswordResetByCode(request.getCode());

        if (search.isPresent()) {
            final UserPasswordReset passwordReset = search.get();

            if (!passwordReset.isValid()) {
                throw new ApiBadRequestException("Le code entré est invalide.");
            } else {
                final User user = passwordReset.getUser();

                if (request.getPassword().equals(request.getPasswordConfirmation())) {
                    final UserDTO tmpDto = new UserDTO();
                    tmpDto.setPassword(request.getPassword());
                    userUtils.checkUserPassword(tmpDto);

                    user.setPassword(request.getPassword());
                    super.getRepository().save(user);
                    this.passwordResetRepository.delete(passwordReset);
                } else {
                    throw new ApiBadRequestException("Les mots de passe ne correspondent pas.");
                }
            }
        } else {
            throw new ApiBadRequestException("Le code entré est invalide.");
        }
    }

    private void sendMailPasswordReset(final UserForgotPasswordDTO request) {
        final Iterable<User> users = getRepository().findAllByEmail(request.getEmail());

        for (final User user : users) {
            UserPasswordReset userPasswordReset = new UserPasswordReset();
            userPasswordReset.setExpirationDate(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)));
            userPasswordReset.setUser(user);
            userPasswordReset.setCode(generateRandomStringCode());

            userPasswordReset = this.passwordResetRepository.save(userPasswordReset);

            final HospyboardMailDTO mailDTO = new HospyboardMailDTO();
            mailDTO.setSubject("Changement de mot de passe Hospyboard");
            mailDTO.setTo(userPasswordReset.getUser().getEmail());
            mailDTO.setText(String.format("Lien de réinitialisation de mot de passe : %s/resetpassword/%s",
                    hospyboardConfig.getUrlDashboard(),
                    userPasswordReset.getCode())
            );
            mailService.getMailQueue().add(mailDTO);
        }
    }

    private String generateRandomStringCode() {
        final PasswordGenerator passwordGenerator = new PasswordGenerator();
        passwordGenerator.setAlphaDown(20);
        passwordGenerator.setAlphaUpper(20);
        passwordGenerator.setNumbersAmount(20);
        passwordGenerator.setSpecialCharsAmount(0);

        return passwordGenerator.generateRandomPassword();
    }

    @Override
    public void beforeDeletingEntity(@NonNull User entity) {
        this.alertRepository.deleteAllByPatient(entity);
        this.alertRepository.deleteAllByStaff(entity);
        this.fileStorageService.deleteAllFilesByUser(entity);
    }

    @Override
    public void beforeSavingEntity(@NonNull UserDTO request, @NonNull User entity) {
        userUtils.checkUserPassword(request);

        if (request.getRoom() != null && request.getRoom().getId() != null) {
            final Optional<Room> search = this.roomsService.getRepository().findByUuid(request.getRoom().getId().toString());

            if (search.isPresent()) {
                final Room room = search.get();
                entity.setRoomUuid(room.getUuid());
            } else {
                throw new ApiNotFoundException(String.format("La chambre id %s n'existe pas.", request.getRoom().getId()));
            }
        }

        if (!Strings.isEmpty(request.getPassword()) && !Strings.isEmpty(request.getPasswordConfirmation())) {
            if (request.getPassword().equals(request.getPasswordConfirmation())) {
                entity.setPassword(request.getPassword());

                if (request.getId() != null) {
                    tokenUtil.invalidateTokens(request.getId());
                }
            } else {
                throw new UserUpdateException("Les mots de passe ne correspondent pas.");
            }
        }
    }

    @Override
    public void beforeSendingDTO(@NonNull UserDTO dto, @Nullable User entity) {
        if (dto.getId() != null) {
            dto.setRoom(this.roomsService.findRoomByPatientId(dto.getId().toString()));
        }

        if (dto.getLastLoginAt() != null) {
            dto.setLastLoginAt(Date.from(dto.getLastLoginAt().toInstant().plus(1, ChronoUnit.HOURS)));
        }
        if (dto.getUpdatedAt() != null) {
            dto.setUpdatedAt(Date.from(dto.getUpdatedAt().toInstant().plus(1, ChronoUnit.HOURS)));
        }
        dto.setCreatedAt(Date.from(dto.getCreatedAt().toInstant().plus(1, ChronoUnit.HOURS)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return super.getRepository()
                .findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("Utilisateur non trouvé: %s", username))
                );
    }

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    public void cleanPasswordResetExpired() {
        final Iterable<UserPasswordReset> passwordResets = this.passwordResetRepository.findAll();
        final List<UserPasswordReset> toRemove = new ArrayList<>();

        for (final UserPasswordReset passwordReset : passwordResets) {
            if (!passwordReset.isValid()) {
                toRemove.add(passwordReset);
            }
        }
        this.passwordResetRepository.deleteAll(toRemove);
    }

}
