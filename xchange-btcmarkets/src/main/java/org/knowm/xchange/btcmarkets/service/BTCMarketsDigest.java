package org.knowm.xchange.btcmarkets.service;

import feign.RequestTemplate;
import net.iharder.Base64;
import org.knowm.xchange.service.ParamsDigest;

import javax.crypto.Mac;

public class BTCMarketsDigest extends ParamsDigest {

  public BTCMarketsDigest(String secretKey) {
    super(decodeBase64(secretKey), HMAC_SHA_512);
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {
    final String nonce = requestTemplate.queries().get("timestamp").iterator().next();
    return digest(requestTemplate.url(), nonce, requestTemplate.bodyTemplate());
  }

  String digest(String url, String nonce, String requestBody) {
    Mac mac256 = getMac();
    if (!url.startsWith("/")) {
      url = "/" + url;
    }
    mac256.update(url.getBytes());
    mac256.update("\n".getBytes());
    mac256.update(nonce.getBytes());
    mac256.update("\n".getBytes());
    if (requestBody != null && !requestBody.isEmpty()) {
      mac256.update(requestBody.getBytes());
    }

    return Base64.encodeBytes(mac256.doFinal());
  }
}
