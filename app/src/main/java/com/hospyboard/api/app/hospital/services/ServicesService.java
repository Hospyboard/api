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
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;

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
    @Transactional
    public ServiceDTO create(ServiceDTO request) {
        final ServiceEntity service = getMapper().toEntity(request);
        service.setHospital(findHospital(request.getHospital()));

        return getMapper().toDto(getRepository().save(service));
    }

    @NonNull
    @Override
    @Transactional
    public ServiceDTO update(ServiceDTO request) {
        if (request.getId() == null) {
            throw new ApiBadRequestException("Pas d'id spécifié pour la mise à jour du service.");
        } else {
            final Optional<ServiceEntity> search = getRepository().findByUuid(request.getId().toString());

            if (search.isPresent()) {
                final ServiceEntity entRequest = getMapper().toEntity(request);
                ServiceEntity service = search.get();

                entRequest.setId(null);
                entRequest.setUpdatedAt(Date.from(Instant.now()));
                getMapper().patch(entRequest, service);

                final Hospital hospital = findHospital(request.getHospital());
                service.setHospital(hospital);

                return getMapper().toDto(getRepository().save(service));
            } else {
                throw new ApiNotFoundException(String.format("Le service id %s n'existe pas.", request.getId()));
            }
        }
    }

    @Override
    @Transactional
    public List<ServiceDTO> update(List<ServiceDTO> request) {
        final List<ServiceDTO> result = new ArrayList<>();

        for (final ServiceDTO req : request) {
            result.add(this.update(req));
        }
        return result;
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
