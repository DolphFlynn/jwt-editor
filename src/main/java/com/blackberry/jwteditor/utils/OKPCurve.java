package com.blackberry.jwteditor.utils;

import com.blackberry.jwteditor.exceptions.PemException;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.util.Base64URL;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed448PrivateKeyParameters;
import org.bouncycastle.crypto.params.X25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.X448PrivateKeyParameters;

import static com.blackberry.jwteditor.utils.ByteArrayUtils.trimByteArray;
import static java.util.Arrays.stream;

public enum OKPCurve {
    X25519(Curve.X25519, 32, "1.3.101.110"),
    X448(Curve.X448, 56, "1.3.101.111"),
    ED25519(Curve.Ed25519, 32, "1.3.101.112"),
    ED448(Curve.Ed448, 57, "1.3.101.113");

    private final Curve curve;
    private final int keyLength;
    private final ASN1ObjectIdentifier oid;

    OKPCurve(Curve curve, int keyLength, String oid) {
        this.curve = curve;
        this.keyLength = keyLength;
        this.oid = new ASN1ObjectIdentifier(oid);
    }

    public OctetKeyPair buildPrivateKeyFrom(byte[] octets) throws PemException {
        if (octets.length != keyLength) {
            throw new PemException("Invalid key length");
        }

        Base64URL x = Base64URL.encode(extractPublicKeyFromPrivateKey(octets));
        Base64URL d = Base64URL.encode(octets);

        return new OctetKeyPair.Builder(curve, x).d(d).build();
    }

    public OctetKeyPair buildPublicKeyFrom(byte[] octets) {
        Base64URL x = Base64URL.encode(trimByteArray(octets, keyLength));
        return new OctetKeyPair.Builder(curve, x).build();
    }

    public ASN1Encodable objectIdentifier() {
        return oid;
    }

    public int keyLength() {
        return keyLength;
    }

    private byte[] extractPublicKeyFromPrivateKey(byte[] octets) {
        return switch (this) {
            case X25519 -> new X25519PrivateKeyParameters(octets).generatePublicKey().getEncoded();
            case X448 -> new X448PrivateKeyParameters(octets).generatePublicKey().getEncoded();
            case ED25519 -> new Ed25519PrivateKeyParameters(octets).generatePublicKey().getEncoded();
            case ED448 -> new Ed448PrivateKeyParameters(octets).generatePublicKey().getEncoded();
        };
    }

    public static OKPCurve fromStandardName(String name) throws PemException {
        return stream(values())
                .filter(curve -> curve.curve.getStdName().equals(name))
                .findFirst()
                .orElseThrow(() -> new PemException("Invalid curve with name: " + name));
    }

    public static OKPCurve fromAlgorithmId(String oid) throws PemException {
        return stream(values())
                .filter(curve -> curve.oid.getId().equals(oid))
                .findFirst()
                .orElseThrow(() -> new PemException("Invalid curve with OID: " + oid));
    }
}
