package org.knowm.xchange.huobi.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.RestProxyFactory;
import org.knowm.xchange.huobi.HuobiAuthenticated;
import org.knowm.xchange.huobi.HuobiExchange;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;
import org.knowm.xchange.service.ParamsDigest;

import java.io.IOException;

public class HuobiBaseService extends BaseExchangeService implements BaseService {

    protected final String apiKey;
    protected final ParamsDigest signatureCreator;
    protected final HuobiAuthenticated huobiAuthenticated;

    protected HuobiBaseService(Exchange exchange) {
        super(exchange);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // disabled features:
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        Feign.Builder builder = getClientConfig().decoder(new JacksonDecoder(mapper)).encoder(new JacksonEncoder(mapper));
        this.signatureCreator = HuobiDigest.createInstance(exchange.getExchangeSpecification());
        this.huobiAuthenticated = RestProxyFactory.createProxy(HuobiAuthenticated.class, exchange.getExchangeSpecification().getSslUri(), builder);
        this.apiKey = exchange.getExchangeSpecification().getApiKey();

    }

    public long getTimestamp() throws IOException {
        return System.currentTimeMillis() + ((HuobiExchange) exchange).deltaServerTime();
    }


}
