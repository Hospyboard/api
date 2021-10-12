package com.hospyboard.api.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String id;
    private String email;
    private String token;
    private String firstName;
    private String lastName;
}
