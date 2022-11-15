package com.hospyboard.api.app.file_storage.dtos;

import com.hospyboard.api.app.user.dto.UserDTO;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDTO extends ApiDTO {
    private UserDTO fileOwner;
    private String fileName;
    private Long fileSize;

    private byte[] file;
}
