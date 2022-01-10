package com.hospyboard.api.core.db_converters;

import com.hospyboard.api.core.configs.CryptSecretConfig;

import javax.crypto.NoSuchPaddingException;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.security.NoSuchAlgorithmException;

@Converter
public class EncryptionDatabaseString extends AEncryptionDatabase implements AttributeConverter<String, String> {

    public EncryptionDatabaseString(CryptSecretConfig cryptSecretConfig) throws NoSuchPaddingException, NoSuchAlgorithmException {
        super(cryptSecretConfig);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return this.convertToDatabase(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return this.convertToEntity(dbData);
    }
}
