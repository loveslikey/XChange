package org.knowm.xchange.kucoin.service;

import feign.RequestTemplate;
import org.knowm.xchange.service.ParamsDigest;

public class KucoinDigest extends ParamsDigest {

  /**
   * Constructor
   *
   * @param secretKeyBase64
   * @throws IllegalArgumentException if key is invalid (cannot be base-64-decoded or the decoded key is invalid).
   */
  private KucoinDigest(String secretKeyBase64) {

    super(secretKeyBase64, HMAC_SHA_256);
  }

  public static KucoinDigest createInstance(String secretKeyBase64) {

    return secretKeyBase64 == null ? null : new KucoinDigest(secretKeyBase64);
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {

    throw  new RuntimeException("暂未做适配");
    // just write down their example code without thinking too much
    // https://kucoinapidocs.docs.apiary.io/#introduction/authentication/signature-calculation
/*    String endpoint = "/" + restInvocation.getPath(); // needs leading slash
    String queryString = restInvocation.getParamsMap().get(QueryParam.class).asHttpHeaders().entrySet()
        .stream()
        .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
        .map(e -> e.getKey() + "=" + e.getValue())
        .collect(Collectors.joining("&"));
    Long nonce = (Long) restInvocation.getParamValue(HeaderParam.class, KucoinAuthenticated.HEADER_NONCE);
    String strForSign = endpoint + "/" + nonce +"/" + queryString;
    Mac mac = getMac();
    try {
      mac.update(Base64.getEncoder().encode(strForSign.getBytes("UTF-8")));
    } catch (IllegalStateException | UnsupportedEncodingException e1) {
      throw new RuntimeException(e1.getMessage());
    }
    return new String(Hex.encodeHex(mac.doFinal()));*/
  }
}
