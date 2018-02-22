package org.knowm.xchange.vaultoro.service;

import feign.RequestTemplate;
import org.knowm.xchange.service.ParamsDigest;

import javax.crypto.Mac;
import java.math.BigInteger;

public class VaultoroDigest extends ParamsDigest {

  private VaultoroDigest(String secretKeyBase64) {

    super(secretKeyBase64, HMAC_SHA_256);
  }

  public static VaultoroDigest createInstance(String secretKeyBase64) {

    return secretKeyBase64 == null ? null : new VaultoroDigest(secretKeyBase64);
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {

    String invocationUrl = requestTemplate.url()+requestTemplate.queryLine();
    Mac mac = getMac();
    mac.update(invocationUrl.getBytes());

    return String.format("%040x", new BigInteger(1, mac.doFinal()));
  }

}
