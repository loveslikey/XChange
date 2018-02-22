package org.knowm.xchange.therock.service;

import feign.RequestTemplate;
import org.knowm.xchange.service.ParamsDigest;
import org.knowm.xchange.therock.TheRockAuthenticated;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;

public class TheRockDigest extends ParamsDigest {

  public static final String HMAC_SHA_512 = "HmacSHA512";

  private final Mac mac;

  public TheRockDigest(String secretKeyStr) {
    try {
      final SecretKey secretKey = new SecretKeySpec(secretKeyStr.getBytes(), HMAC_SHA_512);
      mac = Mac.getInstance(HMAC_SHA_512);
      mac.init(secretKey);
    } catch (Exception e) {
      throw new RuntimeException("Error initializing The Rock Signer", e);
    }
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {
    final String nonce = requestTemplate.queries().get(TheRockAuthenticated.X_TRT_NONCE).iterator().next();
    mac.update(nonce.getBytes());
    mac.update((requestTemplate.url()+requestTemplate.queryLine()).getBytes());

    return String.format("%0128x", new BigInteger(1, mac.doFinal()));
  }
}
