package org.knowm.xchange.poloniex.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.RestProxyFactory;
import org.knowm.xchange.poloniex.Poloniex;
import org.knowm.xchange.poloniex.PoloniexAuthenticated;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;
import org.knowm.xchange.service.ParamsDigest;
import org.knowm.xchange.utils.jackson.DefaultJacksonObjectMapperFactory;
import org.knowm.xchange.utils.jackson.JacksonObjectMapperFactory;

/**
 * @author Zach Holmes
 */

public class PoloniexBaseService extends BaseExchangeService implements BaseService {

  protected final String apiKey;
  protected final PoloniexAuthenticated poloniexAuthenticated;
  protected final ParamsDigest signatureCreator;

  protected final Poloniex poloniex;

  /**
   * Constructor
   *
   * @param exchange
   */
  public PoloniexBaseService(Exchange exchange) {

    super(exchange);

    // TODO should this be fixed/added in rescu itself?
    // Fix for empty string array mapping exception
    //ClientConfig rescuConfig = getClientConfig();
    JacksonObjectMapperFactory objectMapperFactory= new DefaultJacksonObjectMapperFactory() {
      @Override
      public void configureObjectMapper(ObjectMapper objectMapper) {
        super.configureObjectMapper(objectMapper);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
      }
    };
    ObjectMapper objectMapper=objectMapperFactory.createObjectMapper();
    objectMapperFactory.configureObjectMapper(objectMapper);
    Feign.Builder builder = getClientConfig();
    builder.encoder(new JacksonEncoder(objectMapper)).decoder(new JacksonDecoder(objectMapper));
    this.poloniexAuthenticated = RestProxyFactory.createProxy(PoloniexAuthenticated.class, exchange.getExchangeSpecification().getSslUri(),
            builder);
    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    this.signatureCreator = PoloniexDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
    this.poloniex = RestProxyFactory.createProxy(Poloniex.class, exchange.getExchangeSpecification().getSslUri(), builder);
  }

}
