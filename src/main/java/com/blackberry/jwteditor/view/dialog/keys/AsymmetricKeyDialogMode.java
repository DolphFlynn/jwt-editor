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

package com.blackberry.jwteditor.view.dialog.keys;

import com.blackberry.jwteditor.cryptography.okp.OKPGenerator;
import com.blackberry.jwteditor.utils.PEMUtils;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Provider;
import java.security.Security;
import java.util.UUID;

enum AsymmetricKeyDialogMode {
    EC(KeyType.EC, "keys_new_title_ec", Curve.P_256, Curve.P_256, Curve.SECP256K1, Curve.P_384, Curve.P_521),
    RSA(KeyType.RSA, "keys_new_title_rsa", 2048, 512, 1024, 2048, 3072, 4096),
    OKP(KeyType.OKP, "keys_new_title_okp", Curve.X25519, Curve.X25519, Curve.Ed25519, Curve.X448, Curve.Ed448);

    private final KeyType keyType;
    private final String resourceTitleId;
    private final Object defaultOption;
    private final Object[] keyOptions;

    AsymmetricKeyDialogMode(KeyType keyType, String resourceTitleId, Object defaultOption, Object... keyOptions) {
        this.keyType = keyType;
        this.resourceTitleId = resourceTitleId;
        this.defaultOption = defaultOption;
        this.keyOptions = keyOptions;
    }

    KeyType keyType() {
        return keyType;
    }

    JWK pemToECKey(String pem, String keyId) throws PEMUtils.PemException {
        return switch (this) {
            case EC -> PEMUtils.pemToECKey(pem, keyId);
            case RSA -> PEMUtils.pemToRSAKey(pem, keyId);
            case OKP -> PEMUtils.pemToOctetKeyPair(pem, keyId);
        };
    }

    Object[] keyOptions() {
        return keyOptions;
    }

    Object selectedKeyOption(JWK key) {
        if (key == null) {
            return defaultOption;
        }

        return switch (this) {
            case EC -> ((ECKey) key).getCurve();
            case RSA -> key.size();
            case OKP -> ((OctetKeyPair) key).getCurve();
        };
    }

    JWK generateNewKey(Object parameters) throws KeyStoreException, JOSEException {
        String kid = UUID.randomUUID().toString();

        Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);

        // Force using the BC provider, but fall-back to default if this fails
        KeyStore keyStore = provider == null ? null : KeyStore.getInstance(KeyStore.getDefaultType(), provider);

        return switch (this) {
            case EC -> new ECKeyGenerator((Curve) parameters).keyStore(keyStore).keyID(kid).generate();
            case RSA -> new RSAKeyGenerator((Integer) parameters, true).keyStore(keyStore).keyID(kid).generate();
            case OKP -> new OKPGenerator((Curve) parameters).keyStore(keyStore).keyID(kid).generate();
        };
    }

    String resourceTitleId() {
        return resourceTitleId;
    }
}
