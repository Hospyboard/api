package com.hospyboard.api.app.user.entity;

import com.hospyboard.api.app.core.db_converters.EncryptionDatabaseString;
import fr.funixgaming.api.core.crud.entities.ApiEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity(name = "user_password_reset_codes")
public class UserPasswordReset extends ApiEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Convert(converter = EncryptionDatabaseString.class)
    @Column(length = 1000)
    private String code;

    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    public boolean isValid() {
        return expirationDate.toInstant().isAfter(Instant.now());
    }
}
