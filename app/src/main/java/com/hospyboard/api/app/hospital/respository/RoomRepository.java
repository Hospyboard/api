package com.hospyboard.api.app.hospital.respository;

import com.hospyboard.api.app.hospital.entity.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
}
