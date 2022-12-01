package com.hospyboard.api.app.file_storage.ressources;

import com.hospyboard.api.app.file_storage.dtos.FileDTO;
import com.hospyboard.api.app.file_storage.services.FileStorageService;
import com.hospyboard.api.app.user.services.CurrentUser;
import fr.funixgaming.api.core.crud.dtos.PageDTO;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("files")
public class FileStorageResource {

    private final FileStorageService fileStorageService;
    private final CurrentUser currentUser;
    private final Set<String> fileExtensionsValid = Set.of(
            ".txt",
            ".pdf",
            ".png",
            ".jpeg",
            ".gif",
            ".mp4",
            ".mp3"
    );
    private final String fileExtensionsValidString = String.join(" ", fileExtensionsValid);

    public FileStorageResource(FileStorageService fileStorageService,
                               CurrentUser currentUser) {
        this.fileStorageService = fileStorageService;
        this.currentUser = currentUser;
    }

    @GetMapping
    public PageDTO<FileDTO> getFiles(@RequestParam(value = "page", defaultValue = "0") String page,
                                     @RequestParam(value = "elemsPerPage", defaultValue = "300") String elemsPerPage,
                                     @RequestParam(value = "search", defaultValue = "") String search,
                                     @RequestParam(value = "sort", defaultValue = "") String sort) {
        return this.fileStorageService.getAll(page, elemsPerPage, search, sort);
    }

    @GetMapping("{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable("id") String id) {
        final FileDTO fileDTO = fileStorageService.findById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", fileDTO.getFileName()))
                .body(fileDTO.getFile());
    }

    @PostMapping
    public FileDTO uploadFile(@RequestParam(value = "data") @NotNull final MultipartFile file) {
        try {
            if (isFileValid(file)) {
                final FileDTO fileDTO = new FileDTO();

                fileDTO.setFile(file.getBytes());
                fileDTO.setFileOwner(currentUser.getCurrentUser());
                fileDTO.setFileName(String.format("%s-%s", UUID.randomUUID(), file.getResource().getFilename()));
                fileDTO.setFileSize(file.getSize());
                return fileStorageService.create(fileDTO);
            } else {
                throw new ApiBadRequestException("Format de fichier invalide. Voici les formats valides : " + fileExtensionsValidString);
            }
        } catch (IOException e) {
            throw new ApiException("Impossible de décoder le fichier reçu.", e);
        }
    }

    @DeleteMapping
    public void deleteFile(@RequestParam String id) {
        this.fileStorageService.delete(id);
    }

    private boolean isFileValid(final MultipartFile file) {
        for (final String ext : fileExtensionsValid) {
            final String fName = file.getResource().getFilename();

            if (fName != null && fName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

}
