package org.knowm.xchange.bitcurex.service;

import feign.RequestTemplate;
import org.knowm.xchange.service.ParamsDigest;

import javax.crypto.Mac;
import java.util.Base64;

public class BitcurexDigest extends ParamsDigest {

  /**
   * Constructor
   *
   * @param secretKeyBase64
   * @param apiKey @throws IllegalArgumentException if key is invalid (cannot be base-64-decoded or the decoded key is invalid).
   */
  private BitcurexDigest(String secretKeyBase64, String apiKey) {

    super(Base64.getDecoder(). decode( secretKeyBase64), HMAC_SHA_512);
  }

  public static BitcurexDigest createInstance(String secretKeyBase64, String apiKey) {

    return secretKeyBase64 == null ? null : new BitcurexDigest(secretKeyBase64, apiKey);
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {

    String digest;
    byte[] signature;

    Mac sha512 = getMac();

    sha512.update(requestTemplate.bodyTemplate().getBytes());

    signature = sha512.doFinal();
    digest =  Base64.getEncoder().encodeToString(signature);

    return digest;
  }
}
