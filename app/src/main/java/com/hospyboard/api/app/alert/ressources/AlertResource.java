package com.hospyboard.api.app.alert.ressources;

import com.hospyboard.api.app.alert.dto.AlertDTO;
import com.hospyboard.api.app.alert.dto.FileDto;
import com.hospyboard.api.app.alert.entity.AlertEntity;
import com.hospyboard.api.app.alert.services.AlertService;
import com.hospyboard.api.app.user.services.CurrentUser;
import fr.funixgaming.api.core.crud.resources.ApiResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("alert")
public class AlertResource extends ApiResource<AlertDTO, AlertService> {

    public AlertResource(AlertService alertService) {
        super(alertService);
    }

}
