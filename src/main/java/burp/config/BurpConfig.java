/*
Author : Dolph Flynn

Copyright 2022 Dolph Flynn

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package burp.config;

import burp.intruder.IntruderConfig;
import burp.proxy.HighlightColor;
import burp.proxy.ProxyConfig;

public class BurpConfig {
    private final ProxyConfig proxyConfig = new ProxyConfig();
    private final IntruderConfig intruderConfig = new IntruderConfig();

    /**
     * Construct Burp config with default options
     */
    BurpConfig() {
        this(true, null);
    }

    /**
     * Construct Burp config with specified options
     *
     * @param highlightJWT   flag determining whether messages with JWTs passing through Burp are to be highlighted
     * @param highlightColor color to highlight any messages with JWTs
     */
    public BurpConfig(boolean highlightJWT, HighlightColor highlightColor) {
        proxyConfig.setHighlightJWT(highlightJWT);
        proxyConfig.setHighlightColor(highlightColor);
    }

    public ProxyConfig proxyConfig() {
        return proxyConfig;
    }

    public IntruderConfig intruderConfig() {
        return intruderConfig;
    }
}
