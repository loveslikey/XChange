package org.knowm.xchange.utils;

import feign.RequestTemplate;
import org.knowm.xchange.HttpClientBuilderUtil;
import org.knowm.xchange.service.ParamsDigest;

public class BasicAuthCredentials extends ParamsDigest {

    private final String username, password;

    public BasicAuthCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String digestParams(RequestTemplate requestTemplate) {
        // ignore restInvocation, just need username & password
        return HttpClientBuilderUtil.digestForBasicAuth(username, password);
    }
}
