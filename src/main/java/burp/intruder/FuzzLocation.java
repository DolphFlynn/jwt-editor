package burp.intruder;

public enum FuzzLocation {
    HEADER, PAYLOAD;

    @Override
    public String toString() {
        return switch (this) {
            case HEADER -> "Header";
            case PAYLOAD -> "Payload";
        };
    }
}
