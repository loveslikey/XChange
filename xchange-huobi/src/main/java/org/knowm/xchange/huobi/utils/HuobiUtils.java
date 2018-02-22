package org.knowm.xchange.huobi.utils;

import org.knowm.xchange.currency.CurrencyPair;

/**
 * A central place for shared Bitfinex properties
 */
public final class HuobiUtils {

  /**
   * private Constructor
   */
  private HuobiUtils() {

  }

  public static String adaptXchangeCurrency(String xchangeSymbol) {
    String currency = xchangeSymbol.toLowerCase();
    return currency;
  }

  public static String toPairString(CurrencyPair currencyPair) {

    return adaptXchangeCurrency(currencyPair.base.toString()) + adaptXchangeCurrency(currencyPair.counter.toString());
  }

}
