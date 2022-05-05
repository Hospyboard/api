package com.hospyboard.api.app.alert.services;

import com.hospyboard.api.app.alert.dto.AlertDTO;
import com.hospyboard.api.app.alert.entity.AlertEntity;
import com.hospyboard.api.app.alert.enums.AlertStatus;
import com.hospyboard.api.app.alert.mappers.AlertMapper;
import com.hospyboard.api.app.alert.repository.AlertRepository;
import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.enums.UserRole;
import com.hospyboard.api.app.user.services.CurrentUser;
import com.hospyboard.api.app.user.services.UserService;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AlertService extends ApiService<AlertDTO, AlertEntity, AlertMapper, AlertRepository> {

    private final UserService userService;
    private final CurrentUser currentUser;

    public AlertService(AlertRepository alertRepository,
                        AlertMapper alertMapper,
                        CurrentUser currentUser,
                        UserService userService) {
        super(alertRepository, alertMapper);
        this.currentUser = currentUser;
        this.userService = userService;
    }

    @Override
    public AlertDTO create(AlertDTO request) throws ApiException {
        final Optional<User> patientSearch = userService.getRepository().findByUuid(currentUser.getCurrentUser().getId().toString());

        if (patientSearch.isPresent()) {
            final User patient = patientSearch.get();
            AlertEntity alert = super.getMapper().toEntity(request);

            alert.setPatient(patient);
            alert.setStaff(null);
            alert.setStatus(AlertStatus.PENDING);
            alert = super.getRepository().save(alert);
            return super.getMapper().toDto(alert);
        } else {
            throw new ApiBadRequestException("Ce patient n'existe pas.");
        }
    }

    @Override
    public List<AlertDTO> update(List<AlertDTO> request) throws ApiException {
        final List<AlertDTO> data = new ArrayList<>();

        for (final AlertDTO alertDTO : request) {
            data.add(editDto(alertDTO));
        }
        return data;
    }

    @Override
    public AlertDTO update(AlertDTO request) throws ApiException {
        return editDto(request);
    }

    private AlertDTO editDto(final AlertDTO request) throws ApiException {
        final UserDTO userDto = currentUser.getCurrentUser();
        final Optional<AlertEntity> search = super.getRepository().findByUuid(request.getId().toString());
        final Optional<User> searchUser = userService.getRepository().findByUuid(userDto.getId().toString());
        final AlertEntity alert;
        final User user;

        if (search.isPresent()) {
            alert = search.get();
        } else {
            throw new ApiNotFoundException("Cette alerte n'existe pas.");
        }

        if (searchUser.isPresent()) {
            user = searchUser.get();
        } else {
            throw new ApiNotFoundException("L'utilisateur connecté est introuvable.");
        }

        AlertEntity requestEnt = super.getMapper().toEntity(request);
        requestEnt.setId(null);
        requestEnt.setUpdatedAt(Date.from(Instant.now()));
        super.getMapper().patch(requestEnt, alert);

        if (user.getRole().equals(UserRole.PATIENT)) {
            if (request.getId() != null) {
                final User patient = alert.getPatient();

                if (patient == null || !patient.getId().equals(user.getId())) {
                    throw new ApiForbiddenException("Vous ne pouvez pas modifier une alerte que vous n'avez pas crée.");
                }
            }

            alert.setPatient(user);
        } else {
            alert.setStaff(user);
        }

        return super.getMapper().toDto(super.getRepository().save(alert));
    }

}
