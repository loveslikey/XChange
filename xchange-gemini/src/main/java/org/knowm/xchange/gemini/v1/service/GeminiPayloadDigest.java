package org.knowm.xchange.gemini.v1.service;

import net.iharder.Base64;
import org.knowm.xchange.service.ParamsDigest;
import feign.RequestTemplate;

public class GeminiPayloadDigest extends ParamsDigest {

  @Override
  public synchronized String digestParams(RequestTemplate requestTemplate) {

    String postBody = requestTemplate.bodyTemplate();
    return Base64.encodeBytes(postBody.getBytes());
  }
}
