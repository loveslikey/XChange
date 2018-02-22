package org.knowm.xchange.coinfloor.service;

import feign.Feign;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.HttpClientBuilderUtil;
import org.knowm.xchange.RestProxyFactory;
import org.knowm.xchange.coinfloor.CoinfloorAuthenticated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinfloorAuthenticatedService extends CoinfloorService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  protected CoinfloorAuthenticated coinfloor;

  protected CoinfloorAuthenticatedService(Exchange exchange) {
    super(exchange);

    ExchangeSpecification specification = exchange.getExchangeSpecification();

    if (specification.getUserName() == null || specification.getPassword() == null) {
      logger.info("Authenticated endpoints are not available - username and password have not been configured");
      coinfloor = null;
      return;
    }

    okhttp3.OkHttpClient.Builder okHttpbuilder=getClientBuilder();

    String user = specification.getUserName();
    okHttpbuilder= HttpClientBuilderUtil.addBasicAuthCredentials(okHttpbuilder, user == null ? "" : user, specification.getPassword());
    Feign.Builder builder = getClientConfig(okHttpbuilder);
    coinfloor = RestProxyFactory.createProxy(CoinfloorAuthenticated.class, exchange.getExchangeSpecification().getSslUri(), builder);
  }
}
