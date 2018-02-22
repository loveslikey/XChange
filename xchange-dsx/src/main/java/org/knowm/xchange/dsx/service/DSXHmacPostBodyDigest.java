package org.knowm.xchange.dsx.service;

import feign.RequestTemplate;
import org.knowm.xchange.service.ParamsDigest;

import javax.crypto.Mac;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author Mikhail Wall
 */

public class DSXHmacPostBodyDigest extends ParamsDigest {

  private DSXHmacPostBodyDigest(String secretKeyBase64) {

    super(secretKeyBase64, HMAC_SHA_512);
  }

  public static DSXHmacPostBodyDigest createInstance(String secretKeyBase64) {

    return secretKeyBase64 == null ? null : new DSXHmacPostBodyDigest(secretKeyBase64);
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {

    try {
      String postBody = requestTemplate.bodyTemplate();
      Mac mac = getMac();
      mac.update(postBody.getBytes("UTF-8"));
      return Base64.getEncoder().encodeToString(mac.doFinal());
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Illegal encoding, check the code.", e);
    }
  }
}
