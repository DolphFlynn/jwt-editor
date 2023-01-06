package com.blackberry.jwteditor.model.jose;

import com.blackberry.jwteditor.model.jose.exceptions.EncryptionException;
import com.blackberry.jwteditor.model.keys.Key;
import com.nimbusds.jose.*;

import java.nio.charset.StandardCharsets;
import java.security.Provider;
import java.security.Security;

public class JWEFactory {
    public static JWE encrypt(JWS jws, Key key, JWEAlgorithm kek, EncryptionMethod cek) throws EncryptionException {
        JWEHeader header = new JWEHeader(kek, cek);

        // Get the encrypter based on the key type
        JWEEncrypter encrypter;
        try {
            encrypter = key.getEncrypter(kek);
        } catch (JOSEException e) {
            throw new EncryptionException("Invalid key type for encryption algorithm");
        }

        // Try to use the BouncyCastle provider, but fall-back to default if this fails
        Provider provider = Security.getProvider("BC");
        if (provider != null) {
            encrypter.getJCAContext().setProvider(provider);
        }

        // Encrypt the JWS with the key to get a set of Base64 encoded parts
        JWECryptoParts jweCryptoParts;
        try {
            jweCryptoParts = encrypter.encrypt(header, jws.serialize().getBytes(StandardCharsets.US_ASCII));
        } catch (JOSEException e) {
            throw new EncryptionException(e.getMessage());
        }

        // Use the returned parts to construct a JWE
        return new JWE(
                jweCryptoParts.getHeader().toBase64URL(),
                jweCryptoParts.getEncryptedKey(),
                jweCryptoParts.getInitializationVector(),
                jweCryptoParts.getCipherText(),
                jweCryptoParts.getAuthenticationTag()
        );
    }
}
