package com.hospyboard.api.auth.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class AuthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Id
    @GeneratedValue(generator = "UUID")
    private String uuid;

    private String email;

    private String password;
}
