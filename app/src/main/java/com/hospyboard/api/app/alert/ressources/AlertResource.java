package com.hospyboard.api.app.alert.ressources;

import com.hospyboard.api.app.alert.dto.AlertDTO;
import com.hospyboard.api.app.alert.services.AlertService;
import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.enums.UserRole;
import com.hospyboard.api.app.user.services.CurrentUser;
import fr.funixgaming.api.core.crud.resources.ApiResource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("alert")
public class AlertResource extends ApiResource<AlertDTO, AlertService> {

    public AlertResource(AlertService alertService) {
        super(alertService);
    }

}
