package com.hospyboard.api.alert.ressources;

import com.hospyboard.api.alert.dto.AlertDTO;
import com.hospyboard.api.alert.services.AlertService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("alert")
public class AlertResource {

    private final AlertService service;

    public AlertResource(AlertService alertService) {
        this.service = alertService;
    }

    @PostMapping("create")
    public AlertDTO create(@RequestBody AlertDTO request) throws Exception {
        return this.service.createOrUpdate(request);
    }

    @GetMapping("get")
    public AlertDTO[] get(@RequestParam String alertUuid) throws Exception {
        return this.service.get(alertUuid);
    }

}
