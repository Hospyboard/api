package com.hospyboard.api.app.alert.services;

import com.hospyboard.api.app.alert.dto.AlertDTO;
import com.hospyboard.api.app.alert.enums.AlertImportance;
import com.hospyboard.api.app.alert.enums.AlertStatus;
import com.hospyboard.api.app.alert.enums.AlertType;
import com.hospyboard.api.app.alert.entity.AlertEntity;
import com.hospyboard.api.app.alert.mappers.AlertMapper;
import com.hospyboard.api.app.alert.repository.AlertRepository;
import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.enums.UserRole;
import com.hospyboard.api.app.user.services.CurrentUser;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AlertService extends ApiService<AlertDTO, AlertEntity, AlertMapper, AlertRepository> {

    private final CurrentUser currentUser;

    public AlertService(AlertRepository alertRepository,
                        AlertMapper alertMapper,
                        CurrentUser currentUser) {
        super(alertRepository, alertMapper);
        this.currentUser = currentUser;
    }

    @Override
    public AlertDTO create(AlertDTO request) {
        editDto(request);
        return super.create(request);
    }

    @Override
    public List<AlertDTO> update(List<AlertDTO> request) {
        for (final AlertDTO alertDTO : request) {
            editDto(alertDTO);
        }

        return super.update(request);
    }

    @Override
    public AlertDTO update(AlertDTO request) {
        editDto(request);
        return super.update(request);
    }

    private void editDto(final AlertDTO request) {
        final UserDTO user = currentUser.getCurrentUser();

        if (user.getRole().equals(UserRole.PATIENT)) {
            if (request.getId() != null) {
                final Optional<AlertEntity> search = super.getRepository().findByUuid(request.getId().toString());

                if (search.isPresent()) {
                    final AlertEntity alert = search.get();

                    if (!alert.getPatient().getUuid().equals(user.getId())) {
                        throw new ApiForbiddenException("Vous ne pouvez pas modifier une alerte que vous n'avez pas crée.");
                    }
                }
            }

            request.setPatient(user);
        } else {
            request.setStaff(user);
        }
    }

}
