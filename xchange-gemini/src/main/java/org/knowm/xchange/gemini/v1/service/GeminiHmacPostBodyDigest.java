package org.knowm.xchange.gemini.v1.service;

import feign.RequestTemplate;
import net.iharder.Base64;
import org.knowm.xchange.service.ParamsDigest;

import javax.crypto.Mac;
import java.math.BigInteger;

public class GeminiHmacPostBodyDigest extends ParamsDigest {

  /**
   * Constructor
   *
   * @param secretKeyBase64
   * @throws IllegalArgumentException if key is invalid (cannot be base-64-decoded or the decoded key is invalid).
   */
  private GeminiHmacPostBodyDigest(String secretKeyBase64) {

    super(secretKeyBase64, HMAC_SHA_384);
  }

  public static GeminiHmacPostBodyDigest createInstance(String secretKeyBase64) {

    return secretKeyBase64 == null ? null : new GeminiHmacPostBodyDigest(secretKeyBase64);
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {

    String postBody = requestTemplate.bodyTemplate();
    Mac mac = getMac();
    mac.update(Base64.encodeBytes(postBody.getBytes()).getBytes());

    return String.format("%096x", new BigInteger(1, mac.doFinal()));
  }
}
