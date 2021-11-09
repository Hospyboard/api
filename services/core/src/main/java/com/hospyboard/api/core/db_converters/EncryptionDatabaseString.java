package com.hospyboard.api.core.db_converters;

import com.hospyboard.api.core.configs.CryptSecretConfig;
import com.hospyboard.api.core.exceptions.HospyboardAppException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Converter
public class EncryptionDatabaseString implements AttributeConverter<String, String> {
    private static final String AES = "AES";

    private final Base64.Encoder base64Encoder;
    private final Base64.Decoder base64Decoder;
    private final Key key;
    private final Cipher cipher;

    public EncryptionDatabaseString(CryptSecretConfig cryptSecretConfig) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.key = new SecretKeySpec(cryptSecretConfig.getSecret().getBytes(), AES);
        this.cipher = Cipher.getInstance(AES);
        this.base64Encoder = Base64.getEncoder();
        this.base64Decoder = Base64.getDecoder();
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return base64Encoder.encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new HospyboardAppException("An error occurred while encoding to database.", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(base64Decoder.decode(dbData)));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new HospyboardAppException("An error occurred while decoding to app.", e);
        }
    }
}
