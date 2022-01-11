package com.hospyboard.api.user.dto;

import com.hospyboard.api.user.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreationDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String passwordConfirmation;
    private UserRole role;
}
