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

package burp.api.montoya.http;

import burp.api.montoya.core.ByteArray;
import burp.api.montoya.core.Marker;
import burp.api.montoya.http.message.ContentType;
import burp.api.montoya.http.message.HttpHeader;
import burp.api.montoya.http.message.params.HttpParameter;
import burp.api.montoya.http.message.params.ParsedHttpParameter;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.requests.HttpTransformation;

import java.util.List;

public class FakeHttpRequest implements HttpRequest {
    private final ByteArray request;
    private final HttpService httpService;

    public FakeHttpRequest(ByteArray request) {
        this(null, request);
    }

    public FakeHttpRequest(HttpService httpService, ByteArray request) {
        this.httpService = httpService;
        this.request = request;
    }

    @Override
    public HttpService httpService() {
        return httpService;
    }

    @Override
    public String url() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String method() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String path() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String httpVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<HttpHeader> headers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ContentType contentType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ParsedHttpParameter> parameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteArray body() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String bodyToString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int bodyOffset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Marker> markers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteArray toByteArray() {
        return request;
    }

    @Override
    public HttpRequest copyToTempFile() {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withService(HttpService service) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withPath(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withMethod(String method) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withAddedParameters(List<HttpParameter> parameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withAddedParameters(HttpParameter... parameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withRemovedParameters(List<HttpParameter> parameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withRemovedParameters(HttpParameter... parameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withUpdatedParameters(List<HttpParameter> parameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withUpdatedParameters(HttpParameter... parameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withTransformationApplied(HttpTransformation transformation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withBody(String body) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withBody(ByteArray body) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withAddedHeader(String name, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withAddedHeader(HttpHeader header) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withUpdatedHeader(String name, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withUpdatedHeader(HttpHeader header) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withRemovedHeader(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withRemovedHeader(HttpHeader header) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withMarkers(List<Marker> markers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withMarkers(Marker... markers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpRequest withDefaultHeaders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return request.toString();
    }
}
