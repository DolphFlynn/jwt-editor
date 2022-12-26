/*
Author : Dolph Flynn

Copyright 2022 Dolph Flynn

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.blackberry.jwteditor.model.jose;

import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSObject;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JOSEObjectFinder {
    public static final String BASE64_REGEX = "[A-Za-z0-9-_]";

    private static final String JWS_REGEX = "e%s*\\.%s+\\.%s*".formatted(BASE64_REGEX, BASE64_REGEX, BASE64_REGEX);
    private static final String JWE_REGEX = "e%s*\\.%s*\\.%s+\\.%s+\\.%s+".formatted(BASE64_REGEX, BASE64_REGEX, BASE64_REGEX, BASE64_REGEX, BASE64_REGEX);
    private static final Pattern JOSE_OBJECT_PATTERN = Pattern.compile("(%s)|(%s)".formatted(JWE_REGEX, JWS_REGEX));

    /**
     * Extract a list of JOSEObjectPairs from a block of text that may contain JWE/JWS in compact form
     *
     * @param text text block
     * @return list of JOSEObjectPairs
     */
    public static List<JOSEObjectPair> extractJOSEObjects(String text) {
        List<JOSEObjectPair> joseObjects = new ArrayList<>();

        Set<String> candidates = findCandidateJoseObjectsWithin(text);

        for (String candidate : candidates) {
            // Try to parse each as both a JWE and a JWS
            Optional<JWE> jwe = parseJWE(candidate);
            Optional<JWS> jws = jwe.isEmpty() ? parseJWS(candidate) : Optional.empty();

            jwe.ifPresent(value -> joseObjects.add(new JOSEObjectPair(candidate, value)));
            jws.ifPresent(value -> joseObjects.add(new JOSEObjectPair(candidate, value)));
        }

        return joseObjects;
    }

    private static Set<String> findCandidateJoseObjectsWithin(String text) {
        Matcher m = JOSE_OBJECT_PATTERN.matcher(text);
        Set<String> strings = new HashSet<>();

        while (m.find()) {
            String token = m.group();
            strings.add(token);
        }

        return strings;
    }

    private static Optional<JWE> parseJWE(String candidate) {
        try {
            JWEObject.parse(candidate);
            return Optional.of(JWE.parse(candidate));
        } catch (ParseException e) {
            return Optional.empty();
        }
    }

    private static Optional<JWS> parseJWS(String candidate) {
        try {
            JWSObject.parse(candidate);
            return Optional.of(JWS.parse(candidate));
        } catch (ParseException e) {
            return Optional.empty();
        }
    }
}
