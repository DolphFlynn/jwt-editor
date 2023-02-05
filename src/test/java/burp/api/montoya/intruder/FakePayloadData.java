package burp.api.montoya.intruder;

import burp.api.montoya.core.ByteArray;
import burp.api.montoya.core.FakeByteArray;

public class FakePayloadData implements PayloadData {
    private final ByteArray baseValue;
    private final ByteArray currentPayload;

    private FakePayloadData(ByteArray baseValue, ByteArray currentPayload) {
        this.baseValue = baseValue;
        this.currentPayload = currentPayload;
    }

    @Override
    public ByteArray currentPayload() {
        return currentPayload;
    }

    @Override
    public ByteArray originalPayload() {
        return null;
    }

    @Override
    public IntruderInsertionPoint insertionPoint() {
        return () -> baseValue;
    }

    public static class PayloadDataBuilder {
        private ByteArray baseValue;
        private ByteArray currentPayload;

        private PayloadDataBuilder() {
        }

        public PayloadDataBuilder withBaseValue(String baseValue) {
            this.baseValue = new FakeByteArray(baseValue);
            return this;
        }

        public PayloadDataBuilder withCurrentPayload(String currentPayload) {
            this.currentPayload = new FakeByteArray(currentPayload);
            return this;
        }

        public PayloadData build() {
            return new FakePayloadData(baseValue, currentPayload);
        }
    }

    public static PayloadDataBuilder payloadData() {
        return new PayloadDataBuilder();
    }
}
