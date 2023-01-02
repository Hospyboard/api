package com.hospyboard.api.app.file_storage.services;

import com.hospyboard.api.app.file_storage.dtos.FileDTO;
import com.hospyboard.api.app.file_storage.entities.FileEntity;
import com.hospyboard.api.app.file_storage.mappers.FilesMapper;
import com.hospyboard.api.app.file_storage.repositories.FilesRepository;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.repository.UserRepository;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class FileStorageService extends ApiService<FileDTO, FileEntity, FilesMapper, FilesRepository> {

    private final File filesFolder = new File("files_storage");
    private final UserRepository userRepository;

    public FileStorageService(FilesRepository repository,
                              UserRepository userRepository,
                              FilesMapper mapper) throws ApiException {
        super(repository, mapper);
        this.userRepository = userRepository;

        try {
            if (!filesFolder.exists() && !filesFolder.mkdir()) {
                throw new IOException("files_storage can't be created.");
            }
        } catch (IOException e) {
            throw new ApiException("Impossible de créer le dossier qui contient les fichiers upload.", e);
        }
    }

    @Override
    @NonNull
    @Transactional
    public FileDTO findById(String id) {
        final Optional<FileEntity> search = getRepository().findByUuid(id);

        try {
            if (search.isPresent()) {
                final FileEntity file = search.get();
                final FileDTO toSend = getMapper().toDto(file);

                toSend.setFile(Files.readAllBytes(Path.of(file.getFilePath())));
                return toSend;
            } else {
                throw new ApiNotFoundException("Le fichier n'existe pas.");
            }
        } catch (IOException e) {
            throw new ApiException("Erreur lors de la récupération du fichier.", e);
        }
    }

    @Override
    public void beforeSavingEntity(@NonNull FileDTO request, @NonNull FileEntity entity) {
        if (request.getId() == null) {
            final File userFolder = new File(filesFolder, request.getFileOwner().getId().toString());

            try {
                if (!userFolder.exists() && !userFolder.mkdir()) {
                    throw new IOException("Could not create user folder.");
                }
                final File fileToSave = new File(userFolder, request.getFileName());
                if (!fileToSave.exists() && !fileToSave.createNewFile()) {
                    throw new IOException("Could not create final file.");
                }
                Files.write(fileToSave.toPath(), request.getFile());

                final User fileOwner = findUserByUuid(request.getFileOwner().getId().toString());
                entity.setFileOwner(fileOwner);
                entity.setFilePath(fileToSave.getPath());
            } catch (IOException e) {
                throw new ApiException(String.format("Impossible d'enregistrer le fichier. Erreur: %s", e.getMessage()), e);
            }
        } else {
            throw new ApiBadRequestException("Vous ne pouvez pas modifier les data des fichiers.");
        }
    }

    @Override
    public void beforeDeletingEntity(@NonNull FileEntity entity) {
        final File file = new File(entity.getFilePath());

        if (!file.delete()) {
            log.info("Le fichier {} n'existe pas, pas besoin de le supprimer.", entity.getFilePath());
        }
    }

    public void deleteAllFilesByUser(final User user) {
        final Set<FileEntity> files = super.getRepository().findAllByFileOwner(user);

        for (final FileEntity file : files) {
            beforeDeletingEntity(file);
        }
        super.getRepository().deleteAll(files);
    }

    private User findUserByUuid(final String userUuid) {
        final Optional<User> search = userRepository.findByUuid(userUuid);

        if (search.isPresent()) {
            return search.get();
        } else {
            throw new ApiBadRequestException("L'utilisateur demandé n'existe pas.");
        }
    }
}
