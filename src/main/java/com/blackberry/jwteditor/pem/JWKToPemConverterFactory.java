package com.blackberry.jwteditor.pem;

import com.blackberry.jwteditor.exceptions.PemException;
import com.blackberry.jwteditor.exceptions.UnsupportedKeyException;
import com.blackberry.jwteditor.model.keys.JWKKey;
import com.blackberry.jwteditor.model.keys.KeyType;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.jwk.RSAKey;

public class JWKToPemConverterFactory {
    public static JWKToPemConverter converterFor(JWK jwk) throws PemException {
        KeyType keyType = keyTypeFor(jwk);

        return switch (keyType) {
            case RSA -> new RSAKeyToJWKToPemConverter((RSAKey) jwk);
            case EC -> new ECKeyToJWKToPemConverter((ECKey) jwk);
            case OKP -> new OKPKeyToJWKToPemConverter((OctetKeyPair) jwk);
            default -> throw new PemException("Invalid JWK type for PEM conversions");
        };
    }

    private static KeyType keyTypeFor(JWK jwk) throws PemException {
        try {
            return new JWKKey(jwk).getKeyType();
        } catch (UnsupportedKeyException e) {
            throw new PemException("Invalid JWK type for PEM conversions");
        }
    }
}
