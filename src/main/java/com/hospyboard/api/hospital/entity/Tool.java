package com.hospyboard.api.hospital.entity;

import com.hospyboard.api.hospital.enums.ToolTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "hospital_room_tool")
@Getter
@Setter
public class Tool {
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

    @Enumerated(EnumType.STRING)
    @Column(name = "tool_type")
    private ToolTypeEnum toolType;
}
