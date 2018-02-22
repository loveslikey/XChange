package org.knowm.xchange.kraken.service;

import feign.RequestTemplate;
import net.iharder.Base64;
import org.knowm.xchange.service.ParamsDigest;

import javax.crypto.Mac;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Benedikt Bünz
 */
public class KrakenDigest extends ParamsDigest {

  /**
   * Constructor
   *
   * @param secretKeyBase64
   * @throws IllegalArgumentException if key is invalid (cannot be base-64-decoded or the decoded key is invalid).
   */
  private KrakenDigest(byte[] secretKeyBase64) {

    super(secretKeyBase64, HMAC_SHA_512);
  }

  public static KrakenDigest createInstance(String secretKeyBase64) {

    try {
      if (secretKeyBase64 != null)
        return new KrakenDigest(Base64.decode(secretKeyBase64.getBytes()));
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not decode Base 64 string", e);
    }
    return null;
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {

    MessageDigest sha256;
    try {
      sha256 = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Illegal algorithm for post body digest. Check the implementation.");
    }
    sha256.update(requestTemplate.queries().get("nonce").iterator().next().getBytes());
    sha256.update(requestTemplate.bodyTemplate().getBytes());

    Mac mac512 = getMac();
    mac512.update(("/" + requestTemplate.url()).getBytes());
    mac512.update(sha256.digest());

    return Base64.encodeBytes(mac512.doFinal()).trim();

  }
}
