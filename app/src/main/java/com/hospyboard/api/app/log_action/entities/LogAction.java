package com.hospyboard.api.app.log_action.entities;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity(name = "log_action")
public class LogAction extends ApiEntity {
    @Column(nullable = false, name = "when_access")
    private Date when;

    @Column(nullable = false)
    private String route;

    @Column(nullable = false, name = "http_method")
    private String httpMethod;

    @Column(nullable = false)
    private String ip;
}
