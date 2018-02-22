package org.knowm.xchange.bitcoincore.service;

import feign.Feign;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.HttpClientBuilderUtil;
import org.knowm.xchange.RestProxyFactory;
import org.knowm.xchange.bitcoincore.BitcoinCore;
import org.knowm.xchange.bitcoincore.dto.BitcoinCoreException;
import org.knowm.xchange.bitcoincore.dto.account.BitcoinCoreBalanceRequest;
import org.knowm.xchange.bitcoincore.dto.account.BitcoinCoreBalanceResponse;
import org.knowm.xchange.bitcoincore.dto.account.BitcoinCoreUnconfirmedBalanceRequest;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.BaseExchangeService;

import java.io.IOException;

public class BitcoinCoreAccountServiceRaw extends BaseExchangeService {

  private final BitcoinCore bitcoinCore;

  private final BitcoinCoreBalanceRequest balanceRequest = new BitcoinCoreBalanceRequest();
  private final BitcoinCoreUnconfirmedBalanceRequest unconfirmedBalanceRequest = new BitcoinCoreUnconfirmedBalanceRequest();

  protected BitcoinCoreAccountServiceRaw(Exchange exchange) {
    super(exchange);

    ExchangeSpecification specification = exchange.getExchangeSpecification();

    okhttp3.OkHttpClient.Builder okHttpbuilder=getClientBuilder();

    String user = specification.getUserName();
    okHttpbuilder= HttpClientBuilderUtil.addBasicAuthCredentials(okHttpbuilder, user == null ? "" : user, specification.getPassword());
    Feign.Builder builder = getClientConfig(okHttpbuilder);
    bitcoinCore = RestProxyFactory.createProxy(BitcoinCore.class, specification.getPlainTextUri(), builder);
  }

  public BitcoinCoreBalanceResponse getBalance() throws IOException {
    try {
      return bitcoinCore.getBalance(balanceRequest);
    } catch (BitcoinCoreException e) {
      throw new ExchangeException(e);
    }
  }

  public BitcoinCoreBalanceResponse getUnconfirmedBalance() throws IOException {
    try {
      return bitcoinCore.getUnconfirmedBalance(unconfirmedBalanceRequest);
    } catch (BitcoinCoreException e) {
      throw new ExchangeException(e);
    }
  }
}
