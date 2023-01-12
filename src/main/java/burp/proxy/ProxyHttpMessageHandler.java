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

package burp.proxy;

import burp.api.montoya.core.Annotations;
import burp.api.montoya.proxy.http.*;
import burp.api.montoya.utilities.ByteUtils;
import com.blackberry.jwteditor.model.config.ProxyConfig;

public class ProxyHttpMessageHandler implements ProxyRequestHandler, ProxyResponseHandler {
    private final AnnotationsModifier annotationsModifier;

    public ProxyHttpMessageHandler(ProxyConfig proxyConfig, ByteUtils byteUtils) {
        this.annotationsModifier = new AnnotationsModifier(proxyConfig, byteUtils);
    }

    @Override
    public ProxyRequestReceivedAction handleRequestReceived(InterceptedRequest interceptedRequest) {
        Annotations modifiedAnnotations = annotationsModifier.updateAnnotationsIfApplicable(interceptedRequest.annotations(), interceptedRequest.toByteArray());

        return ProxyRequestReceivedAction.continueWith(interceptedRequest, modifiedAnnotations);
    }

    @Override
    public ProxyRequestToBeSentAction handleRequestToBeSent(InterceptedRequest interceptedRequest) {
        return ProxyRequestToBeSentAction.continueWith(interceptedRequest);
    }

    @Override
    public ProxyResponseReceivedAction handleResponseReceived(InterceptedResponse interceptedResponse) {
        Annotations modifiedAnnotations = annotationsModifier.updateAnnotationsIfApplicable(interceptedResponse.annotations(), interceptedResponse.toByteArray());

        return ProxyResponseReceivedAction.continueWith(interceptedResponse, modifiedAnnotations);
    }

    @Override
    public ProxyResponseToBeSentAction handleResponseToBeSent(InterceptedResponse interceptedResponse) {
        return ProxyResponseToBeSentAction.continueWith(interceptedResponse);
    }
}
