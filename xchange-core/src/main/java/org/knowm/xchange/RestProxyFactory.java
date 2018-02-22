package org.knowm.xchange;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.okhttp.OkHttpClient;
import okhttp3.ConnectionPool;
import org.knowm.xchange.org.knowm.xchange.utils.proxy.ParamsInterceptor;
import org.knowm.xchange.utils.proxy.XchangeInvocationHandlerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/2/18.
 */
public final class RestProxyFactory {
    private RestProxyFactory() {
    }


    /**
     * Create a proxy implementation of restInterface. The interface must be annotated with jax-rs annotations. Basic support exists for {@link javax.ws.rs.Path}, {@link javax.ws.rs.GET},
     * {@link javax.ws.rs.POST}, {@link javax.ws.rs.QueryParam}, {@link javax.ws.rs.FormParam}, {@link javax.ws.rs.HeaderParam}, {@link javax.ws.rs.PathParam}., {@link javax.ws.rs.PATCH}
     *
     * @param restInterface The interface to implement
     * @param baseUrl       The service base baseUrl
     * @param <I>           The interface to implement
     * @param builder        Client configuration
     * @return a proxy implementation of restInterface
     */
    public static <I> I createProxy(Class<I> restInterface, String baseUrl, Feign.Builder builder) {
        return builder.target(restInterface, baseUrl);
    }

    public static <I> I createProxy(Class<I> restInterface, String baseUrl) {
        Feign.Builder builder =getDefaultClientConfig();
        return createProxy(restInterface, baseUrl, builder);
    }

    public  static  okhttp3.OkHttpClient.Builder getDefaultClientBuilder(){
        ConnectionPool pool = new ConnectionPool(25, 3, TimeUnit.SECONDS);
        okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder() //
                .connectTimeout(3, TimeUnit.SECONDS)
                .followRedirects(true)
                .readTimeout(3, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .writeTimeout(3, TimeUnit.SECONDS).
                        connectionPool(pool);
        return builder;
    }

    public static Feign.Builder getDefaultClientConfig(okhttp3.OkHttpClient.Builder okHttpBuilder){
        okhttp3.OkHttpClient client = okHttpBuilder.build();
        ObjectMapper mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        XchangeInvocationHandlerFactory invocationHandlerFactory=new XchangeInvocationHandlerFactory();
        invocationHandlerFactory.addInterceptor(new ParamsInterceptor());
        Feign.Builder builder = Feign.builder().client(new OkHttpClient(client))
                .contract(new JAXRSContract()).requestInterceptor(new DigestRequestInterceptor())
                .encoder(new JacksonEncoder(mapper))
                .decoder(new JacksonDecoder(mapper)).invocationHandlerFactory(invocationHandlerFactory);
        return builder;
    }

    public static Feign.Builder getDefaultClientConfig() {
        okhttp3.OkHttpClient.Builder okHttpBuilder = getDefaultClientBuilder();
        Feign.Builder builder = getDefaultClientConfig(okHttpBuilder);
        return builder;
    }

}
