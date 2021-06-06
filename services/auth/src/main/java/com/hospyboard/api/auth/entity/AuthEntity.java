package com.hospyboard.api.auth.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class AuthEntity {
    @Id
    private Long id;

    @Id
    private String uuid;
}
