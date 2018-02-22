package org.knowm.xchange.btctrade.service;

import feign.RequestTemplate;
import org.knowm.xchange.service.ParamsDigest;
import org.knowm.xchange.utils.Params;

import javax.crypto.Mac;
import java.math.BigInteger;
import java.nio.charset.Charset;

public class BTCTradeDigest extends ParamsDigest {

  private static final String ENCODING = "UTF-8";
  private static final Charset CHARSET = Charset.forName(ENCODING);

  public static BTCTradeDigest createInstance(String secret) {

    return new BTCTradeDigest(secret.getBytes(CHARSET));
  }

  private BTCTradeDigest(byte[] secretKeyBase64) {

    super(secretKeyBase64, HMAC_SHA_256);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String digestParams(RequestTemplate requestTemplate) {

    final Params params = Params.of();
    requestTemplate.queries().entrySet().stream().filter(entry -> !entry.getKey().equals("signature")).forEach(entry -> {
      params.add(entry.getKey(), entry.getValue());
    });

    String message = params.asQueryString();

    Mac mac = getMac();
    mac.update(message.getBytes());

    return String.format("%064x", new BigInteger(1, mac.doFinal()));
  }

}
