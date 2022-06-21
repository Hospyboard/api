package com.hospyboard.api.app.user.entity;

import com.hospyboard.api.app.core.db_converters.EncryptionDatabaseString;
import fr.funixgaming.api.core.crud.entities.ApiEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity(name = "user_password_reset_codes")
public class UserPasswordReset extends ApiEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Convert(converter = EncryptionDatabaseString.class)
    private String code;

    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;
}
