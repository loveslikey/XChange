package org.knowm.xchange.binance.service;

import feign.RequestTemplate;
import org.knowm.xchange.binance.BinanceAuthenticated;
import org.knowm.xchange.service.ParamsDigest;
import org.knowm.xchange.utils.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;

import static org.knowm.xchange.utils.DigestUtils.bytesToHex;

public class BinanceHmacDigest extends ParamsDigest {

  private static final Logger LOG = LoggerFactory.getLogger(BinanceHmacDigest.class);


  private BinanceHmacDigest(String secretKeyBase64) {
    super(secretKeyBase64, HMAC_SHA_256);
  }

  public static BinanceHmacDigest createInstance(String secretKeyBase64) {
    return secretKeyBase64 == null ? null : new BinanceHmacDigest(secretKeyBase64);
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {
    try {
      final String input;

      if (requestTemplate.url().startsWith("wapi/")) {
        // little dirty hack for /wapi methods
        input = getQuery(requestTemplate.queries());
      } else {
        switch (requestTemplate.method()) {
          case "GET":
          case "DELETE":
            input = getQuery(requestTemplate.queries());
            break;
          case "POST":
            input = requestTemplate.bodyTemplate();
            break;
          default:
            throw new RuntimeException("Not support http method: " + requestTemplate.method());
        }
      }

      Mac mac = getMac();
      mac.update(input.getBytes("UTF-8"));
      String printBase64Binary = bytesToHex(mac.doFinal());
      LOG.debug("value to sign: {},  signature: {}", input, printBase64Binary);
      return printBase64Binary;
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Illegal encoding, check the code.", e);
    }
  }

  /**
   * @return the query string except of the "signature" parameter
   */
  private static String getQuery( Map<String, Collection<String>>  queries) {
    final Params p = Params.of();
    queries.entrySet().stream().filter(entry -> !entry.getKey().equals(BinanceAuthenticated.SIGNATURE)).forEach(entry -> {
      p.add(entry.getKey(), entry.getValue());
    });
    return p.asQueryString();
  }
}
