package com.hospyboard.api.app.hospital.entity;

import com.hospyboard.api.app.core.db_converters.EncryptionDatabaseString;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "hospital_room")
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @NaturalId
    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private String uuid;

    @PrePersist
    @PreUpdate
    public void onCreateOrUpdate() {
        if (Strings.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
        }
    }

    @Column(name = "room_number")
    @Convert(converter = EncryptionDatabaseString.class)
    private String roomNumber;
}
