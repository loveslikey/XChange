package org.knowm.xchange.bitfinex.v1.service;

import net.iharder.Base64;
import org.knowm.xchange.service.ParamsDigest;
import feign.RequestTemplate;

public class BitfinexPayloadDigest extends ParamsDigest {

  @Override
  public synchronized String digestParams(RequestTemplate requestTemplate) {

    String postBody = requestTemplate.bodyTemplate();
    return Base64.encodeBytes(postBody.getBytes());
  }
}
