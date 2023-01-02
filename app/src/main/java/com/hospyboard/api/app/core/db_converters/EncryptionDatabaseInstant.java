package com.hospyboard.api.app.core.db_converters;

import fr.funixgaming.api.core.utils.encryption.ApiConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Converter
@RequiredArgsConstructor
public class EncryptionDatabaseInstant implements ApiConverter<Instant> {

    private final HospyboardEncryption encryption;

    @Override
    public synchronized String convertToDatabaseColumn(Instant instant) {
        return encryption.convertToDatabase(Long.toString(instant.getEpochSecond()));
    }

    @Override
    public synchronized Instant convertToEntityAttribute(String dbData) {
        return Instant.ofEpochSecond(Long.parseLong(encryption.convertToEntity(dbData)));
    }
}
