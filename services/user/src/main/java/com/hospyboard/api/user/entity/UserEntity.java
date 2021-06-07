package com.hospyboard.api.user.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "auth_entity")
@Getter
@Setter
public class AuthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /*@Id
    @GeneratedValue(generator = "UUID")*/
    private String uuid;

    private String email;

    private String password;
}
