package org.knowm.xchange.gdax.service;

import feign.RequestTemplate;
import net.iharder.Base64;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.ParamsDigest;

import java.io.IOException;

public class GDAXDigest extends ParamsDigest {

  private String signature = "";
  
  private GDAXDigest(byte[] secretKey) {

    super(secretKey, HMAC_SHA_256);
  }

  public static GDAXDigest createInstance(String secretKey) {

    try {
      return secretKey == null ? null : new GDAXDigest(Base64.decode(secretKey));
    } catch (IOException e) {
      throw new ExchangeException("Cannot decode secret key", e);
    }
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {
    throw  new RuntimeException("暂未做适配");
  /*  String pathWithQueryString = restInvocation.getInvocationUrl().replace(restInvocation.getBaseUrl(), "");
    String message = restInvocation.getParamValue(HeaderParam.class, "CB-ACCESS-TIMESTAMP").toString() + restInvocation.getHttpMethod()
        + pathWithQueryString + (restInvocation.getRequestBody() != null ? restInvocation.getRequestBody() : "");

    Mac mac256 = getMac();

    try {
      mac256.update(message.getBytes("UTF-8"));
    } catch (Exception e) {
      throw new ExchangeException("Digest encoding exception", e);
    }

    signature = Base64.encodeBytes(mac256.doFinal());
    return signature;*/
  }
  
  public String getSignature() {
    return signature;
  }
}
