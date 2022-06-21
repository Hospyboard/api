package com.hospyboard.api.app.hospital.ressources;

import com.hospyboard.api.app.hospital.dto.HospitalDTO;
import com.hospyboard.api.app.hospital.services.HospitalsService;
import fr.funixgaming.api.core.crud.resources.ApiResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hospital")
public class HospitalResource extends ApiResource<HospitalDTO, HospitalsService> {
    public HospitalResource(HospitalsService hospitalsService) {
        super(hospitalsService);
    }
}
