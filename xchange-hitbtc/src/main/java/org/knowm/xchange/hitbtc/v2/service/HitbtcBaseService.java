package org.knowm.xchange.hitbtc.v2.service;

import feign.Feign;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.HttpClientBuilderUtil;
import org.knowm.xchange.RestProxyFactory;
import org.knowm.xchange.hitbtc.v2.HitbtcAuthenticated;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;

public class HitbtcBaseService extends BaseExchangeService implements BaseService {

  protected final HitbtcAuthenticated hitbtc;

  protected HitbtcBaseService(Exchange exchange) {

    super(exchange);

    String apiKey = exchange.getExchangeSpecification().getApiKey();
    String secretKey = exchange.getExchangeSpecification().getSecretKey();
/*    Feign.Builder config = getClientConfig();
    ClientConfigUtil.addBasicAuthCredentials(config, apiKey, secretKey);*/
    okhttp3.OkHttpClient.Builder okHttpbuilder=getClientBuilder();

    okHttpbuilder= HttpClientBuilderUtil.addBasicAuthCredentials(okHttpbuilder, apiKey, secretKey);
    Feign.Builder builder = getClientConfig(okHttpbuilder);
    hitbtc = RestProxyFactory.createProxy(HitbtcAuthenticated.class, exchange.getExchangeSpecification().getSslUri(), builder);
  }

}
