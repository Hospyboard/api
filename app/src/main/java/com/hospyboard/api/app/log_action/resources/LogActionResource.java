package com.hospyboard.api.app.log_action.resources;

import com.hospyboard.api.app.log_action.dtos.LogActionDTO;
import com.hospyboard.api.app.log_action.services.LogActionService;
import fr.funixgaming.api.core.crud.resources.ApiResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("logAction")
public class LogActionResource extends ApiResource<LogActionDTO, LogActionService> {

    public LogActionResource(LogActionService logActionService) {
        super(logActionService);
    }

}
