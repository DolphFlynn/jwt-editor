/*

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

package com.blackberry.jwteditor.view.dialog.operations;

import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.operations.Attacks;
import com.nimbusds.jose.JWSAlgorithm;

import static com.nimbusds.jose.JWSAlgorithm.*;

public class PsychicSignaturePanel extends SingleFixedInputJWSOperation<JWSAlgorithm> {
    private static final JWSAlgorithm[] ALGORITHMS = {ES256, ES384, ES512};

    public PsychicSignaturePanel() {
        super("psychic_signature_signing_dialog_title", "psychic_signature_signing_algorithm", ALGORITHMS);
    }

    @Override
    public JWS performOperation(JWS originalJwt) throws Exception {
        return Attacks.signWithPsychicSignature(originalJwt, selectedInputValue());
    }
}
