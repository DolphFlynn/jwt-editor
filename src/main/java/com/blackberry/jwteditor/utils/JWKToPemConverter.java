package com.blackberry.jwteditor.utils;

import com.blackberry.jwteditor.utils.PEMUtils.PemException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.IOException;
import java.io.StringWriter;

public abstract class JWKToPemConverter {
    static final String PUBLIC_KEY_PEM_LABEL = "PUBLIC KEY"; //NON-NLS

    private final JWK jwk;

    JWKToPemConverter(JWK jwk) {
        this.jwk = jwk;
    }

    abstract PemObject encodePublicKey() throws JOSEException, IOException;

    abstract PemObject encodePrivateKey() throws JOSEException, IOException;

    public String convertToPem() throws PemException {
        try {
            PemObject pemObject = jwk.isPrivate() ? encodePrivateKey() : encodePublicKey();
            return pemObjectToString(pemObject);
        } catch (JOSEException | IOException e) {
            throw new PemException("PEM conversion error");
        }
    }

    /**
     * Convert a BouncyCastle PemObject to its String representation
     * @param pemObject the PemObject
     * @return a PEM string
     * @throws IOException if conversion fails
     */
    private static String pemObjectToString(PemObject pemObject) throws IOException {
        StringWriter stringWriter = new StringWriter();
        PemWriter pemWriter = new PemWriter(stringWriter);
        pemWriter.writeObject(pemObject);
        pemWriter.close();
        stringWriter.close();
        return stringWriter.toString();
    }
}
