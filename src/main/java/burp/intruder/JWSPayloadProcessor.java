package burp.intruder;

import burp.api.montoya.core.ByteArray;
import burp.api.montoya.intruder.PayloadData;
import burp.api.montoya.intruder.PayloadProcessingResult;
import burp.api.montoya.intruder.PayloadProcessor;
import com.blackberry.jwteditor.model.jose.JOSEObject;
import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.model.jose.JWSFactory;
import com.nimbusds.jose.util.Base64URL;
import org.json.JSONObject;

import java.util.Optional;

import static com.blackberry.jwteditor.model.jose.JOSEObjectFinder.parseJOSEObject;

public class JWSPayloadProcessor implements PayloadProcessor {
    private final IntruderConfig intruderConfig;

    public JWSPayloadProcessor(IntruderConfig intruderConfig) {
        this.intruderConfig = intruderConfig;
    }

    @Override
    public PayloadProcessingResult processPayload(PayloadData payloadData) {
        ByteArray baseValue = payloadData.insertionPoint().baseValue();
        Optional<JOSEObject> joseObject = parseJOSEObject(baseValue.toString());

        if (joseObject.isPresent() && (joseObject.get() instanceof JWS jws)) {
            String targetData = intruderConfig.fuzzParameterInPayload() ? jws.getPayload() : jws.getHeader();
            JSONObject targetJson = new JSONObject(targetData);

            if (targetJson.has(intruderConfig.fuzzParameter())) {
                targetJson.put(intruderConfig.fuzzParameter(), payloadData.currentPayload().toString());

                Base64URL updatedHeader = intruderConfig.fuzzParameterInPayload()
                        ? jws.getEncodedHeader()
                        : Base64URL.encode(targetJson.toString());

                Base64URL updatedPayload = intruderConfig.fuzzParameterInPayload()
                        ? Base64URL.encode(targetJson.toString())
                        : jws.getEncodedPayload();

                JWS updatedJws = JWSFactory.jwsFromParts(updatedHeader, updatedPayload, jws.getEncodedSignature());
                baseValue = ByteArray.byteArray(updatedJws.serialize());
            }
        }

        return PayloadProcessingResult.usePayload(baseValue);
    }

    @Override
    public String displayName() {
        return "JWS payload processor";
    }
}
