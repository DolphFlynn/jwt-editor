package burp;

import burp.api.montoya.core.Annotations;
import burp.api.montoya.core.ByteArray;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.proxy.*;
import burp.api.montoya.utilities.ByteUtils;
import com.blackberry.jwteditor.model.config.ProxyConfig;
import com.blackberry.jwteditor.model.jose.JOSEObjectPair;
import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.utils.Utils;

import static burp.api.montoya.core.Annotations.annotations;
import static burp.api.montoya.proxy.RequestFinalInterceptResult.continueWith;
import static burp.api.montoya.proxy.RequestInitialInterceptResult.followUserRules;
import static burp.api.montoya.proxy.ResponseFinalInterceptResult.continueWith;
import static burp.api.montoya.proxy.ResponseInitialInterceptResult.followUserRules;

class ProxyMessageHandler implements ProxyHttpRequestHandler, ProxyHttpResponseHandler {
    private final ProxyConfig proxyConfig;
    private final ByteUtils byteUtils;

    ProxyMessageHandler(ProxyConfig proxyConfig, ByteUtils byteUtils) {
        this.proxyConfig = proxyConfig;
        this.byteUtils = byteUtils;
    }

    @Override
    public RequestInitialInterceptResult handleReceivedRequest(InterceptedHttpRequest interceptedHttpRequest, Annotations annotations) {
        Counts counts = countJOSEObjects(interceptedHttpRequest.asBytes());

        Annotations modifiedAnnotations = counts.isZero()
                ? annotations
                : annotations(counts.comment(), proxyConfig.highlightColor().burpColor);

        return followUserRules(interceptedHttpRequest, modifiedAnnotations);
    }

    @Override
    public RequestFinalInterceptResult handleRequestToIssue(InterceptedHttpRequest interceptedHttpRequest, Annotations annotations) {
        return continueWith(interceptedHttpRequest, annotations);
    }

    @Override
    public ResponseInitialInterceptResult handleReceivedResponse(InterceptedHttpResponse interceptedHttpResponse, HttpRequest httpRequest, Annotations annotations) {
        Counts counts = countJOSEObjects(interceptedHttpResponse.asBytes());

        Annotations modifiedAnnotations = counts.isZero()
                ? annotations
                : annotations(counts.comment(), proxyConfig.highlightColor().burpColor);

        return followUserRules(interceptedHttpResponse, modifiedAnnotations);
    }

    @Override
    public ResponseFinalInterceptResult handleResponseToReturn(InterceptedHttpResponse interceptedHttpResponse, HttpRequest httpRequest, Annotations annotations) {
        return continueWith(interceptedHttpResponse, annotations);
    }

    private Counts countJOSEObjects(ByteArray message) {
        String messageString = byteUtils.convertToString(message.getBytes());

        // Extract and count JWE/JWSs from the HTTP message
        int jwsCount = 0;
        int jweCount = 0;

        for (JOSEObjectPair joseObjectPair : Utils.extractJOSEObjects(messageString)) {
            if (joseObjectPair.getModified() instanceof JWS) {
                jwsCount++;
            } else {
                jweCount++;
            }
        }

        return new Counts(proxyConfig, jweCount, jwsCount);
    }

    private record Counts(ProxyConfig proxyConfig, int jweCount, int jwsCount) {
        boolean isZero() {
            return jweCount == 0 && jwsCount == 0;
        }

        String comment() {
            return proxyConfig.comment(jwsCount, jweCount);
        }
    }
}
