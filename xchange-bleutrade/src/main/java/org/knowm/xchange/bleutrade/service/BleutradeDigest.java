package org.knowm.xchange.bleutrade.service;

import feign.RequestTemplate;
import org.knowm.xchange.service.ParamsDigest;

import javax.crypto.Mac;
import java.math.BigInteger;

public class BleutradeDigest extends ParamsDigest {

  /**
   * Constructor
   *
   * @param secretKeyBase64
   * @throws IllegalArgumentException if key is invalid (cannot be base-64-decoded or the decoded key is invalid).
   */
  private BleutradeDigest(String secretKeyBase64) {

    super(secretKeyBase64, HMAC_SHA_512);
  }

  public static BleutradeDigest createInstance(String secretKeyBase64) {

    return secretKeyBase64 == null ? null : new BleutradeDigest(secretKeyBase64);
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {

    String invocationUrl = requestTemplate.url()+requestTemplate.queryLine();
    Mac mac = getMac();
    mac.update(invocationUrl.getBytes());

    return String.format("%0128x", new BigInteger(1, mac.doFinal()));
  }
}
