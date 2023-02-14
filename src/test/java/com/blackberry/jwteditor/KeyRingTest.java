package com.blackberry.jwteditor;

import com.blackberry.jwteditor.model.jose.JWE;
import com.blackberry.jwteditor.model.jose.JWEFactory;
import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.model.jose.JWSFactory;
import com.blackberry.jwteditor.model.keys.Key;
import com.blackberry.jwteditor.model.keys.KeyRing;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Optional;

import static com.blackberry.jwteditor.KeysRingBuilder.keyRing;
import static data.PemData.*;
import static org.assertj.core.api.Assertions.assertThat;

class KeyRingTest {
    private static final String TEST_JWS = "eyJraWQiOiI0Yzc1NmNjYi0wMDgwLTQyZDMtYmM1OC03NjkyZmQyYWIzYzUiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJwb3J0c3dpZ2dlciIsInN1YiI6IndpZW5lciIsImV4cCI6MTY1NzkwNzkyMX0.FI0wtDMzcoAstABg4Zu1OovB4N78tS1JGDEiaeLLvtz8HahxNSp0pkJ7DJV_-2AUzkImVZJntmA2W_uAjh7v6A";
    private static final String TEST_JWE = "eyJlcGsiOnsia3R5IjoiRUMiLCJjcnYiOiJQLTI1NiIsIngiOiJWcUtNak56OXpjcl9zaWJrNnhqcmJwUFZDUzk1UEdSU0dHYkVpMG5qWldBIiwieSI6IlFWMWpxSXBTZTVQSU80eVpTZTBKcnR1bkx4QTJ2ZHRnSnFfV18wNTNweDQifSwiZW5jIjoiQTEyOEdDTSIsImFsZyI6IkVDREgtRVMifQ..00TznoP0a4LjQTDR.vAwRkLB7GbK8ptyhfXqDwn_Hm8hbP4HBdmrvjvpxxRlxJxKcCoZngO9r_eb11vmi1oUhCVdFwzH_7-eSrzjv7ouoyVmv_ujMWweNvyKQdxJBmNlU8h3eR_TWjOLFetGhOhlnWtp95o4w4lNT4YyMGk66Ki6aG1UDLgK-WwLLcFbOLX7HPEk2ml8eQCnuqbjlCk7cWVtvoJ4xyLrLlEjJ3U_yXy8KPu0oAPI-Nxl3Nj1vG6KT_TqYaRHydr2wrT5A7cMOse6bwXPS_8uCp12EY8CQGkh7d9y63Fte7j8Bqmu_yJhKAoSkyoEOBTAvvzd-EhoLMBlWm3ouULg3MIt9gL-QZi8npXSrgiBCTw4Z2exew6WQIlV7JWGphcZYePFZHUvWf4Sd59fUvPaztuzhyHS3XaSwO_fOSPeYnX3a9wCH7Crcmldi-kiirKkh4QVSPUlCQyC0ihHot1PVwfUQDWXLAL4g0FJGZApkgAM9YBLkJpUQeT-qGjAhtoEGKQH6WTKOGaP65ZBORChDJThrFARYbJI1mqdc3gr5eBd1_FQsfxG5_Desg5AdpjIALyfYq8X39UmCLnx2tF4Yy6nIe79lIxqOOZA2XKYStrGmQORoOVQ8r91UbmC6tYJStPmVUWzXG47tUWLUBEktKtCl.9pZsA42F2w79Mi0AK84wVg";

    @Test
    void givenKeyRingEmpty_whenVerifyJWS_thenEmptyReturned() throws ParseException {
        KeyRing keyRing = keyRing().build();
        JWS jws = JWSFactory.parse(TEST_JWS);

        Optional<Key> keyOptional = keyRing.findVerifyingKey(jws);

        assertThat(keyOptional).isEmpty();
    }

