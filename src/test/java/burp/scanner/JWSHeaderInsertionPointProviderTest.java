/*
Author : Dolph Flynn

Copyright 2023 Dolph Flynn

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

package burp.scanner;

import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPoint;
import org.junit.jupiter.api.Test;

import java.util.List;

import static burp.api.montoya.http.FakeHttpRequestResponse.requestResponse;
import static org.assertj.core.api.Assertions.assertThat;

class JWSHeaderInsertionPointProviderTest {
    private static final String TEST_JWS1 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    private static final String TEST_JWS2 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.bu3Ajz_67BI824OUeJEuv0qeJjYgVmdGirJ4tAqXRLo";
    private static final String TEST_JWE = "eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiUlNBMV81In0.C2fCo0dzRcmDCFojqFguEAHLVKTWPXZTZjup5VYdONNts84-fy7i6ImZuO9p1l5OqCw8E-44SNp2ZlyvyyIlEPcyYAmDV26IpnFiNKkZZ69gPh0Zjxrw9CSpuZUvZZ5f3b30GchrD8kQwDY_7g6fkaCeb4JAki-2rDN3bCRI3zKGB2RcZ03M_G2zfidO5YKDtBCgdR9G7AqH6xrWGdI1O-0ZtsFcemsYeOjuBdqYlP2MUnmZUc27sOSAknn__mzHwYFyDfqY3ggZ1THGu8l76CDyINcigTm8TEMEhyf31r3bDyUJCjnjJuOFtH5uH6xLClvsBN6IFllUqhWwudSsNQ.MmbtNZ-czmpvjuw2.tbsTUPHl-f8cETFCkTkJ2rqVeVV98s54qyDz1gsmesDKFDk_87iyLfkI736pNRx3A9MFQ6uekUL1tzBbYbkXTB_G3NJra3XTlmT_BqZ_9UR7ponnzwcb7tpU1N2jKHmqeryxmgWOYIRU6oNmTYGNi6Xqb4AYRpe3o0XhLD2CUwXzicPsMO755Y0f6zZmE4BjwR9XMJ8V9OibDeY.F2zlkzkolE_W3PsdAPdrY";
    private final ScannerConfig config = new ScannerConfig();
    private final JWSHeaderInsertionPointProvider insertionPointProvider = new JWSHeaderInsertionPointProvider(config);

    @Test
    void givenEmptyRequest_whenInsertionPointsProvided_thenEmptyListReturned() {
        config.setEnableHeaderJWSInsertionPointLocation(true);
        HttpRequestResponse requestResponse = requestResponse().withRequest("").build();

        List<AuditInsertionPoint> insertionPoints = insertionPointProvider.provideInsertionPoints(requestResponse);

        assertThat(insertionPoints).isEmpty();
    }

    @Test
    void givenRequestWithoutJWT_whenInsertionPointsProvided_thenEmptyListReturned() {
        config.setEnableHeaderJWSInsertionPointLocation(true);
        HttpRequestResponse requestResponse = requestResponse().withRequest("GET / HTTP/1.1\r\nHost: jwt.io\r\n\r\n").build();

        List<AuditInsertionPoint> insertionPoints = insertionPointProvider.provideInsertionPoints(requestResponse);

        assertThat(insertionPoints).isEmpty();
    }

    @Test
    void givenRequestWithJWE_whenInsertionPointsProvided_thenEmptyListReturned() {
        config.setEnableHeaderJWSInsertionPointLocation(true);
        HttpRequestResponse requestResponse = requestResponse().withRequest("GET / HTTP/1.1\r\nHost: jwt.io\r\n\r\n%s".formatted(TEST_JWE)).build();

        List<AuditInsertionPoint> insertionPoints = insertionPointProvider.provideInsertionPoints(requestResponse);

        assertThat(insertionPoints).isEmpty();
    }

    @Test
    void givenRequestWithMultipleJWS_andJWSInsertionPointLocationFalse_whenInsertionPointsProvided_thenEmptyListReturned() {
        config.setEnableHeaderJWSInsertionPointLocation(false);
        String request = "GET / HTTP/1.1\r\nHost: jwt.io\r\nAuthorization: Bearer %s\r\n\r\n".formatted(TEST_JWS1);
        HttpRequestResponse requestResponse = requestResponse().withRequest(request).build();

        List<AuditInsertionPoint> insertionPoints = insertionPointProvider.provideInsertionPoints(requestResponse);

        assertThat(insertionPoints).isEmpty();
    }

    @Test
    void givenRequestWithSingleJWS_whenInsertionPointsProvided_thenSingleInsertionPointReturned() {
        config.setEnableHeaderJWSInsertionPointLocation(true);
        String request = "GET / HTTP/1.1\r\nHost: jwt.io\r\nAuthorization: Bearer %s\r\n\r\n".formatted(TEST_JWS1);
        HttpRequestResponse requestResponse = requestResponse().withRequest(request).build();

        List<AuditInsertionPoint> insertionPoints = insertionPointProvider.provideInsertionPoints(requestResponse);

        assertThat(insertionPoints).hasSize(1);
    }

    @Test
    void givenRequestWithMultipleJWS_whenInsertionPointsProvided_thenMultipleInsertionPointReturned() {
        config.setEnableHeaderJWSInsertionPointLocation(true);
        String request = "GET / HTTP/1.1\r\nHost: jwt.io\r\nAuthorization: Bearer %s\r\n\r\n%s".formatted(TEST_JWS1, TEST_JWS2);
        HttpRequestResponse requestResponse = requestResponse().withRequest(request).build();

        List<AuditInsertionPoint> insertionPoints = insertionPointProvider.provideInsertionPoints(requestResponse);

        assertThat(insertionPoints).hasSize(2);
    }
}