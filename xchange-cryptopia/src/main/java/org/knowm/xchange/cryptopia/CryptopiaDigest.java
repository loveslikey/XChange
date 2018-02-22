package org.knowm.xchange.cryptopia;

import feign.RequestTemplate;
import net.iharder.Base64;
import org.knowm.xchange.SynchronizedValueFactory;
import org.knowm.xchange.service.ParamsDigest;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class CryptopiaDigest extends ParamsDigest {

  private final SynchronizedValueFactory<Long> nonceFactory;
  private final String apiKey;

  private CryptopiaDigest(SynchronizedValueFactory<Long> nonceFactory, String secretKey, String apiKey) throws IOException {
    super(decodeBase64(secretKey), HMAC_SHA_256);

    this.nonceFactory = nonceFactory;
    this.apiKey = apiKey;
  }

  public static CryptopiaDigest createInstance(SynchronizedValueFactory<Long> nonceFactory, String secretKey, String apiKey) {
    if (secretKey == null)
      return null;

    try {
      return new CryptopiaDigest(nonceFactory, secretKey, apiKey);
    } catch (Exception e) {
      throw new IllegalStateException("cannot create digest", e);
    }
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {
    try {
      String urlMethod = requestTemplate.url()+requestTemplate.queryLine();
      String nonce = String.valueOf(nonceFactory.createValue());

      String body =requestTemplate.bodyTemplate();
      String md5 = Base64.encodeBytes(MessageDigest.getInstance("MD5").digest(body.getBytes("UTF-8")));

      String reqSignature =
          apiKey
              + "POST"
              + URLEncoder.encode(urlMethod, StandardCharsets.UTF_8.toString()).toLowerCase()
              + nonce
              + md5;

      return "amx "
          + apiKey
          + ":"
          + Base64.encodeBytes(getMac().doFinal(reqSignature.getBytes("UTF-8")))
          + ":"
          + nonce;
    } catch (Exception e) {
      throw new IllegalStateException("Faile to sign request", e);
    }
  }
}
