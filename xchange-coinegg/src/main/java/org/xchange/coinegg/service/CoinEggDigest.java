package org.xchange.coinegg.service;

import feign.RequestTemplate;
import org.apache.commons.codec.binary.Hex;
import org.knowm.xchange.service.ParamsDigest;
import org.knowm.xchange.utils.Params;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class CoinEggDigest extends ParamsDigest {

  private static Charset UTF8;

  private CoinEggDigest(String md5PrivateKey) {
    super(md5PrivateKey, HMAC_SHA_256);
  }
  
  @Override
  public String digestParams(RequestTemplate requestTemplate) {

    // Create Query String From Form Parameters
    final Params params = Params.of();
    requestTemplate.queries().entrySet().stream().filter(entry -> !entry.getKey().equals("signature")).forEach(entry -> {
      params.add(entry.getKey(), entry.getValue());
    });


    // Parse Query String
    byte[] queryString = params.asQueryString()
                               .trim()
                               .getBytes(UTF8);
    
    // Create And Return Signature
    return hex(getMac().doFinal(queryString));
  }
  
  public static CoinEggDigest createInstance(String privateKey) {
    try {
      CoinEggDigest.UTF8 = Charset.forName("UTF-8");
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      
      return new CoinEggDigest(hex(md5.digest(privateKey.getBytes(UTF8))));
    } 
    catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Illegal algorithm for post body digest. Check the implementation.");
    }
  }
  
  private static String hex(byte[] b) {
    return String.valueOf(Hex.encodeHex(b));
  }
  
}
