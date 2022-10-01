package com.hospyboard.api.app.alert.ressources;

import com.hospyboard.api.app.alert.dto.AlertDTO;
import com.hospyboard.api.app.alert.services.AlertService;
import fr.funixgaming.api.core.crud.resources.ApiResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
