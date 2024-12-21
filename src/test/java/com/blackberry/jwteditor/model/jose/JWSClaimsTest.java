/*
Author : Dolph Flynn

Copyright 2024 Dolph Flynn

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

import com.nimbusds.jose.util.Base64URL;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;

class JWSClaimsTest {

    @Test
    void givenJWS_thenDecodedClaimsIsCorrect() throws ParseException {
        JWS jws = JWSFactory.parse("eyJ0eXAiOiJKV1QiLCJhbGciOiJub25lIn0.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.");

        String decodedClaims = jws.claims().decoded();

        assertThat(decodedClaims).isEqualTo("{\"sub\":\"1234567890\",\"name\":\"John Doe\",\"admin\":true}");
    }

    @Test
    void givenJWS_thenEncodedClaimsIsCorrect() throws ParseException {
        JWS jws = JWSFactory.parse("eyJ0eXAiOiJKV1QiLCJhbGciOiJub25lIn0.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.");

        Base64URL encodedClaims = jws.claims().encoded();

        assertThat(encodedClaims).isEqualTo(new Base64URL("eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9"));
    }

    @Test
    void givenJWS_thenClaimsSerializationCorrect() throws ParseException {
        JWS jws = JWSFactory.parse("eyJ0eXAiOiJKV1QiLCJhbGciOiJub25lIn0.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.");

        String data = jws.claims().toString();

        assertThat(data).isEqualTo("eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9");
    }
}