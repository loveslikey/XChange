package org.knowm.xchange.bitmarket;

import feign.RequestTemplate;
import org.knowm.xchange.service.ParamsDigest;

import javax.crypto.Mac;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * @author kfonal
 */
public class BitMarketDigest extends ParamsDigest {

  private BitMarketDigest(String secretKeyBase64) throws IllegalArgumentException {
    super(secretKeyBase64, HMAC_SHA_512);
  }

  public static BitMarketDigest createInstance(String secretKeyBase64) {

    return secretKeyBase64 == null ? null : new BitMarketDigest(secretKeyBase64);
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {

    try {
      String postBody = requestTemplate.bodyTemplate();
      Mac mac = getMac();
      mac.update(postBody.getBytes("UTF-8"));
      return String.format("%0128x", new BigInteger(1, mac.doFinal()));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Illegal encoding, check the code.", e);
    }
  }
}
