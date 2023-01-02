package com.hospyboard.api.app.log_action.entities;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "log_action")
public class LogAction extends ApiEntity {
    @Column(nullable = false, name = "when_access")
    private Date when;

    @Column(nullable = false)
    private String route;

    @Column(name = "user_uuid")
    private String userUuid;

    @Column(nullable = false, name = "http_method")
    private String httpMethod;

    @Column(nullable = false)
    private String ip;

    @Nullable
    public UUID getUserUuid() {
        if (userUuid == null) {
            return null;
        } else {
            return UUID.fromString(userUuid);
        }
    }

    public void setUserUuid(@Nullable UUID userUuid) {
        if (userUuid == null) {
            this.userUuid = null;
        } else {
            this.userUuid = userUuid.toString();
        }
    }
}
