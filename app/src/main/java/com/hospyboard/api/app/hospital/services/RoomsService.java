package com.hospyboard.api.app.hospital.services;

import com.hospyboard.api.app.hospital.dto.RoomDTO;
import com.hospyboard.api.app.hospital.dto.ServiceDTO;
import com.hospyboard.api.app.hospital.entity.Room;
import com.hospyboard.api.app.hospital.entity.Service;
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

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.*;

@org.springframework.stereotype.Service
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
    public List<RoomDTO> getAll(String page, String elemsPerPage) {
        final List<RoomDTO> list = super.getAll(page, elemsPerPage);

        for (final RoomDTO roomDTO : list) {
            addUsersInRoom(roomDTO);
        }
        return list;
    }

    @Override
    public RoomDTO findById(String id) {
        final RoomDTO roomDTO = super.findById(id);
        addUsersInRoom(roomDTO);
        return roomDTO;
    }

    @Override
    public List<RoomDTO> search(String search, String page, String elemsPerPage) {
        final List<RoomDTO> list = super.search(search, page, elemsPerPage);

        for (final RoomDTO roomDTO : list) {
            addUsersInRoom(roomDTO);
        }
        return list;
    }

    @Override
    public RoomDTO create(RoomDTO request) {
        final Room room = getMapper().toEntity(request);
        final Service service = findService(request.getService());
        room.setService(service);

        return getMapper().toDto(getRepository().save(room));
    }

    @Override
    public RoomDTO update(RoomDTO request) {
        if (request.getId() == null) {
            throw new ApiBadRequestException("Pas d'id spécifié pour la mise à jour de la chambre.");
        } else {
            final Optional<Room> search = getRepository().findByUuid(request.getId().toString());

            if (search.isPresent()) {
                final Room entRequest = getMapper().toEntity(request);
                Room room = search.get();

                entRequest.setId(null);
                entRequest.setUpdatedAt(Date.from(Instant.now()));
                getMapper().patch(entRequest, room);

                final Service service = findService(request.getService());
                room.setService(service);

                return getMapper().toDto(getRepository().save(room));
            } else {
                throw new ApiNotFoundException(String.format("La chambre id %s n'existe pas.", request.getId()));
            }
        }
    }

    @Override
    public List<RoomDTO> update(List<RoomDTO> request) {
        final List<RoomDTO> result = new ArrayList<>();

        for (final RoomDTO req : request) {
            result.add(this.update(req));
        }
        return result;
    }

    private void addUsersInRoom(@Nullable final RoomDTO roomDTO) {
        if (roomDTO == null) {
            return;
        }

        final List<UserDTO> patients = new ArrayList<>();

        for (final User user : userRepository.findAllByRoomUuid(roomDTO.getId().toString())) {
            patients.add(userMapper.toDto(user));
        }
        roomDTO.setPatients(patients);
    }

    private Service findService(@Nullable final ServiceDTO serviceDTO) {
        if (serviceDTO == null) {
            throw new ApiBadRequestException("Vous n'avez pas spécifié de service id.");
        } else {
            if (serviceDTO.getId() == null) {
                throw new ApiBadRequestException("Votre service n'a pas d'ID.");
            } else {
                final Optional<Service> search = this.serviceRepository.findByUuid(serviceDTO.getId().toString());

                if (search.isPresent()) {
                    return search.get();
                } else {
                    throw new ApiNotFoundException(String.format("Le service ID: %s n'existe pas.", serviceDTO.getId()));
                }
            }
        }
    }

}
