package com.hospyboard.api.app.core.db_converters;

import fr.funixgaming.api.core.utils.encryption.ApiConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Converter
@RequiredArgsConstructor
public class EncryptionDatabaseString implements ApiConverter<String> {

    private final HospyboardEncryption encryption;

    @Override
    public synchronized String convertToDatabaseColumn(String attribute) {
        return encryption.convertToDatabase(attribute);
    }

    @Override
    public synchronized String convertToEntityAttribute(String dbData) {
        return encryption.convertToEntity(dbData);
    }
}
