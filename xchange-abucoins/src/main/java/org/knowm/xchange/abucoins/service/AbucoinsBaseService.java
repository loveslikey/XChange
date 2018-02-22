package org.knowm.xchange.abucoins.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.RestProxyFactory;
import org.knowm.xchange.abucoins.Abucoins;
import org.knowm.xchange.abucoins.AbucoinsAuthenticated;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;
import org.knowm.xchange.utils.jackson.DefaultJacksonObjectMapperFactory;
import org.knowm.xchange.utils.jackson.JacksonObjectMapperFactory;

import java.math.BigDecimal;

/**
 * @author bryant_harris
 */
public class AbucoinsBaseService extends BaseExchangeService implements BaseService {
    protected final Abucoins abucoins;
    protected final AbucoinsAuthenticated abucoinsAuthenticated;
    protected final AbucoinsDigest signatureCreator;

    /**
     * Constructor
     *
     * @param exchange
     */
    public AbucoinsBaseService(Exchange exchange) {

        super(exchange);
        JacksonObjectMapperFactory objectMapperFactory = new DefaultJacksonObjectMapperFactory() {
            @Override
            public void configureObjectMapper(ObjectMapper objectMapper) {
                super.configureObjectMapper(objectMapper);
                SimpleModule module = new SimpleModule();
                module.addSerializer(BigDecimal.class, new ToStringSerializer());
                objectMapper.registerModule(module);
            }
        };
        ObjectMapper objectMapper = objectMapperFactory.createObjectMapper();
        objectMapperFactory.configureObjectMapper(objectMapper);
        Feign.Builder builder = getClientConfig();
        builder.encoder(new JacksonEncoder(objectMapper)).decoder(new JacksonDecoder(objectMapper));

        abucoins = RestProxyFactory.createProxy(Abucoins.class,
                exchange.getExchangeSpecification().getSslUri(),
                builder);
        abucoinsAuthenticated = RestProxyFactory.createProxy(AbucoinsAuthenticated.class,
                exchange.getExchangeSpecification().getSslUri());
        signatureCreator = AbucoinsDigest.createInstance(abucoins,
                exchange.getExchangeSpecification().getSecretKey());


    }

    /**
     * Helper method that performs a null check.  SignatureCreator is null if no API key is provided.
     *
     * @return The timestamp as maintained by the signature creator.
     */
    protected String timestamp() {
        return (signatureCreator == null) ? null : signatureCreator.timestamp();
    }
}
