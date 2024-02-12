package burp.intruder;

import burp.api.montoya.core.ByteArray;
import burp.api.montoya.intruder.PayloadData;
import burp.api.montoya.intruder.PayloadProcessingResult;
import burp.api.montoya.intruder.PayloadProcessor;
import burp.api.montoya.logging.Logging;

import com.blackberry.jwteditor.exceptions.SigningException;
import com.blackberry.jwteditor.model.jose.JOSEObject;
import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.model.jose.JWSFactory;
import com.blackberry.jwteditor.model.keys.Key;
import com.blackberry.jwteditor.model.keys.KeysModel;
import com.nimbusds.jose.util.Base64URL;
import org.json.JSONObject;

import java.util.Optional;

import static burp.intruder.FuzzLocation.PAYLOAD;
import static com.blackberry.jwteditor.model.jose.JOSEObjectFinder.parseJOSEObject;
import static com.blackberry.jwteditor.utils.Constants.INTRUDER_NO_SIGNING_KEY_ID_LABEL;

public class JWSPayloadProcessor implements PayloadProcessor {
    Optional<Logging> logging;
    private final IntruderConfig intruderConfig;
    private final Optional<KeysModel> keysModel;

    public JWSPayloadProcessor(IntruderConfig intruderConfig, Optional<Logging> logging, Optional<KeysModel> keysModel) {
        this.logging = logging;
        this.intruderConfig = intruderConfig;
        this.keysModel = keysModel;
    }

    @Override
    public PayloadProcessingResult processPayload(PayloadData payloadData) {
        ByteArray baseValue = payloadData.insertionPoint().baseValue();
        Optional<JOSEObject> joseObject = parseJOSEObject(baseValue.toString());

        if (joseObject.isPresent() && (joseObject.get() instanceof JWS jws)) {
            boolean fuzzPayload = intruderConfig.fuzzLocation() == PAYLOAD;
            String targetData = fuzzPayload ? jws.getPayload() : jws.getHeader();
            JSONObject targetJson = new JSONObject(targetData);

            if (targetJson.has(intruderConfig.fuzzParameter())) {
                targetJson.put(intruderConfig.fuzzParameter(), payloadData.currentPayload().toString());

                Base64URL updatedHeader = fuzzPayload
                        ? jws.getEncodedHeader()
                        : Base64URL.encode(targetJson.toString());

                Base64URL updatedPayload = fuzzPayload
                        ? Base64URL.encode(targetJson.toString())
                        : jws.getEncodedPayload();

                JWS updatedJws = this.createJWS(updatedHeader, updatedPayload, jws.getEncodedSignature());
                baseValue = ByteArray.byteArray(updatedJws.serialize());
            }
        }

        return PayloadProcessingResult.usePayload(baseValue);
    }

    @Override
    public String displayName() {
        return "JWS payload processor";
    }

    private Optional<Key> loadKey() {
        if (keysModel.isPresent()) {
            String keyId = intruderConfig.signingKeyId();
            // only try to load key if the input value is non-empty
            if (keyId.trim().length() > 0 && keyId != INTRUDER_NO_SIGNING_KEY_ID_LABEL) {
                Key key = keysModel.get().getKey(intruderConfig.signingKeyId());
                if (key != null) {
                    return Optional.of(key);
                } else {
                    this.logging.ifPresent(log -> log.logToError("Key with ID " + intruderConfig.signingKeyId() + " not found"));
                }
            }
        } else {
            this.logging.ifPresent(log -> log.logToOutput("No keysModel available. Will not be able to sign JWS"));
        }

        return Optional.empty();
    }

    // Creates a JWS object from the given attributes. Signs the JWS if possible (i.e., available key selected in Intruder settings)
    private JWS createJWS(Base64URL header, Base64URL payload, Base64URL originalSignature) {
        return this.loadKey().flatMap(key -> {
            Optional<JWS> result = Optional.empty();

            try {
                result = Optional.of(JWSFactory.sign(key, key.getSigningAlgorithms()[0], header, payload));
            } catch (SigningException ex) {
                this.logging.ifPresent(log -> log.logToError("Failed to sign JWS: " + ex));
            }

            return result;
        }).orElseGet(() -> JWSFactory.jwsFromParts(header, payload, originalSignature));
    }
}
