package org.knowm.xchange.hitbtc.v2;

import org.junit.BeforeClass;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;

public class BaseServiceTest {

  protected static ExchangeSpecification exchangeSpecification;
  protected static Exchange exchange;

  @BeforeClass
  public static void setUpBaseClass() {
    exchangeSpecification = new ExchangeSpecification(HitbtcExchange.class);
    exchange = ExchangeFactory.INSTANCE.createExchange(exchangeSpecification);
  }

  public static Exchange exchange() {
    return exchange;
  }
}
