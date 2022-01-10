package com.hospyboard.api.log_actions.entity;

import com.hospyboard.api.core.db_converters.EncryptionDatabaseInstant;
import com.hospyboard.api.core.db_converters.EncryptionDatabaseString;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity(name = "hospyboard_action")
@Getter
@Setter
public class HospyboardAction {
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

    @Column(name = "user_uuid")
    @Convert(converter = EncryptionDatabaseString.class)
    private String userUuid;

    @Column(name = "requested_at")
    @Convert(converter = EncryptionDatabaseInstant.class)
    private Instant requestedAt;

    @Column(name = "service")
    @Convert(converter = EncryptionDatabaseString.class)
    private String service;

    @Column(name = "route_name")
    @Convert(converter = EncryptionDatabaseString.class)
    private String routeName;

}
