package com.blackberry.jwteditor.model.jose;

import com.blackberry.jwteditor.model.jose.exceptions.SigningException;
import com.blackberry.jwteditor.model.keys.Key;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.util.Base64URL;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;

import static java.util.Arrays.stream;

public class JWSFactory {

    public static JWS sign(Key key, Base64URL header, Base64URL payload, JWSHeader signingInfo) throws SigningException {
       return new JWSSigner(key).sign(header, payload, signingInfo);
    }

    /**
     * Parse a JWS from compact serialization
     *
     * @param compactJWS the JWS in compact serialization
     * @return the parsed JWS
     * @throws ParseException if parsing fails
     */
    public static JWS parse(String compactJWS) throws ParseException {
        if (StringUtils.countMatches(compactJWS, ".") != 2) {
            throw new ParseException("Invalid number of encoded sections", 0);
        }

        Base64URL[] parts = com.nimbusds.jose.JOSEObject.split(compactJWS);

        boolean allEmpty = stream(parts).allMatch(part -> part.decodeToString().isEmpty());

        if (allEmpty) {
            throw new ParseException("All sections empty", 0);
        }

        return new JWS(parts[0], parts[1], parts[2]);
    }

    /**
     * Construct a JWS from encoded components
     * @param header the encoded header
     * @param payload the encoded payload
     * @param signature the encoded signature
     */
    public static JWS jwsFromParts(Base64URL header, Base64URL payload, Base64URL signature) {
        return new JWS(header, payload, signature);
    }
}
