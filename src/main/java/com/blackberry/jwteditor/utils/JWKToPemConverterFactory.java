package com.blackberry.jwteditor.utils;

import com.blackberry.jwteditor.model.keys.JWKKey;
import com.blackberry.jwteditor.model.keys.Key;
import com.blackberry.jwteditor.model.keys.KeyType;
import com.blackberry.jwteditor.utils.PEMUtils.PemException;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.jwk.RSAKey;

public class JWKToPemConverterFactory {
    public static JWKToPemConverter converterFor(JWK jwk) throws PemException {
        KeyType keyType = keyTypeFor(jwk);

        switch (keyType){
            case RSA:
                return new RSAKeyToJWKToPemConverter((RSAKey) jwk);
            case EC:
                return new ECKeyToJWKToPemConverter((ECKey) jwk);
            case OKP:
                return new OKPKeyToJWKToPemConverter((OctetKeyPair) jwk);
            default:
                throw new PemException("Invalid JWK type for PEM conversions");
        }
    }

    private static KeyType keyTypeFor(JWK jwk) throws PemException {
        try {
            return new JWKKey(jwk).getKeyType();
        } catch (Key.UnsupportedKeyException e) {
            throw new PemException("Invalid JWK type for PEM conversions");
        }
    }
}
