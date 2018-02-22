package org.knowm.xchange.huobi.service.marketdata;

import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.huobi.HuobiExchange;
import org.knowm.xchange.service.marketdata.MarketDataService;

/**
 * @author timmolter
 */
public class TickerFetchIntegration {

  @Test
  public void tickerFetchTest() throws Exception {

    Exchange exchange = ExchangeFactory.INSTANCE.createExchange(HuobiExchange.class.getName());
    MarketDataService marketDataService = exchange.getMarketDataService();

    OrderBook orderBook= marketDataService.getOrderBook(new CurrencyPair("BTC", "USDT"));
    System.out.println(orderBook);
    Ticker ticker = marketDataService.getTicker(new CurrencyPair("BTC", "USDT"));
    System.out.println(ticker.toString());
   Trades trades= marketDataService.getTrades(new CurrencyPair("BTC", "USDT"),10);
    System.out.println(trades);
    //assertThat(ticker).isNotNull();
  }

}
