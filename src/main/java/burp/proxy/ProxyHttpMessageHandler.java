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
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.proxy.*;
import burp.api.montoya.utilities.ByteUtils;
import com.blackberry.jwteditor.model.config.ProxyConfig;

import static burp.api.montoya.proxy.RequestFinalInterceptResult.continueWith;
import static burp.api.montoya.proxy.RequestInitialInterceptResult.followUserRules;
import static burp.api.montoya.proxy.ResponseFinalInterceptResult.continueWith;
import static burp.api.montoya.proxy.ResponseInitialInterceptResult.followUserRules;

public class ProxyHttpMessageHandler implements ProxyHttpRequestHandler, ProxyHttpResponseHandler {
    private final AnnotationsModifier annotationsModifier;

    public ProxyHttpMessageHandler(ProxyConfig proxyConfig, ByteUtils byteUtils) {
        this.annotationsModifier = new AnnotationsModifier(proxyConfig, byteUtils);
    }

    @Override
    public RequestInitialInterceptResult handleReceivedRequest(InterceptedHttpRequest interceptedHttpRequest, Annotations annotations) {
        Annotations modifiedAnnotations = annotationsModifier.updateAnnotationsIfApplicable(annotations, interceptedHttpRequest.asBytes());

        return followUserRules(interceptedHttpRequest, modifiedAnnotations);
    }

    @Override
    public RequestFinalInterceptResult handleRequestToIssue(InterceptedHttpRequest interceptedHttpRequest, Annotations annotations) {
        return continueWith(interceptedHttpRequest, annotations);
    }

    @Override
    public ResponseInitialInterceptResult handleReceivedResponse(InterceptedHttpResponse interceptedHttpResponse, HttpRequest httpRequest, Annotations annotations) {
        Annotations modifiedAnnotations = annotationsModifier.updateAnnotationsIfApplicable(annotations, interceptedHttpResponse.asBytes());

        return followUserRules(interceptedHttpResponse, modifiedAnnotations);
    }

    @Override
    public ResponseFinalInterceptResult handleResponseToReturn(InterceptedHttpResponse interceptedHttpResponse, HttpRequest httpRequest, Annotations annotations) {
        return continueWith(interceptedHttpResponse, annotations);
    }
}
