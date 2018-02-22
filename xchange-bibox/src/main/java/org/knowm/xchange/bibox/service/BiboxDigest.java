package org.knowm.xchange.bibox.service;

import feign.RequestTemplate;
import org.apache.commons.codec.binary.Hex;
import org.knowm.xchange.bibox.BiboxAuthenticated;
import org.knowm.xchange.service.ParamsDigest;

import java.io.UnsupportedEncodingException;

public class BiboxDigest extends ParamsDigest {

  /**
   * Constructor
   *
   * @param secretKey
   * @throws IllegalArgumentException if key is invalid (cannot be base-64-decoded or the decoded key is invalid).
   */
  private BiboxDigest(String secretKey) {

    super(secretKey, HMAC_MD5);
  }

  public static BiboxDigest createInstance(String secretKey) {

    return secretKey == null ? null : new BiboxDigest(secretKey);
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {

    String cmds = (String) requestTemplate.queries().get(BiboxAuthenticated.FORM_CMDS).iterator().next();
    try {
      return new String(Hex.encodeHex(getMac().doFinal(cmds.getBytes("UTF-8"))));
    } catch (IllegalStateException | UnsupportedEncodingException e1) {
      throw new RuntimeException(e1.getMessage());
    }
  }
}
