package com.hospyboard.api.app.alert.services;

import com.hospyboard.api.app.alert.dto.AlertDTO;
import com.hospyboard.api.app.alert.entity.AlertEntity;
import com.hospyboard.api.app.alert.enums.AlertStatus;
import com.hospyboard.api.app.alert.mappers.AlertMapper;
import com.hospyboard.api.app.alert.repository.AlertRepository;
import com.hospyboard.api.app.hospital.services.RoomsService;
import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.enums.UserRole;
import com.hospyboard.api.app.user.repository.UserRepository;
import com.hospyboard.api.app.user.services.CurrentUser;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AlertService extends ApiService<AlertDTO, AlertEntity, AlertMapper, AlertRepository> {

    private final UserRepository userRepository;
    private final CurrentUser currentUser;
    private final RoomsService roomsService;

    public AlertService(AlertRepository alertRepository,
                        AlertMapper alertMapper,
                        CurrentUser currentUser,
                        RoomsService roomsService,
                        UserRepository userRepository) {
        super(alertRepository, alertMapper);
        this.currentUser = currentUser;
        this.userRepository = userRepository;
        this.roomsService = roomsService;
    }

    @Override
    public void beforeSavingEntity(@NotNull AlertDTO request, @NotNull AlertEntity alert) {
        final UserDTO userDto = currentUser.getCurrentUser();
        final Optional<User> searchUser = userRepository.findByUuid(userDto.getId().toString());
        final User user;

        if (searchUser.isPresent()) {
            user = searchUser.get();
        } else {
            throw new ApiNotFoundException("L'utilisateur connecté est introuvable.");
        }

        if (user.getRole().equals(UserRole.PATIENT)) {
            if (request.getId() != null) {
                final User patient = alert.getPatient();

                if (patient == null || !patient.getId().equals(user.getId())) {
                    throw new ApiForbiddenException("Vous ne pouvez pas modifier une alerte que vous n'avez pas crée.");
                }
            }
        } else {
            alert.setStaff(user);
        }

        if (request.getId() == null) {
            alert.setStatus(AlertStatus.PENDING);
            alert.setPatient(user);
        }
    }

    @Override
    public void beforeSendingDTO(@NonNull AlertDTO dto, @Nullable AlertEntity entity) {
        if (dto.getPatient() != null && dto.getPatient().getId() != null) {
            dto.getPatient().setRoom(roomsService.findRoomByPatientId(dto.getPatient().getId().toString()));
        }
    }

    public List<AlertDTO> fetchPatientAlerts() {
        final UserDTO actual = currentUser.getCurrentUser();
        final Optional<User> searchUser = userRepository.findByUuid(actual.getId().toString());

        if (searchUser.isPresent()) {
            final User user = searchUser.get();
            final List<AlertDTO> toSend = new ArrayList<>();

            for (final AlertEntity alert : getRepository().findAllByPatientOrderByCreatedAtDesc(user)) {
                final AlertDTO dto = getMapper().toDto(alert);

                beforeSendingDTO(dto, alert);
                toSend.add(dto);
            }
            return toSend;
        } else {
            throw new ApiNotFoundException("L'utilisateur connecté est introuvable.");
        }
    }
}
