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
        AlertDTO ret = service.create(request);
        service.updateWS();
        return ret;
    }
    @PutMapping
    public AlertDTO update(@RequestBody AlertDTO request) throws  Exception {
        AlertDTO ret = service.update(request);
        service.updateWS();
        return ret;
    }

    @GetMapping
    public Set<AlertDTO> get(@RequestParam String alertUuid) throws Exception {
        Set<AlertDTO> ret = service.get(alertUuid);
        service.updateWS();
        return ret;
    }
    @GetMapping("/all")
    public Set<AlertDTO> getALL() throws Exception {
        return service.get(null);
    }

}
