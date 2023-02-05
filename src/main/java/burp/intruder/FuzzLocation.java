package burp.intruder;

import static java.util.Arrays.stream;

public enum FuzzLocation {
    HEADER, PAYLOAD;

    @Override
    public String toString() {
        return switch (this) {
            case HEADER -> "Header";
            case PAYLOAD -> "Payload";
        };
    }

    public static FuzzLocation from(String displayName) {
        return stream(values())
                .filter(fuzzLocation -> fuzzLocation.toString().equalsIgnoreCase(displayName))
                .findFirst()
                .orElse(null);
    }
}
