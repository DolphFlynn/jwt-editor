/*

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

package com.blackberry.jwteditor.view.dialog.operations;

import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.operations.Attacks;

public class NoneOperation extends SingleFixedInputJWSOperation<String> {
    private static final String[] NONE_ALGORITHM_VALUES = {"none", "None", "NONE", "nOnE"};

    public NoneOperation() {
        super("none_attack_dialog_title", "none_label_algorithm", NONE_ALGORITHM_VALUES);
    }

    @Override
    public JWS performOperation(JWS originalJwt) {
        return Attacks.noneSigning(originalJwt, selectedInputValue());
    }
}
