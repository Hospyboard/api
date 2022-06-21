package com.hospyboard.api.app.hospital.entity;

import com.hospyboard.api.app.user.entity.User;
import fr.funixgaming.api.core.crud.entities.ApiEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity(name = "room")
public class Room extends ApiEntity {

    @Column(nullable = false)
    private String name;

    @MapsId
    @ManyToOne
    @JoinColumn(nullable = false, name = "service_id")
    private Service service;

    @OneToMany(mappedBy = "hospitalRoom")
    private Set<User> patients;
}