    @Test
    void givenKeyRingWithWrongKeys_whenVerifyJWS_thenEmptyReturned() throws ParseException {
        KeyRing keyRing = keyRing()
                .withOKPKey(X25519Private)
                .withOKPKey(X25519Public)
                .withRSAKey(RSA1024Private)
                .withRSAKey(RSA1024Public)
                .build();
        JWS jws = JWSFactory.parse(TEST_JWS);

        Optional<Key> keyOptional = keyRing.findVerifyingKey(jws);

        assertThat(keyOptional).isEmpty();
    }

    @Test
    void givenKeyRingWithCorrectKey_whenVerifyJWS_thenKeyReturned() throws ParseException {
        KeyRing keyRing = keyRing()
                .withOKPKey(X25519Private)
                .withOKPKey(X25519Public)
                .withECKey(PRIME256v1PrivateSEC1)
                .withRSAKey(RSA1024Private)
                .withRSAKey(RSA1024Public)
                .build();
        JWS jws = JWSFactory.parse(TEST_JWS);

        Optional<Key> keyOptional = keyRing.findVerifyingKey(jws);

        assertThat(keyOptional).isPresent();
        assertThat(keyOptional.get().getID()).isEqualTo("3");
    }

    @Test
    void givenKeyRingEmpty_whenDecryptJWE_thenEmptyReturned() throws ParseException {
        KeyRing keyRing = keyRing().build();
        JWE jwe = JWEFactory.parse(TEST_JWE);

        Optional<JWS> jwsOptional = keyRing.attemptDecryption(jwe);

        assertThat(jwsOptional).isEmpty();
    }

    @Test
    void givenKeyRingWithWrongKeys_whenDecryptJWE_thenEmptyReturned() throws ParseException {
        KeyRing keyRing = keyRing()
                .withOKPKey(X25519Private)
                .withOKPKey(X25519Public)
                .withRSAKey(RSA1024Private)
                .withRSAKey(RSA1024Public)
                .build();
        JWE jwe = JWEFactory.parse(TEST_JWE);

        Optional<JWS> jwsOptional = keyRing.attemptDecryption(jwe);

        assertThat(jwsOptional).isEmpty();
    }

    @Test
    void givenKeyRingWithCorrectKey_whenDecryptJWE_thenJWSReturned() throws ParseException {
        KeyRing keyRing = keyRing()
                .withOKPKey(X25519Private)
                .withOKPKey(X25519Public)
                .withECKey(PRIME256v1PrivateSEC1)
                .withRSAKey(RSA1024Private)
                .withRSAKey(RSA1024Public)
                .build();
        JWE jwe = JWEFactory.parse(TEST_JWE);

        Optional<JWS> jwsOptional = keyRing.attemptDecryption(jwe);

        assertThat(jwsOptional).isPresent();
        assertThat(jwsOptional.get().serialize()).isEqualTo("eyJraWQiOiI1NDI2NjMxNC0zN2FiLTQ0ZDYtYWMwYy01ZTI4OGUzNGYzZTYiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJwb3J0c3dpZ2dlciIsInN1YiI6IndpZW5lciIsImV4cCI6MTY1OTM2NzkzMX0.vT_A4MkQAzvBacKxOmXy52EcCte-w_dupPdh3az8MywUY90e-gXqYgPHTwQo9Mr9cCI5qrXOSw9kChh7U0NToJLKeeI-VqN9dYwMpsUTIFYsh3U7Ey0zS27Y6wW4j3un-AhPrz06YKtuzQ4pZ8ImIa4Z0AHCfrg2UYQ62KrkX-tZAET4x-8pFy5kX69ZFKjQiXWEGGebxsiYGN88_Un1IIjgI62jvSvhuY5KFXfRdQ1tJot3OM4iBhaFbx_5e3BsGT8nsEkDtiQQ_nHXf3yx09dbTBBUjmtnM4zEoLp2kg1dfYoaWte4H9Gh6_dzDxAxVn-ns6eOPuFx77jHQwaewQ");
    }
}