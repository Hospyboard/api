package com.hospyboard.api.app.file_storage.repositories;

import com.hospyboard.api.app.file_storage.entities.FileEntity;
import com.hospyboard.api.app.user.entity.User;
import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FilesRepository extends ApiRepository<FileEntity> {
    Set<FileEntity> findAllByFileOwner(final User owner);
}
