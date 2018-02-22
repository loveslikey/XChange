package org.knowm.xchange.bitstamp.service;

import feign.RequestTemplate;
import org.knowm.xchange.service.ParamsDigest;

import javax.crypto.Mac;
import java.math.BigInteger;

/**
 * @author Benedikt Bünz
 */
public class BitstampDigest extends ParamsDigest {

  private final String clientId;
  private final String apiKey;

  /**
   * Constructor
   *
   * @param secretKeyBase64
   * @param clientId
   * @param apiKey @throws IllegalArgumentException if key is invalid (cannot be base-64-decoded or the decoded key is invalid).
   */
  private BitstampDigest(String secretKeyBase64, String clientId, String apiKey) {

    super(secretKeyBase64, HMAC_SHA_256);
    this.clientId = clientId;
    this.apiKey = apiKey;
  }

  public static BitstampDigest createInstance(String secretKeyBase64, String clientId, String apiKey) {

    return secretKeyBase64 == null ? null : new BitstampDigest(secretKeyBase64, clientId, apiKey);
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {

    Mac mac256 = getMac();
    mac256.update(requestTemplate.queries().get("nonce").iterator().next().getBytes());
    mac256.update(clientId.getBytes());
    mac256.update(apiKey.getBytes());

    return String.format("%064x", new BigInteger(1, mac256.doFinal())).toUpperCase();
  }
}
