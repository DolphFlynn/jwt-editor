package com.blackberry.jwteditor.pem;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.util.io.pem.PemObject;

import java.io.IOException;

class RSAKeyToJWKToPemConverter extends JWKToPemConverter {
    private static final String RSA_PRIVATE_KEY_PEM_LABEL = "RSA PRIVATE KEY"; //NON-NLS

    private final RSAKey rsaKey;

    RSAKeyToJWKToPemConverter(RSAKey rsaKey) {
        super(rsaKey);
        this.rsaKey = rsaKey;
    }

    @Override
    PemObject encodePublicKey() throws JOSEException {
        return new PemObject(PUBLIC_KEY_PEM_LABEL, rsaKey.toPublicKey().getEncoded());
    }

    @Override
    PemObject encodePrivateKey() throws JOSEException, IOException {
        PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(rsaKey.toRSAPrivateKey().getEncoded());
        ASN1Encodable asn1Encodable = privateKeyInfo.parsePrivateKey();
        ASN1Primitive asn1Primitive = asn1Encodable.toASN1Primitive();
        byte[] privateKeyPKCS8 = asn1Primitive.getEncoded(ASN1Encoding.DER);

        return new PemObject(RSA_PRIVATE_KEY_PEM_LABEL, privateKeyPKCS8);
    }
}
