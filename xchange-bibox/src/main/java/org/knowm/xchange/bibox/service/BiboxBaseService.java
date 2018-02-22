package org.knowm.xchange.bibox.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.bibox.BiboxAuthenticated;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;

import org.knowm.xchange.service.ParamsDigest;
import org.knowm.xchange.RestProxyFactory;

public class BiboxBaseService extends BaseExchangeService implements BaseService {

  protected final String apiKey;
  protected final BiboxAuthenticated bibox;
  protected final ParamsDigest signatureCreator;

  /**
   * Constructor
   *
   * @param exchange
   */
  protected BiboxBaseService(Exchange exchange) {
    super(exchange);
    this.bibox = RestProxyFactory.createProxy(BiboxAuthenticated.class, exchange.getExchangeSpecification().getSslUri(), getClientConfig());
    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    this.signatureCreator = BiboxDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
  }
}
