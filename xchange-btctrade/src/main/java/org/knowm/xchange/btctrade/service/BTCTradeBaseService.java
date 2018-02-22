package org.knowm.xchange.btctrade.service;

import feign.Feign;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.RestProxyFactory;
import org.knowm.xchange.btctrade.BTCTrade;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;

public class BTCTradeBaseService extends BaseExchangeService implements BaseService {

  protected final BTCTrade btcTrade;

  /**
   * Constructor
   *
   * @param exchange
   */
  public BTCTradeBaseService(Exchange exchange) {

    super(exchange);

    ExchangeSpecification exchangeSpecification = exchange.getExchangeSpecification();
    okhttp3.OkHttpClient.Builder okHttpbuilder=getClientBuilder();
    Feign.Builder builder = getClientConfig(okHttpbuilder);
    // btctrade is using an ssl certificate for 33option.com
  /*  config.setHostnameVerifier(CertHelper.createIncorrectHostnameVerifier(exchangeSpecification.getHost(),
        "CN=www.33option.com,OU=IT,O=OPTIONFORTUNE TRADE LIMITED,L=KOWLOON,ST=HONGKONG,C=HK"));*/

    btcTrade = RestProxyFactory.createProxy(BTCTrade.class, exchangeSpecification.getSslUri(), builder);
  }

  protected long toLong(Object object) {

    final long since;
    if (object instanceof Integer) {
      since = (Integer) object;
    } else if (object instanceof Long) {
      since = (Long) object;
    } else {
      since = Long.parseLong(object.toString());
    }
    return since;
  }
}
