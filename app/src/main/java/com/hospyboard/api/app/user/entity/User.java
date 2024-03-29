package com.hospyboard.api.app.user.entity;

import com.hospyboard.api.app.core.db_converters.EncryptionDatabaseString;
import fr.funixgaming.api.core.crud.entities.ApiEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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

    @Column(length = 10000)
    @Convert(converter = EncryptionDatabaseString.class)
    private String infos;

    @Column(name = "hospital_room_uuid")
    private String roomUuid;

    @Column(name = "last_login_at")
    private Date lastLoginAt;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<UserToken> tokens = new HashSet<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<UserPasswordReset> passwordResets = new HashSet<>();

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

    public UUID getRoomUuid() {
        if (roomUuid != null) {
            return UUID.fromString(roomUuid);
        } else {
            return null;
        }
    }

    public void setRoomUuid(UUID roomUuid) {
        if (roomUuid == null) {
            this.roomUuid = null;
        } else {
            this.roomUuid = roomUuid.toString();
        }
    }
}
