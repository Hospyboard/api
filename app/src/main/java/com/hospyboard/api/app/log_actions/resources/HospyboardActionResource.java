package com.hospyboard.api.app.log_actions.resources;

import com.hospyboard.api.app.log_actions.dto.HospyboardActionDTO;
import com.hospyboard.api.app.log_actions.services.LogActionService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("log")
public class HospyboardActionResource {

    private final LogActionService actionsService;

    public HospyboardActionResource(LogActionService actionsService) {
        this.actionsService = actionsService;
    }

    @PostMapping
    @Transactional
    public HospyboardActionDTO doAction(@RequestBody HospyboardActionDTO actionDTO) {
        return this.actionsService.doAction(actionDTO);
    }

    @GetMapping
    public List<HospyboardActionDTO> getAll() {
        return this.actionsService.getAllActions();
    }

}
