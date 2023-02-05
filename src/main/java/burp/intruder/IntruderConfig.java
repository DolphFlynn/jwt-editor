package burp.intruder;

import static burp.intruder.FuzzLocation.PAYLOAD;

public class IntruderConfig {
    private String fuzzParameter;
    private FuzzLocation fuzzLocation;

    public IntruderConfig() {
        this.fuzzParameter = "name";
        this.fuzzLocation = PAYLOAD;
    }

    public String fuzzParameter() {
        return fuzzParameter;
    }

    public void setFuzzParameter(String fuzzParameter) {
        this.fuzzParameter = fuzzParameter;
    }

    public FuzzLocation fuzzLocation() {
        return fuzzLocation;
    }

    public void setFuzzLocation(FuzzLocation fuzzLocation) {
        this.fuzzLocation = fuzzLocation;
    }
}
