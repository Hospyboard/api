package com.hospyboard.api.app.hospital.services;

import com.hospyboard.api.app.hospital.dto.HospitalDTO;
import com.hospyboard.api.app.hospital.dto.ServiceDTO;
import com.hospyboard.api.app.hospital.entity.Hospital;
import com.hospyboard.api.app.hospital.entity.ServiceEntity;
import com.hospyboard.api.app.hospital.mappers.ServiceMapper;
import com.hospyboard.api.app.hospital.repositories.HospitalRepository;
import com.hospyboard.api.app.hospital.repositories.ServiceRepository;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicesService extends ApiService<ServiceDTO, ServiceEntity, ServiceMapper, ServiceRepository> {

    private final HospitalRepository hospitalRepository;

    public ServicesService(ServiceRepository repository,
                           ServiceMapper mapper,
                           HospitalRepository hospitalRepository) {
        super(repository, mapper);
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public void beforeSavingEntity(@NonNull ServiceDTO request, @NonNull ServiceEntity entity) {
        if (request.getId() == null) {
            final Hospital hospital = findHospital(request.getHospital());
            entity.setHospital(hospital);
        }
    }

    private Hospital findHospital(@Nullable final HospitalDTO hospitalDto) {
        if (hospitalDto == null) {
            throw new ApiBadRequestException("Vous n'avez pas spécifié d'hôpital id.");
        } else {
            if (hospitalDto.getId() == null) {
                throw new ApiBadRequestException("Votre hôpital n'a pas d'ID.");
            } else {
                final Optional<Hospital> search = this.hospitalRepository.findByUuid(hospitalDto.getId().toString());

                if (search.isPresent()) {
                    return search.get();
                } else {
                    throw new ApiNotFoundException(String.format("L'hôpital ID %s n'existe pas.", hospitalDto.getId()));
                }
            }
        }
    }
}
