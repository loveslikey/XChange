package org.knowm.xchange.liqui.service;

import feign.RequestTemplate;
import org.apache.commons.codec.binary.Hex;
import org.knowm.xchange.service.ParamsDigest;

import javax.crypto.Mac;
import java.nio.charset.StandardCharsets;

public class LiquiDigest extends ParamsDigest {

  protected LiquiDigest(final String secretKeyBase64) throws IllegalArgumentException {
    super(secretKeyBase64, HMAC_SHA_512);
  }

  public static LiquiDigest createInstance(final String secretKeyBase64) {
    if (secretKeyBase64 != null) {
      return new LiquiDigest(secretKeyBase64);
    }
    return null;
  }

  @Override
  public String digestParams(final RequestTemplate requestTemplate) {

    final Mac mac512 = getMac();
    mac512.update(requestTemplate.bodyTemplate().getBytes(StandardCharsets.UTF_8));

    return new String(Hex.encodeHex(mac512.doFinal()));
  }
}
