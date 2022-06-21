package com.hospyboard.api.app.hospital.repositories;

import com.hospyboard.api.app.hospital.entity.Hospital;
import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends ApiRepository<Hospital> {
}
