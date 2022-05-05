package com.hospyboard.api.app.alert.entity;

import com.hospyboard.api.app.alert.enums.AlertImportance;
import com.hospyboard.api.app.alert.enums.AlertStatus;
import com.hospyboard.api.app.alert.enums.AlertType;
import com.hospyboard.api.app.user.entity.User;
import fr.funixgaming.api.core.crud.entities.ApiEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "alert_entity")
public class AlertEntity extends ApiEntity {
    @MapsId
    @OneToOne
    @Column(nullable = false, name = "patient_id")
    private User patient;

    @MapsId
    @OneToOne
    @Column(name = "staff_id")
    private User staff;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlertType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlertImportance importance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlertStatus status;

}
