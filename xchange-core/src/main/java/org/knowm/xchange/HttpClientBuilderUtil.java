package org.knowm.xchange;

import net.iharder.Base64;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2018/2/18.
 */
public class HttpClientBuilderUtil {
    public static  okhttp3.OkHttpClient.Builder addBasicAuthCredentials( okhttp3.OkHttpClient.Builder okHttpbuilder , String user, String password) {
        Interceptor mTokenInterceptor = new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                if (alreadyHasAuthorizationHeader(originalRequest)) {
                    return chain.proceed(originalRequest);
                }
                Request authorised = originalRequest.newBuilder()
                        .header("Authorization", digestForBasicAuth(user, password))
                        .build();
                return chain.proceed(authorised);
            }
        };
        okHttpbuilder.addInterceptor(mTokenInterceptor);
        return  okHttpbuilder;

        //return config.addDefaultParam(HeaderParam.class, "Authorization", digestForBasicAuth(user, password));
    }
    static Boolean  alreadyHasAuthorizationHeader( Request originalRequest){
        return StringUtils.isNotBlank(originalRequest.header("Authorization"));
    }
    public static String digestForBasicAuth(String username, String password) {
        try {
            byte[] inputBytes = (username + ":" + password).getBytes("ISO-8859-1");
            return "Basic " + Base64.encodeBytes(inputBytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported encoding, fix the code.", e);
        }
    }
}
