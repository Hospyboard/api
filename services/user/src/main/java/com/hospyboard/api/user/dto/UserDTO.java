package com.hospyboard.api.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> role;
}
