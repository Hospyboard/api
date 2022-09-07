package com.hospyboard.api.app.hospital.services;

import com.hospyboard.api.app.hospital.dto.RoomDTO;
import com.hospyboard.api.app.hospital.dto.ServiceDTO;
import com.hospyboard.api.app.hospital.dto.requests.LinkRoomAndPatientDTO;
import com.hospyboard.api.app.hospital.entity.Room;
import com.hospyboard.api.app.hospital.entity.ServiceEntity;
import com.hospyboard.api.app.hospital.mappers.RoomMapper;
import com.hospyboard.api.app.hospital.repositories.RoomRepository;
import com.hospyboard.api.app.hospital.repositories.ServiceRepository;
import com.hospyboard.api.app.user.dto.UserDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.mappers.UserMapper;
import com.hospyboard.api.app.user.repository.UserRepository;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomsService extends ApiService<RoomDTO, Room, RoomMapper, RoomRepository> {

    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public RoomsService(RoomRepository repository,
                        RoomMapper mapper,
                        ServiceRepository serviceRepository,
                        UserRepository userRepository,
                        UserMapper userMapper) {
        super(repository, mapper);
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public void beforeSendingDTO(@NonNull RoomDTO roomDTO, Room entity) {
        final List<UserDTO> patients = new ArrayList<>();

        for (final User user : userRepository.findAllByRoomUuid(roomDTO.getId().toString())) {
            patients.add(userMapper.toDto(user));
        }
        roomDTO.setPatients(patients);
    }

    @Override
    public void beforeSavingEntity(@NonNull RoomDTO request, @NonNull Room room) {
        if (request.getId() == null) {
            final ServiceEntity service = findService(request.getService());
            room.setService(service);
        }
    }

    @Transactional
    public RoomDTO addPatient(LinkRoomAndPatientDTO request) {
        final Optional<User> patientSearch = userRepository.findByUuid(request.getPatientId().toString());
        final Optional<Room> roomSearch = getRepository().findByUuid(request.getRoomId().toString());

        if (patientSearch.isPresent() && roomSearch.isPresent()) {
            final User patient = patientSearch.get();
            final Room room = roomSearch.get();

            patient.setRoomUuid(room.getUuid());
            userRepository.save(patient);

            return this.findById(room.getUuid().toString());
        } else {
            throw new ApiNotFoundException("Le patient ou la chambre n'existe pas.");
        }
    }

    private ServiceEntity findService(@Nullable final ServiceDTO serviceDTO) {
        if (serviceDTO == null) {
            throw new ApiBadRequestException("Vous n'avez pas spécifié de service id.");
        } else {
            if (serviceDTO.getId() == null) {
                throw new ApiBadRequestException("Votre service n'a pas d'ID.");
            } else {
                final Optional<ServiceEntity> search = this.serviceRepository.findByUuid(serviceDTO.getId().toString());

                if (search.isPresent()) {
                    return search.get();
                } else {
                    throw new ApiNotFoundException(String.format("Le service ID %s n'existe pas.", serviceDTO.getId()));
                }
            }
        }
    }

}
