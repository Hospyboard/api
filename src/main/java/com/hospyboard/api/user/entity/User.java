package com.hospyboard.api.user.entity;

import com.hospyboard.api.core.db_converters.EncryptionDatabaseString;
import com.hospyboard.api.user.enums.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity(name = "auth_user")
@Getter
@Setter
public class User implements Serializable, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @NaturalId
    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private String uuid;

    @PrePersist
    @PreUpdate
    public void onCreateOrUpdate() {
        if (Strings.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
        }
    }

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
