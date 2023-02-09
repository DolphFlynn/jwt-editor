/*
Author : Dolph Flynn

Copyright 2023 Dolph Flynn

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

package burp.proxy;

import burp.api.montoya.proxy.websocket.*;
import burp.api.montoya.utilities.ByteUtils;

public class ProxyWsMessageHandler implements ProxyMessageHandler {
    private final AnnotationsModifier annotationsModifier;

    public ProxyWsMessageHandler(ProxyConfig proxyConfig, ByteUtils byteUtils) {
        this.annotationsModifier = new AnnotationsModifier(proxyConfig, byteUtils);
    }

    @Override
    public TextMessageReceivedAction handleTextMessageReceived(InterceptedTextMessage interceptedTextMessage) {
        annotationsModifier.updateAnnotationsIfApplicable(interceptedTextMessage.annotations(), interceptedTextMessage.payload());

        return TextMessageReceivedAction.continueWith(interceptedTextMessage);
    }

    @Override
    public TextMessageToBeSentAction handleTextMessageToBeSent(InterceptedTextMessage interceptedTextMessage) {
        return TextMessageToBeSentAction.continueWith(interceptedTextMessage);
    }

    @Override
    public BinaryMessageReceivedAction handleBinaryMessageReceived(InterceptedBinaryMessage interceptedBinaryMessage) {
        annotationsModifier.updateAnnotationsIfApplicable(interceptedBinaryMessage.annotations(), interceptedBinaryMessage.payload());

        return BinaryMessageReceivedAction.continueWith(interceptedBinaryMessage);
    }

    @Override
    public BinaryMessageToBeSentAction handleBinaryMessageToBeSent(InterceptedBinaryMessage interceptedBinaryMessage) {
        return BinaryMessageToBeSentAction.continueWith(interceptedBinaryMessage);
    }
}
