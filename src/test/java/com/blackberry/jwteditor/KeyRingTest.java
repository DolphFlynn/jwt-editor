package com.blackberry.jwteditor;

import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.model.jose.JWSFactory;
import com.blackberry.jwteditor.model.keys.Key;
import com.blackberry.jwteditor.model.keys.KeyRing;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Optional;

import static com.blackberry.jwteditor.KeysRingBuilder.keyRing;
import static com.blackberry.jwteditor.PemData.*;
import static org.assertj.core.api.Assertions.assertThat;

class KeyRingTest {
    private static final String TEST_JWS = "eyJraWQiOiI0Yzc1NmNjYi0wMDgwLTQyZDMtYmM1OC03NjkyZmQyYWIzYzUiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJwb3J0c3dpZ2dlciIsInN1YiI6IndpZW5lciIsImV4cCI6MTY1NzkwNzkyMX0.FI0wtDMzcoAstABg4Zu1OovB4N78tS1JGDEiaeLLvtz8HahxNSp0pkJ7DJV_-2AUzkImVZJntmA2W_uAjh7v6A";

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
}