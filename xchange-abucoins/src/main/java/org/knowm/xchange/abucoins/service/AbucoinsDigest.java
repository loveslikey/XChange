package org.knowm.xchange.abucoins.service;

import feign.RequestTemplate;
import org.knowm.xchange.abucoins.Abucoins;
import org.knowm.xchange.abucoins.dto.AbucoinsServerTime;
import org.knowm.xchange.service.ParamsDigest;
import org.knowm.xchange.utils.Params;

import javax.crypto.Mac;
import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;


/**
 * @author bryant_harris
 */
public class AbucoinsDigest extends ParamsDigest {
    long timeDiffFromServer = 0;

    private AbucoinsDigest(Abucoins abucoins, String secretKeyBase64) throws IllegalArgumentException {
        super(secretKeyBase64 == null ? null : Base64.getDecoder().decode(secretKeyBase64), HMAC_SHA_256);

        try {
            AbucoinsServerTime serverTime = abucoins.getTime();

            long ourTime = System.currentTimeMillis() / 1000L;
            timeDiffFromServer = serverTime.getEpoch() - ourTime;
        } catch (IOException e) {
            throw new RuntimeException("Unable to determine server time");
        }
    }

    static AbucoinsDigest instance;

    public static AbucoinsDigest createInstance(Abucoins abucoins, String secretKeyBase64) {
        if (instance == null)
            instance = secretKeyBase64 == null ? null : new AbucoinsDigest(abucoins, secretKeyBase64);
        return instance;
    }

    @Override
    public String digestParams(RequestTemplate requestTemplate) {
        String timestamp = requestTemplate.headers().get("AC-ACCESS-TIMESTAMP").iterator().next();
        String method = requestTemplate.method();
        String path = requestTemplate.url();
        Params params = Params.of();
        Map<String, Collection<String>> queries = requestTemplate.queries();
        queries.entrySet().stream().forEach(entry -> {
            params.add(entry.getKey(), entry.getValue());
        });
        String queryParameters = params.asQueryString();
        String body = requestTemplate.bodyTemplate();
        body = body == null ? "" : body;

        String queryArgs = timestamp + method + path + (queryParameters + body);
        Mac shaMac = getMac();
        final byte[] macData = shaMac.doFinal(queryArgs.getBytes());
        return Base64.getEncoder().encodeToString(macData);
    }

    public String timestamp() {
        return String.valueOf((System.currentTimeMillis() / 1000) + timeDiffFromServer);
    }
}
