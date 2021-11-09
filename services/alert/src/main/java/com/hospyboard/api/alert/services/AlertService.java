package com.hospyboard.api.alert.services;

import com.hospyboard.api.alert.dto.AlertDTO;
import com.hospyboard.api.alert.dto.AlertImportance;
import com.hospyboard.api.alert.dto.AlertStatus;
import com.hospyboard.api.alert.dto.AlertType;
import com.hospyboard.api.alert.entity.AlertEntity;
import com.hospyboard.api.alert.mappers.AlertMapper;
import com.hospyboard.api.alert.repository.AlertRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;

    public AlertService(AlertRepository alertRepository,
                       AlertMapper alertMapper) {
        this.alertRepository = alertRepository;
        this.alertMapper = alertMapper;
    }
    public void updateWS() throws Exception {
        String url = "http://localhost:9393/notify";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        AlertDTO obj = new AlertDTO();
        obj.setType(AlertType.WC);
        obj.setPatientUuid("eeee");
        obj.setImportance(AlertImportance.NOT_URGENT);
        obj.setStatus(AlertStatus.PENDING);
        obj.setStaffUuid("eeeeeeeeee");
        HttpEntity<AlertDTO> req = new HttpEntity(obj, headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(url, HttpMethod.GET,  req, String.class);
    }

    public AlertDTO create(final AlertDTO request) throws Exception {
        AlertEntity alertEntity = new AlertEntity();
        alertEntity.setType(request.getType());
        alertEntity.setPatientUuid(request.getPatientUuid());
        alertEntity.setImportance(request.getImportance());
        alertEntity.setStatus(request.getStatus());
        alertEntity.setStaffUuid(request.getStaffUuid());
        return alertMapper.toDto(alertRepository.save(alertEntity));
    }
    public AlertDTO update(final AlertDTO request) throws Exception {
        final AlertEntity alertEntity = this.alertRepository.getAlertEntityByAlertUuid(request.getAlertUuid());

        alertEntity.setType(request.getType());
        alertEntity.setPatientUuid(request.getPatientUuid());
        alertEntity.setImportance(request.getImportance());
        alertEntity.setStatus(request.getStatus());
        alertEntity.setStaffUuid(request.getStaffUuid());
        return alertMapper.toDto(alertRepository.save(alertEntity));
    }

    public Set<AlertDTO> get(final String alertUuid) throws Exception {
        final Set<AlertEntity> ret = new HashSet<>();

        if (alertUuid != null) {
            ret.add(this.alertRepository.getAlertEntityByAlertUuid(alertUuid));
        } else {
            for (final AlertEntity alert : this.alertRepository.findAll()) {
                ret.add(alert);
            }
        }
        Set<AlertDTO> toRet = new HashSet<>();
        for (AlertEntity alertEntity : ret) {
            toRet.add(this.alertMapper.toDto(alertEntity));
        }
        return toRet;
    }

}
