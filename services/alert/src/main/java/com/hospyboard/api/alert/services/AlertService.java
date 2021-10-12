package com.hospyboard.api.alert.services;

import com.hospyboard.api.alert.dto.AlertDTO;
import com.hospyboard.api.alert.entity.AlertEntity;
import com.hospyboard.api.alert.mappers.AlertMapper;
import com.hospyboard.api.alert.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;

    public AlertService(AlertRepository alertRepository,
                       AlertMapper alertMapper) {
        this.alertRepository = alertRepository;
        this.alertMapper = alertMapper;
    }

    public AlertDTO createOrUpdate(final AlertDTO request) {
        final AlertEntity alertEntity = new AlertEntity();

        if (request.getAlertUuid() != null) {
            alertEntity.setAlertUuid(request.getAlertUuid());
        }
        alertEntity.setType(request.getType());
        alertEntity.setPatientUuid(request.getPatientUuid());
        alertEntity.setImportance(request.getImportance());
        alertEntity.setStatus(request.getStatus());
        alertEntity.setStaffUuid(request.getStaffUuid());

        return alertMapper.toDto(alertRepository.save(alertEntity));
    }

    public AlertDTO[] get(final String alertUuid) throws Exception {
        AlertEntity[] ret;

        if (alertUuid != null) {
            ret = new AlertEntity[]{this.alertRepository.getAlertEntityByUuid(alertUuid)};
        } else {
            ret = this.alertRepository.getAlertEntity();
        }
        ArrayList<AlertDTO> res = new ArrayList<>();
        for (AlertEntity alertEntity : ret) {
            res.add(this.alertMapper.toDto(alertEntity));
        }
        return (AlertDTO[]) res.toArray();
    }

}
