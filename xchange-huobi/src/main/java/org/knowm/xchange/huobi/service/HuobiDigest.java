package org.knowm.xchange.huobi.service;


import feign.RequestTemplate;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.service.ParamsDigest;
import org.knowm.xchange.utils.Params;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HuobiDigest extends ParamsDigest {

    private final String secretKey;
    private final String appKey;
    static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");
    static final ZoneId ZONE_GMT = ZoneId.of("Z");


    public static HuobiDigest createInstance(ExchangeSpecification exchangeSpecification) {
        return new HuobiDigest(exchangeSpecification);
    }

    public HuobiDigest(ExchangeSpecification exchangeSpecification) {
        this.secretKey = exchangeSpecification.getSecretKey();
        this.appKey = exchangeSpecification.getApiKey();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String digestParams(RequestTemplate requestTemplate) {
        Map<String, Collection<String>> queries = requestTemplate.queries();
        queries.remove("Signature");
        final Params p = Params.of();
        queries.entrySet().stream().forEach(entry -> {
            p.add(entry.getKey(), entry.getValue());
        });
        final Map<String, String> nameValueMap = p.asHttpHeaders();
        queries.put("AccessKeyId", Collections.singletonList(appKey));
        queries.put("SignatureVersion", Collections.singletonList("2"));
        queries.put("SignatureMethod", Collections.singletonList("HmacSHA256"));
        queries.put("Timestamp", Collections.singletonList(gmtNow()));
        return createSignature(secretKey, requestTemplate.method(), "", requestTemplate.url(), nameValueMap);
    }

    /**
     * 创建一个有效的签名。该方法为客户端调用，将在传入的params中添加AccessKeyId、Timestamp、SignatureVersion、SignatureMethod、Signature参数。
     *
     * @param appSecretKey AppKeySecret.
     * @param method       请求方法，"GET"或"POST"
     * @param host         请求域名，例如"be.huobi.com"
     * @param uri          请求路径，注意不含?以及后的参数，例如"/v1/api/info"
     * @param params       原始请求参数，以Key-Value存储，注意Value不要编码
     */
    public String createSignature(String appSecretKey, String method, String host,
                                  String uri, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(1024);
        sb.append(method.toUpperCase()).append('\n') // GET
                .append(host.toLowerCase()).append('\n') // Host
                .append(uri).append('\n'); // /path

        // build signature:
        SortedMap<String, String> map = new TreeMap<>(params);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append('=').append(urlEncode(value)).append('&');
        }
        // remove last '&':
        sb.deleteCharAt(sb.length() - 1);
        // sign:
        Mac hmacSha256 = null;
        try {
            hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secKey =
                    new SecretKeySpec(appSecretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSha256.init(secKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No such algorithm: " + e.getMessage());
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key: " + e.getMessage());
        }
        String payload = sb.toString();
        byte[] hash = hmacSha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        String actualSign = Base64.getEncoder().encodeToString(hash);
        return actualSign;
        //params.put("Signature", actualSign);
    }

    /**
     * Return epoch seconds
     */
    long epochNow() {
        return Instant.now().getEpochSecond();
    }

    String gmtNow() {
        return Instant.ofEpochSecond(epochNow()).atZone(ZONE_GMT).format(DT_FORMAT);
    }

    /**
     * 使用标准URL Encode编码。注意和JDK默认的不同，空格被编码为%20而不是+。
     *
     * @param s String字符串
     * @return URL编码后的字符串
     */
    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("UTF-8 encoding not supported!");
        }
    }

}
