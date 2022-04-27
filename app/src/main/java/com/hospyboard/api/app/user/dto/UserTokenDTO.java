package com.hospyboard.api.app.user.dto;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class UserTokenDTO extends ApiDTO {
    private UserDTO user;
    private String token;
    private Date expirationDate;
}
