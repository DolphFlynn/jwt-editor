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

package burp;

import burp.api.montoya.core.Annotations;
import burp.api.montoya.core.ByteArray;
import burp.api.montoya.utilities.ByteUtils;
import com.blackberry.jwteditor.model.config.ProxyConfig;
import com.blackberry.jwteditor.model.jose.JOSEObjectPair;
import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.utils.Utils;

import static burp.api.montoya.core.Annotations.annotations;

class AnnotationsModifier {
    private final ProxyConfig proxyConfig;
    private final ByteUtils byteUtils;

    AnnotationsModifier(ProxyConfig proxyConfig, ByteUtils byteUtils) {
        this.proxyConfig = proxyConfig;
        this.byteUtils = byteUtils;
    }

    Annotations updateAnnotationsIfApplicable(Annotations annotations, ByteArray message) {
        Counts counts = countJOSEObjects(message);

        return counts.isZero()
                ? annotations
                : annotations(counts.comment(), proxyConfig.highlightColor().burpColor);
    }

    private Counts countJOSEObjects(ByteArray message) {
        String messageString = byteUtils.convertToString(message.getBytes());

        // Extract and count JWE/JWSs from the message
        int jwsCount = 0;
        int jweCount = 0;

        for (JOSEObjectPair joseObjectPair : Utils.extractJOSEObjects(messageString)) {
            if (joseObjectPair.getModified() instanceof JWS) {
                jwsCount++;
            } else {
                jweCount++;
            }
        }

        return new Counts(proxyConfig, jweCount, jwsCount);
    }

    private record Counts(ProxyConfig proxyConfig, int jweCount, int jwsCount) {
        boolean isZero() {
            return jweCount == 0 && jwsCount == 0;
        }

        String comment() {
            return proxyConfig.comment(jwsCount, jweCount);
        }
    }
}
