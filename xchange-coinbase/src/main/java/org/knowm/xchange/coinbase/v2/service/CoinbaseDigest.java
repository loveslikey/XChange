package org.knowm.xchange.coinbase.v2.service;

import feign.RequestTemplate;
import org.knowm.xchange.service.ParamsDigest;

import javax.crypto.Mac;
import java.math.BigInteger;

public class CoinbaseDigest extends ParamsDigest {

  private CoinbaseDigest(String secretKey) {

    super(secretKey, HMAC_SHA_256);
  }

  public static CoinbaseDigest createInstance(String secretKey) {

    return secretKey == null ? null : new CoinbaseDigest(secretKey);
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {

    final String message =requestTemplate.queries().get("ACCESS_NONCE").iterator().next() + requestTemplate.url()+requestTemplate.queryLine()
        + requestTemplate.bodyTemplate();

    Mac mac256 = getMac();
    mac256.update(message.getBytes());

    return String.format("%064x", new BigInteger(1, mac256.doFinal()));
  }

  @Override
  public Mac getMac() {
    return super.getMac();
  }
  
}
