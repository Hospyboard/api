package com.hospyboard.api.app.user.entity;

import com.hospyboard.api.app.core.db_converters.EncryptionDatabaseString;
import fr.funixgaming.api.core.crud.entities.ApiEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity(name = "user_tokens")
public class UserToken extends ApiEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, updatable = false, length = 2000)
    @Convert(converter = EncryptionDatabaseString.class)
    private String token;

    @Column(name = "expiration_date", updatable = false, nullable = false)
    private Date expirationDate;
}
