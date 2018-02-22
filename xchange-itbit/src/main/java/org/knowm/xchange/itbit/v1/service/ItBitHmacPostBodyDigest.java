package org.knowm.xchange.itbit.v1.service;

import feign.RequestTemplate;
import net.iharder.Base64;
import org.knowm.xchange.service.ParamsDigest;

import javax.crypto.Mac;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Map;

public class ItBitHmacPostBodyDigest extends ParamsDigest {

  private static final String FIELD_SEPARATOR = "\",\"";

  private final String apiKey;
  private final Charset charset;

  /**
   * Constructor
   *
   * @param secretKeyBase64
   * @throws IllegalArgumentException if key is invalid (cannot be base-64-decoded or the decoded key is invalid).
   */
  private ItBitHmacPostBodyDigest(String apiKey, String secretKeyBase64) {

    super(secretKeyBase64, HMAC_SHA_512);
    this.apiKey = apiKey;
    this.charset = Charset.forName("UTF-8");
  }

  public static ItBitHmacPostBodyDigest createInstance(String apiKey, String secretKeyBase64) {

    return secretKeyBase64 == null ? null : new ItBitHmacPostBodyDigest(apiKey, secretKeyBase64);
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {

    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Illegal algorithm for post body digest. Check the implementation.");
    }

    Map<String, Collection<String>> httpHeaders =requestTemplate.queries();
    String currentNonce = httpHeaders.get("X-Auth-Nonce").iterator().next();
    String currentTimestamp = httpHeaders.get("X-Auth-Timestamp").iterator().next();

    // only POST requests will have a non-null request body.
    String requestBody = requestTemplate.bodyTemplate();
    if (requestBody == null) {
      requestBody = "";
    } else {
      requestBody = requestBody.replace("\"", "\\\"");
    }

    String verb = requestTemplate.method().trim();
    String invocationUrl = (requestTemplate.url()+requestTemplate.queryLine()).trim();
    String jsonEncodedArray = new StringBuilder("[\"").append(verb).append(FIELD_SEPARATOR).append(invocationUrl).append(FIELD_SEPARATOR)
        .append(requestBody).append(FIELD_SEPARATOR).append(currentNonce).append(FIELD_SEPARATOR).append(currentTimestamp).append("\"]").toString();
    md.update(currentNonce.getBytes(charset));
    md.update(jsonEncodedArray.getBytes(charset));
    byte[] messageHash = md.digest();

    Mac mac512 = getMac();
    mac512.update(invocationUrl.getBytes(charset));
    mac512.update(messageHash);

    byte[] hmacDigest = mac512.doFinal();
    return apiKey + ":" + Base64.encodeBytes(hmacDigest);
  }
}
