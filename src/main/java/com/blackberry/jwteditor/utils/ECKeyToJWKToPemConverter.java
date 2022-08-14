package com.blackberry.jwteditor.utils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.ECKey;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.bouncycastle.util.io.pem.PemObject;

import java.io.IOException;

class ECKeyToJWKToPemConverter extends JWKToPemConverter {
    private final ECKey ecKey;

    ECKeyToJWKToPemConverter(ECKey ecKey) {
        super(ecKey);
        this.ecKey = ecKey;
    }

    @Override
    PemObject encodePublicKey() throws JOSEException {
        return new PemObject(PUBLIC_KEY_PEM_LABEL, ecKey.toECPublicKey().getEncoded());
    }

    @Override
    PemObject encodePrivateKey() throws JOSEException, IOException {
        return new JcaPKCS8Generator(ecKey.toECPrivateKey(), null).generate();
    }
}
