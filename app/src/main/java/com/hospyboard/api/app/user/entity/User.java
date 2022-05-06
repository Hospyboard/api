package com.hospyboard.api.app.user.entity;

import com.hospyboard.api.app.alert.entity.AlertEntity;
import com.hospyboard.api.app.core.db_converters.EncryptionDatabaseString;
import fr.funixgaming.api.core.crud.entities.ApiEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity(name = "auth_user")
public class User extends ApiEntity implements UserDetails {
    @Column(nullable = false, unique = true)
    @Convert(converter = EncryptionDatabaseString.class)
    private String username;

    @Column(nullable = false)
    @Convert(converter = EncryptionDatabaseString.class)
    private String password;

    @Column(name = "first_name", nullable = false)
    @Convert(converter = EncryptionDatabaseString.class)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Convert(converter = EncryptionDatabaseString.class)
    private String lastName;

    @Column(nullable = false)
    @Convert(converter = EncryptionDatabaseString.class)
    private String email;

    @Column(name = "user_role", nullable = false)
    @Convert(converter = EncryptionDatabaseString.class)
    private String role;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<UserToken> tokens;

    @OneToMany(mappedBy = "patient", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<AlertEntity> patientAlerts;

    @OneToMany(mappedBy = "staff", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<AlertEntity> staffAlerts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
