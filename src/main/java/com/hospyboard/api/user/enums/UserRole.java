package com.hospyboard.api.user.enums;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

public class UserRole {
    public static final String PATIENT = "PATIENT";
    public static final String HOSPYTAL_WORKER = "HOSPYTAL_WORKER";
    public static final String ADMIN = "ADMIN";
}
