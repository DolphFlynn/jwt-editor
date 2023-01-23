package com.blackberry.jwteditor.model.jose;

import com.blackberry.jwteditor.model.jose.exceptions.SigningException;
import com.blackberry.jwteditor.model.keys.Key;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.util.Base64URL;

public class JWSFactory {

    public static JWS sign(Key key, Base64URL header, Base64URL payload, JWSHeader signingInfo) throws SigningException {
       return new JWSSigner(key).sign(header, payload, signingInfo);
    }
}
