package com.hospyboard.api.app.hospital.services;

import com.hospyboard.api.app.hospital.dto.HospitalDTO;
import com.hospyboard.api.app.hospital.entity.Hospital;
import com.hospyboard.api.app.hospital.mappers.HospitalMapper;
import com.hospyboard.api.app.hospital.repositories.HospitalRepository;
import fr.funixgaming.api.core.crud.services.ApiService;
import org.springframework.stereotype.Service;

@Service
public class HospitalsService extends ApiService<HospitalDTO, Hospital, HospitalMapper, HospitalRepository> {

    public HospitalsService(HospitalRepository repository,
                            HospitalMapper mapper) {
        super(repository, mapper);
    }

}
