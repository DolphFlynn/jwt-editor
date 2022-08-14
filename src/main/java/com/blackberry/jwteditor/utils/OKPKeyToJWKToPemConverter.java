package com.blackberry.jwteditor.utils;

import com.blackberry.jwteditor.utils.PEMUtils.OKPCurve;
import com.blackberry.jwteditor.utils.PEMUtils.PemException;
import com.nimbusds.jose.jwk.OctetKeyPair;
import org.bouncycastle.asn1.*;
import org.bouncycastle.util.io.pem.PemObject;

import java.io.IOException;

import static com.blackberry.jwteditor.utils.ByteArrayUtils.trimByteArray;

class OKPKeyToJWKToPemConverter extends JWKToPemConverter {
    private static final String PRIVATE_KEY_PEM_LABEL = "PRIVATE KEY"; //NON-NLS

    private final OctetKeyPair octetKeyPair;
    private final OKPCurve curve;

    OKPKeyToJWKToPemConverter(OctetKeyPair octetKeyPair) throws PemException {
        super(octetKeyPair);

        this.octetKeyPair = octetKeyPair;
        this.curve = OKPCurve.fromStandardName(octetKeyPair.getCurve().getStdName());
    }

    @Override
    PemObject encodePublicKey() throws IOException {
        // Build a DER sequence for the public key bytes
        byte[] publicKeyBytes = trimByteArray(octetKeyPair.getX().decode(), curve.keyLength());

        DLSequence algorithmSequence = new DLSequence(curve.objectIdentifier());
        DERBitString bitString = new DERBitString(publicKeyBytes);

        DLSequence outerSequence = new DLSequence(new ASN1Encodable[]{algorithmSequence, bitString});
        return new PemObject(PUBLIC_KEY_PEM_LABEL, outerSequence.getEncoded());
    }

    @Override
    PemObject encodePrivateKey() throws IOException {
        // Build a DER sequence for the private key bytes
        byte[] privateKeyBytes = octetKeyPair.getD().decode();

        DEROctetString innerOctetString = new DEROctetString(privateKeyBytes);
        DEROctetString outerOctetString = new DEROctetString(innerOctetString.getEncoded());
        ASN1Integer integer = new ASN1Integer(0);

        DLSequence algorithmSequence = new DLSequence(curve.objectIdentifier());
        DLSequence outerSequence = new DLSequence(new ASN1Encodable[] {integer, algorithmSequence, outerOctetString});

        return new PemObject(PRIVATE_KEY_PEM_LABEL, outerSequence.getEncoded());
    }
}
