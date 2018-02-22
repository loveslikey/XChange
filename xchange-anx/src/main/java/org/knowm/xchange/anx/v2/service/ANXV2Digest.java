package org.knowm.xchange.anx.v2.service;

import feign.RequestTemplate;
import net.iharder.Base64;
import org.knowm.xchange.service.ParamsDigest;

import javax.crypto.Mac;
import java.io.IOException;

/**
 * @author Matija Mazi
 */
public class ANXV2Digest extends ParamsDigest {

  /**
   * Constructor
   *
   * @param secretKeyBase64
   * @throws IllegalArgumentException if key is invalid (cannot be base-64-decoded or the decoded key is invalid).
   */
  private ANXV2Digest(byte[] secretKeyBase64) {

    super(secretKeyBase64, HMAC_SHA_512);
  }

  public static ANXV2Digest createInstance(String secretKeyBase64) {

    try {
      if (secretKeyBase64 != null)
        return new ANXV2Digest(Base64.decode(secretKeyBase64.getBytes()));
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not decode Base 64 string", e);
    }
    return null;
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {

    Mac mac = getMac();
    mac.update(requestTemplate.url().getBytes());
    mac.update(new byte[]{0});
    mac.update(requestTemplate.bodyTemplate().getBytes());

    return Base64.encodeBytes(mac.doFinal()).trim();
  }
}
