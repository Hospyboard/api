package com.hospyboard.api.app.core.db_converters;

import fr.funixgaming.api.core.utils.encryption.ApiConverter;
import fr.funixgaming.api.core.utils.encryption.Encryption;
import org.springframework.stereotype.Component;

import javax.persistence.Converter;

@Component
@Converter
public class EncryptionDatabaseString implements ApiConverter<String> {

    private final Encryption encryption = new Encryption();

    @Override
    public synchronized String convertToDatabaseColumn(String attribute) {
        return encryption.convertToDatabase(attribute);
    }

    @Override
    public synchronized String convertToEntityAttribute(String dbData) {
        return encryption.convertToEntity(dbData);
    }
}
