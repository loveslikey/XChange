package org.knowm.xchange.okcoin;

import feign.RequestTemplate;
import org.knowm.xchange.service.ParamsDigest;
import org.knowm.xchange.utils.Params;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;

public class OkCoinDigest extends ParamsDigest {

  private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

  private final String apikey;
  private final String secretKey;
  private final MessageDigest md;
  private final Comparator<Entry<String, String>> comparator = new Comparator<Map.Entry<String, String>>() {

    @Override
    public int compare(Entry<String, String> o1, Entry<String, String> o2) {

      return o1.getKey().compareTo(o2.getKey());
    }
  };

  public OkCoinDigest(String apikey, String secretKey) {

    this.apikey = apikey;
    this.secretKey = secretKey;

    try {
      md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Problem instantiating message digest.", e);
    }
  }

  private static char[] encodeHex(byte[] data, char[] toDigits) {

    int l = data.length;
    char[] out = new char[l << 1];
    // two characters form the hex value.
    for (int i = 0, j = 0; i < l; i++) {
      out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
      out[j++] = toDigits[0x0F & data[i]];
    }
    return out;
  }

  @Override
  public String digestParams(RequestTemplate requestTemplate) {
    final Params params = Params.of();
    requestTemplate.queries().entrySet().stream().forEach(entry -> {
      params.add(entry.getKey(), entry.getValue());
    });
    final Map<String, String> nameValueMap = params.asHttpHeaders();

    nameValueMap.remove("sign");
    nameValueMap.put("api_key", apikey);

    // odd requirements for buy/sell market orders
    if (nameValueMap.containsKey("type") && nameValueMap.get("type").contains("market")) {
      if (nameValueMap.get("type").equals("buy_market")) {
        nameValueMap.remove("amount");
      } else if (nameValueMap.get("type").equals("sell_market")) {
        nameValueMap.remove("price");
      }
    }
    final List<Map.Entry<String, String>> nameValueList = new ArrayList<>(nameValueMap.entrySet());
    Collections.sort(nameValueList, comparator);

    final Params newParams = Params.of();
    for (int i = 0; i < nameValueList.size(); i++) {
      Map.Entry<String, String> param = nameValueList.get(i);
      newParams.add(param.getKey(), param.getValue());
    }

    // final String message = newParams.asQueryString() + "&secret_key=" + secretKey;
    final String message = newParams.toString() + "&secret_key=" + secretKey;

    try {
      md.reset();

      byte[] digest = md.digest(message.getBytes("US-ASCII"));

      return String.valueOf(encodeHex(digest, DIGITS_UPPER));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Codec error", e);
    }
  }
}
