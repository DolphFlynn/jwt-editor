package com.blackberry.jwteditor.pem;

import com.blackberry.jwteditor.exceptions.PemException;
import com.blackberry.jwteditor.pem.PemKey.NewLineStrategy;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import org.bouncycastle.util.io.pem.PemObject;

import java.io.IOException;

public abstract class JWKToPemConverter {
    static final String PUBLIC_KEY_PEM_LABEL = "PUBLIC KEY"; //NON-NLS

    private final JWK jwk;

    JWKToPemConverter(JWK jwk) {
        this.jwk = jwk;
    }

    abstract PemObject encodePublicKey() throws JOSEException, IOException;

    abstract PemObject encodePrivateKey() throws JOSEException, IOException;

    public String convertToPem(NewLineStrategy newLineStrategy) throws PemException {
        try {
            PemObject pemObject = jwk.isPrivate() ? encodePrivateKey() : encodePublicKey();
            return new PemKey(pemObject).toString(newLineStrategy);
        } catch (JOSEException | IOException e) {
            throw new PemException("PEM conversion error");
        }
    }
}
