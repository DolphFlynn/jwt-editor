package burp.intruder;

class IntruderConfigBuilder {
    private final IntruderConfig config;

    private IntruderConfigBuilder() {
        this.config = new IntruderConfig();
    }

    IntruderConfigBuilder withFuzzLocation(FuzzLocation fuzzLocation) {
        config.setFuzzLocation(fuzzLocation);
        return this;
    }

    IntruderConfigBuilder withFuzzParameter(String parameterName) {
        config.setFuzzParameter(parameterName);
        return this;
    }

    IntruderConfig build() {
        return config;
    }

    static IntruderConfigBuilder intruderConfig() {
        return new IntruderConfigBuilder();
    }
}
