package com.hospyboard.api.app.hospital.entity;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity(name = "hospital")
public class Hospital extends ApiEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "hospital", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ServiceEntity> services;
}
