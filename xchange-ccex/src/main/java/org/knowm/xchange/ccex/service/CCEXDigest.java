package org.knowm.xchange.ccex.service;

import feign.RequestTemplate;
import org.knowm.xchange.service.ParamsDigest;

import javax.crypto.Mac;
import java.math.BigInteger;

public class CCEXDigest extends ParamsDigest {

  private CCEXDigest(String secretKey) {

    super(secretKey, HMAC_SHA_512);
  }

  public static CCEXDigest createInstance(String secretKeyBase64) {

    return secretKeyBase64 == null ? null : new CCEXDigest(secretKeyBase64);
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {
    String invocationUrl = requestTemplate.url()+requestTemplate.queryLine();
    Mac mac = getMac();
    mac.update(invocationUrl.getBytes());

    return String.format("%0128x", new BigInteger(1, mac.doFinal()));
  }

}
