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
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPoint;
import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPointProvider;
import com.blackberry.jwteditor.model.jose.JWS;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.blackberry.jwteditor.model.jose.JOSEObjectFinder.extractJOSEObjects;
import static java.util.Collections.emptyList;

public class JWSHeaderInsertionPointProvider implements AuditInsertionPointProvider {
    private final ScannerConfig scannerConfig;

    public JWSHeaderInsertionPointProvider(ScannerConfig scannerConfig) {
        this.scannerConfig = scannerConfig;
    }

    @Override
    public List<AuditInsertionPoint> provideInsertionPoints(HttpRequestResponse httpRequestResponse) {
        if (!scannerConfig.enableHeaderJWSInsertionPointLocation()) {
            return emptyList();
        }

        List<AuditInsertionPoint> insertionPoints = new LinkedList<>();
        HttpRequest baseRequest = httpRequestResponse.request();

        extractJOSEObjects(baseRequest.toString()).stream()
                .map(joseObject -> (joseObject.getModified() instanceof JWS jws) ?
                        new JWSHeaderInsertionPoint(
                                baseRequest,
                                jws,
                                scannerConfig.insertionPointLocationParameterName(),
                                joseObject.getOriginal()
                        ) : null
                )
                .filter(Objects::nonNull)
                .forEach(insertionPoints::add);

        return insertionPoints;
    }
}
