package com.hospyboard.api.core.db_converters;

import com.hospyboard.api.core.configs.CryptSecretConfig;

import javax.crypto.NoSuchPaddingException;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

@Converter
public class EncryptionDatabaseInstant extends AEncryptionDatabase implements AttributeConverter<Instant, String> {

    public EncryptionDatabaseInstant(CryptSecretConfig cryptSecretConfig) throws NoSuchPaddingException, NoSuchAlgorithmException {
        super(cryptSecretConfig);
    }

    @Override
    public String convertToDatabaseColumn(Instant instant) {
        return super.convertToDatabase(Long.toString(instant.getEpochSecond()));
    }

    @Override
    public Instant convertToEntityAttribute(String dbData) {
        return Instant.ofEpochSecond(Long.parseLong(super.convertToEntity(dbData)));
    }
}
