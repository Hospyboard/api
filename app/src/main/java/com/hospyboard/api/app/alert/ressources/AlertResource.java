package com.hospyboard.api.app.alert.ressources;

import com.hospyboard.api.app.alert.dto.AlertDTO;
import com.hospyboard.api.app.alert.enums.AlertStatus;
import com.hospyboard.api.app.alert.services.AlertService;
import fr.funixgaming.api.core.crud.resources.ApiResource;
import org.bouncycastle.util.Strings;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("alert")
public class AlertResource extends ApiResource<AlertDTO, AlertService> {

    public AlertResource(AlertService alertService) {
        super(alertService);
    }

    @GetMapping("patient")
    public List<AlertDTO> fetchPatientAlerts() {
        return getService().fetchPatientAlerts();
    }

    @GetMapping("patientId")
    public List<AlertDTO> fetchPatientAlertsById(@RequestParam String patientId, @RequestParam String status) {
        final String[] statusListNames = Strings.split(status, ',');
        final Set<AlertStatus> alertStatuses = new HashSet<>();

        for (final String statusGet : statusListNames) {
            alertStatuses.add(AlertStatus.valueOf(statusGet));
        }
        return getService().fetchPatientAlertsById(patientId, alertStatuses);
    }

    @PostMapping("removeAll")
    public void removeAllAlerts() {
        getService().getRepository().deleteAll();
    }
}
