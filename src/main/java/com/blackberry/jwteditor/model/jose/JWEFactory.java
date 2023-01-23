package com.blackberry.jwteditor.model.jose;

import com.blackberry.jwteditor.model.jose.exceptions.EncryptionException;
import com.blackberry.jwteditor.model.keys.Key;
import com.nimbusds.jose.*;
import com.nimbusds.jose.util.Base64URL;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Provider;
import java.security.Security;
import java.text.ParseException;

import static java.util.Arrays.stream;

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

    /**
     * Parse a JWE from compact serialization
     *
     * @param compactJWE JWE in compact serialization form
     * @return a parsed JWE object
     * @throws ParseException if the value is not a valid JWE
     */
    public static JWE parse(String compactJWE) throws ParseException {
        if (StringUtils.countMatches(compactJWE, ".") != 4) {
            throw new ParseException("Invalid number of encoded fields", 0);
        }

        Base64URL[] parts = com.nimbusds.jose.JOSEObject.split(compactJWE);

        boolean allEmpty = stream(parts).allMatch(part -> part.decodeToString().isEmpty());

        if (allEmpty) {
            throw new ParseException("All sections empty", 0);
        }

        return new JWE(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }
}
