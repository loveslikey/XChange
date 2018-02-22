package org.knowm.xchange.gatecoin.service;

import feign.RequestTemplate;
import net.iharder.Base64;
import org.knowm.xchange.service.ParamsDigest;

import javax.crypto.Mac;

public class GatecoinDigest extends ParamsDigest {

  private GatecoinDigest(String secretKeyBase64) {
    super(secretKeyBase64, HMAC_SHA_256);
  }

  public static GatecoinDigest createInstance(String secretKeyBase64) {
    return secretKeyBase64 == null ? null : new GatecoinDigest(secretKeyBase64);
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {
    return digest(requestTemplate.method(), requestTemplate.url()+requestTemplate.queryLine(), requestTemplate.headers().get("Content-Type").iterator().next(),
        requestTemplate.queries().get("API_REQUEST_DATE").iterator().next().toString());
  }

  String digest(String httpMethod, String invocationUrl, String reqContentType, String now) {
    Mac mac256 = getMac();
    mac256.update(httpMethod.toLowerCase().getBytes());
    mac256.update(invocationUrl.toLowerCase().getBytes());
    if (!"GET".equals(httpMethod)) {
      mac256.update(reqContentType.toLowerCase().getBytes());
    }
    mac256.update(now.toLowerCase().getBytes());
    return Base64.encodeBytes(mac256.doFinal());
  }
}
