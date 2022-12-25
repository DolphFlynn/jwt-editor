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

package com.blackberry.jwteditor.view;

import burp.api.montoya.http.HttpService;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.ui.editor.extension.ExtensionHttpRequestEditor;
import com.blackberry.jwteditor.presenter.PresenterStore;

import static burp.api.montoya.internal.ObjectFactoryLocator.FACTORY;

public class RequestEditorView extends EditorView implements ExtensionHttpRequestEditor {
    private volatile HttpService httpService;

    public RequestEditorView(PresenterStore presenters, RstaFactory rstaFactory, boolean editable) {
        super(presenters, rstaFactory, editable);
    }

    @Override
    public void setHttpRequestResponse(HttpRequestResponse requestResponse) {
        HttpRequest httpRequest = requestResponse.httpRequest();
        httpService = httpRequest.httpService();
        presenter.setMessage(httpRequest.asBytes().toString());
    }

    @Override
    public boolean isEnabledFor(HttpRequestResponse requestResponse) {
        String content = requestResponse.httpRequest().asBytes().toString();
        return presenter.isEnabled(content);
    }

    @Override
    public HttpRequest getHttpRequest() {
        return FACTORY.httpRequest(httpService, presenter.getMessage());
    }
}
