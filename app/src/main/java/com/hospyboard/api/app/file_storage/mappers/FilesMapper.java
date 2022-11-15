package com.hospyboard.api.app.file_storage.mappers;

import com.hospyboard.api.app.file_storage.dtos.FileDTO;
import com.hospyboard.api.app.file_storage.entities.FileEntity;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FilesMapper extends ApiMapper<FileEntity, FileDTO> {

    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "filePath", ignore = true)
    @Mapping(target = "fileOwner", ignore = true)
    @Mapping(target = "fileOwner.uuid", source = "fileOwner.id")
    @Mapping(target = "fileOwner.id", ignore = true)
    FileEntity toEntity(FileDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "file", ignore = true)
    @Mapping(target = "fileOwner", ignore = true)
    @Mapping(target = "fileOwner.id", source = "fileOwner.uuid")
    FileDTO toDto(FileEntity entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(FileEntity request, @MappingTarget FileEntity toPatch);
}
