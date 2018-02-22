package org.knowm.xchange.kucoin.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.kucoin.KucoinAuthenticated;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;

import org.knowm.xchange.service.ParamsDigest;
import org.knowm.xchange.RestProxyFactory;

public class KucoinBaseService extends BaseExchangeService implements BaseService {

  protected final String apiKey;
  protected final KucoinAuthenticated kucoin;
  protected final ParamsDigest signatureCreator;

  /**
   * Constructor
   *
   * @param exchange
   */
  protected KucoinBaseService(Exchange exchange) {
    super(exchange);
    this.kucoin = RestProxyFactory.createProxy(KucoinAuthenticated.class, exchange.getExchangeSpecification().getSslUri(), getClientConfig());
    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    this.signatureCreator = KucoinDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
  }
}
