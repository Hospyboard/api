package com.hospyboard.api.app.hospital.ressources;

import com.hospyboard.api.app.hospital.dto.ServiceDTO;
import com.hospyboard.api.app.hospital.services.ServicesService;
import fr.funixgaming.api.core.crud.resources.ApiResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("service")
public class ServiceResource extends ApiResource<ServiceDTO, ServicesService> {
    public ServiceResource(ServicesService servicesService) {
        super(servicesService);
    }
}
