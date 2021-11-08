package com.hospyboard.api.alert.ressources;

import com.hospyboard.api.alert.dto.AlertDTO;
import com.hospyboard.api.alert.services.AlertService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Set;

@RestController
@RequestMapping("alert")
public class AlertResource {

    private final AlertService service;

    public AlertResource(AlertService alertService) {
        this.service = alertService;
    }

    @PostMapping
    @Transactional
    public AlertDTO create(@RequestBody AlertDTO request) throws Exception {
        return service.createOrUpdate(request);
    }

    @GetMapping
    public Set<AlertDTO> get(@RequestParam String alertUuid) throws Exception {
        return service.get(alertUuid);
    }

}
