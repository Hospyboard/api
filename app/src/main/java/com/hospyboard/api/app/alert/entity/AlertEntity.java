package com.hospyboard.api.app.alert.entity;

import com.hospyboard.api.app.alert.dto.AlertImportance;
import com.hospyboard.api.app.alert.dto.AlertStatus;
import com.hospyboard.api.app.alert.dto.AlertType;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "alert_entity")
@Getter
@Setter
public class AlertEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @NaturalId
    @Column(name = "alertUuid", nullable = false, updatable = false, unique = true)
    private String alertUuid;

    @PrePersist
    @PreUpdate
    public void onCreateOrUpdate() {
        if (Strings.isEmpty(alertUuid)) {
            alertUuid = UUID.randomUUID().toString();
        }
    }

    @Column(nullable = false)
    private String patientUuid;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlertType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlertImportance importance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlertStatus status;

    @Column(nullable = true)
    private String staffUuid;

}
