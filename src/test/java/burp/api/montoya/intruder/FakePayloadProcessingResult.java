package burp.api.montoya.intruder;

import burp.api.montoya.core.ByteArray;

import static burp.api.montoya.intruder.PayloadProcessingAction.USE_PAYLOAD;

public record FakePayloadProcessingResult(ByteArray processedPayload) implements PayloadProcessingResult {

    @Override
    public PayloadProcessingAction action() {
        return USE_PAYLOAD;
    }
}
