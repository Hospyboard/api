package com.hospyboard.api.app.file_storage.entities;

import com.hospyboard.api.app.user.entity.User;
import fr.funixgaming.api.core.crud.entities.ApiEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity(name = "file_entity")
public class FileEntity extends ApiEntity {

    @ManyToOne
    @JoinColumn(name = "file_owner", nullable = false)
    private User fileOwner;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;
}
