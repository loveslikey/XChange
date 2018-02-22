package org.known.xchange.acx;

import feign.RequestTemplate;
import org.knowm.xchange.service.ParamsDigest;

public class AcxSignatureCreator extends ParamsDigest {
    private static final String PLACEHOLDER = "ACX_PLACEHOLDER";

    public AcxSignatureCreator(String secretKey) {
        super(secretKey, HMAC_SHA_256);
    }

    @Override
    public String digestParams(RequestTemplate requestTemplate) {
        throw  new RuntimeException("暂未做适配");
/*        String method =requestTemplate.method();
        String path = stripParams(requestTemplate.url());
        String query = Stream.of(requestTemplate.queries())
                .map(Params::asHttpHeaders)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .filter(e -> !"signature".equals(e.getKey()))
                .sorted(Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
        String toSign = String.format("%s|/api/v2/%s|%s", method, path, query);
        Mac sha256hmac = getMac();
        byte[] signed = sha256hmac.doFinal(toSign.getBytes());
        String signature = new String(encodeHex(signed));

        return signature;*/
    }

    private String stripParams(String path) {
        int paramsStart = path.indexOf("?");
        String stripped = paramsStart == -1 ? path : path.substring(0, paramsStart);
        if (stripped.startsWith("/")) {
            stripped = stripped.substring(1);
        }
        return stripped;
    }

    private static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }
        return out;
    }


    @Override
    public String toString() {
        return PLACEHOLDER;
    }

    private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
}
