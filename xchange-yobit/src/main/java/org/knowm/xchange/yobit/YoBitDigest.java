package org.knowm.xchange.yobit;

import feign.RequestTemplate;
import org.knowm.xchange.service.ParamsDigest;
import org.knowm.xchange.utils.Params;

import javax.crypto.Mac;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;


public class YoBitDigest extends ParamsDigest {

  private YoBitDigest(String secretKeyBase64, String apiKey) throws IOException {
    super(secretKeyBase64, HMAC_SHA_512);
  }

  public static YoBitDigest createInstance(String secretKeyBase64, String apiKey) {
    try {
      return new YoBitDigest(secretKeyBase64, apiKey);
    } catch (Exception e) {
      throw new IllegalStateException("cannot create digest", e);
    }
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {
    Params params =Params.of();
    Map<String, Collection<String>> queries= requestTemplate.queries();
    queries.entrySet().stream().forEach(entry-> {
            params.add(entry.getKey(),entry.getValue());
    });
    StringBuilder queryString = new StringBuilder(params.asQueryString());

    Mac mac = getMac();

    byte[] source = mac.doFinal(queryString.toString().getBytes());

    return String.format("%0128x", new BigInteger(1, source));
  }
}
