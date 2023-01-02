package com.hospyboard.api.app.hospital.entity;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity(name = "service")
public class ServiceEntity extends ApiEntity {
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false, name = "hospital_id")
    private Hospital hospital;

    @OneToMany(mappedBy = "service", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Room> rooms;
}
