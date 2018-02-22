package org.knowm.xchange.bitso.service;

import feign.RequestTemplate;
import org.knowm.xchange.service.ParamsDigest;

import javax.crypto.Mac;
import java.math.BigInteger;

public class BitsoDigest extends ParamsDigest {

  private final String clientId;

  private final String apiKey;

  private BitsoDigest(String secretKeyHex, String clientId, String apiKey) {
    super(secretKeyHex, HMAC_SHA_256);
    this.clientId = clientId;
    this.apiKey = apiKey;
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {
    Mac mac256 = getMac();
    mac256.update(requestTemplate.queries().get("nonce").iterator().next().getBytes());
    mac256.update(clientId.getBytes());
    mac256.update(apiKey.getBytes());

    return String.format("%064x", new BigInteger(1, mac256.doFinal())).toUpperCase();
  }

  public static BitsoDigest createInstance(String secretKey, String userName, String apiKey) {
    return secretKey == null ? null : new BitsoDigest(secretKey, userName, apiKey);
  }
}
