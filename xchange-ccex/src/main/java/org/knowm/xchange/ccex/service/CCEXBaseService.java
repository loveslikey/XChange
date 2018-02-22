package org.knowm.xchange.ccex.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ccex.CCEXAuthenticated;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;

import org.knowm.xchange.service.ParamsDigest;
import org.knowm.xchange.RestProxyFactory;

/**
 * @author Andraž Prinčič
 */
public class CCEXBaseService extends BaseExchangeService implements BaseService {

  protected final String apiKey;
  protected final CCEXAuthenticated cCEXAuthenticated;
  protected final ParamsDigest signatureCreator;

  /**
   * Constructor
   *
   * @param exchange
   */
  public CCEXBaseService(Exchange exchange) {

    super(exchange);

    this.cCEXAuthenticated = RestProxyFactory.createProxy(CCEXAuthenticated.class, exchange.getExchangeSpecification().getSslUri(),
        getClientConfig());
    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    this.signatureCreator = CCEXDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
  }
}
