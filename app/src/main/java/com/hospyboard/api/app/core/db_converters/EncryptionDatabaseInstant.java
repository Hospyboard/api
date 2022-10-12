package com.hospyboard.api.app.core.db_converters;

import fr.funixgaming.api.core.utils.encryption.ApiConverter;
import fr.funixgaming.api.core.utils.encryption.Encryption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Converter;
import java.time.Instant;

@Component
@Converter
@RequiredArgsConstructor
public class EncryptionDatabaseInstant implements ApiConverter<Instant> {

    private final Encryption encryption;

    @Override
    public synchronized String convertToDatabaseColumn(Instant instant) {
        return encryption.convertToDatabase(Long.toString(instant.getEpochSecond()));
    }

    @Override
    public synchronized Instant convertToEntityAttribute(String dbData) {
        return Instant.ofEpochSecond(Long.parseLong(encryption.convertToEntity(dbData)));
    }
}
