package com.hospyboard.api.app.hospital.repositories;

import com.hospyboard.api.app.hospital.entity.Service;
import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends ApiRepository<Service> {
}
