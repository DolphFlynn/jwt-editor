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

package com.blackberry.jwteditor.model.config;

import com.blackberry.jwteditor.utils.Utils;

import static com.blackberry.jwteditor.model.config.HighlightColor.GREEN;

/**
 * Class containing configuration for Burp's proxy listener
 */
public class BurpConfig {
    public static final HighlightColor DEFAULT_HIGHLIGHT_COLOR = GREEN;

    private static final String BURP_PROXY_COMMENT_TEMPLATE = Utils.getResourceString("burp_proxy_comment");

    private volatile boolean highlightJWT;
    private volatile HighlightColor highlightColor;

    /**
     * Construct proxy config with default options
     */
    public BurpConfig() {
        this(true, null);
    }

    /**
     * Construct proxy config with specified options
     *
     * @param highlightJWT   flag determining whether messages with JWTs passing through Burp are to be highlighted
     * @param highlightColor color to highlight any messages with JWTs
     */
    public BurpConfig(boolean highlightJWT, HighlightColor highlightColor) {
        this.highlightJWT = highlightJWT;
        this.highlightColor = highlightColor == null ? DEFAULT_HIGHLIGHT_COLOR : highlightColor;
    }

    /**
     * Get whether messages with JWTs passing through Burp are to be highlighted
     *
     * @return true if messages with JWTs are to be highlighted
     */
    public boolean highlightJWT() {
        return highlightJWT;
    }

    /**
     * Set whether messages with JWTs passing through Burp are to be highlighted
     *
     * @param highlightJWT flag determining whether messages with JWTs passing through Burp are highlighted
     */
    public void setHighlightJWT(boolean highlightJWT) {
        this.highlightJWT = highlightJWT;
    }

    /**
     * Highlight color used to highlight any messages with JWTs
     *
     * @return color to use to highlight any appropriate messages
     */
    public HighlightColor highlightColor() {
        return highlightColor;
    }

    /**
     * Sets the highlight color used to highlight any messages with JWTs
     *
     * @param highlightColor color to use to highlight any appropriate messages
     */
    public void setHighlightColor(HighlightColor highlightColor) {
        this.highlightColor = highlightColor;
    }

    /**
     * Generates comment for proxy history items to be highlighted
     *
     * @param jwsCount the number of JWS within the message
     * @param jweCount the number of JWE within the message
     * @return the comment string
     */
    public String comment(int jwsCount, int jweCount) {
        return String.format(BURP_PROXY_COMMENT_TEMPLATE, jwsCount, jweCount);
    }
}
