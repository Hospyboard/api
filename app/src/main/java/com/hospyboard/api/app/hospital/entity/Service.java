package com.hospyboard.api.app.hospital.entity;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity(name = "service")
public class Service extends ApiEntity {
    @Column(nullable = false)
    private String name;

    @MapsId
    @ManyToOne
    @JoinColumn(nullable = false, name = "hospital_id")
    private Hospital hospital;

    @OneToMany(mappedBy = "service", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Room> rooms;
}
