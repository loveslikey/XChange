package org.knowm.xchange.bitmex.service;

import feign.RequestTemplate;
import org.knowm.xchange.service.ParamsDigest;

import java.util.Base64;

public class BitmexDigest extends ParamsDigest {

  private String apiKey;

  /**
   * Constructor
   *
   * @param secretKeyBase64 the secret key to sign requests
   */

  private BitmexDigest(byte[] secretKeyBase64) {

    super(Base64.getUrlEncoder().withoutPadding().encodeToString(secretKeyBase64), HMAC_SHA_256);
  }

  public static BitmexDigest createInstance(String secretKeyBase64) {

    if (secretKeyBase64 != null) {
      return new BitmexDigest(Base64.getUrlDecoder().decode(secretKeyBase64.getBytes()));
    }
    return null;
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate ) {

    throw  new RuntimeException("暂未做适配");
/*    String nonce = requestTemplate.queries().get( "api-nonce").iterator().next();
    String path = restInvocation.getInvocationUrl().split(restInvocation.getBaseUrl())[1];
    String payload = restInvocation.getHttpMethod() + "/" + path + nonce + restInvocation.getRequestBody();

    return new String(Hex.encodeHex(getMac().doFinal(payload.getBytes())));*/
  }

  private BitmexDigest(String secretKeyBase64, String apiKey) {

    super(secretKeyBase64, HMAC_SHA_256);
    this.apiKey = apiKey;
  }

  public static BitmexDigest createInstance(String secretKeyBase64, String apiKey) {

    return secretKeyBase64 == null ? null : new BitmexDigest(secretKeyBase64, apiKey);
  }

}
