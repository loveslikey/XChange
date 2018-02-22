package org.knowm.xchange.huobi;


import org.knowm.xchange.*;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.huobi.service.HuobiAccountService;
import org.knowm.xchange.huobi.service.HuobiMarketDataService;
import org.knowm.xchange.huobi.service.HuobiMarketDataServiceRaw;
import org.knowm.xchange.huobi.service.HuobiTradeService;
import org.knowm.xchange.utils.AuthUtils;
import org.knowm.xchange.utils.nonce.AtomicLongCurrentTimeIncrementalNonceFactory;

import java.io.IOException;
import java.util.List;

/**
 * Entry point to the XChange APIs.
 */
public class HuobiExchange extends BaseExchange implements Exchange {

	private static final int DEFAULT_PRECISION = 8;

	private SynchronizedValueFactory<Long> nonceFactory = new AtomicLongCurrentTimeIncrementalNonceFactory();
	private Long deltaServerTime;

	@Override
	protected void initServices() {
		this.marketDataService = new HuobiMarketDataService(this);
		this.tradeService = new HuobiTradeService(this);
		this.accountService = new HuobiAccountService(this);
	}


	@Override
	public ExchangeSpecification getDefaultExchangeSpecification() {
		ExchangeSpecification spec = new ExchangeSpecification(this.getClass().getCanonicalName());
		spec.setSslUri("https://api.huobi.pro");
		spec.setHost("api.huobi.pro");
		spec.setPort(443);
		spec.setExchangeName("Huobi");
		spec.setExchangeDescription("Huobi Exchange.");
		AuthUtils.setApiAndSecretKey(spec, "Huobi");
		return spec;
	}


	@Override
	public SynchronizedValueFactory<Long> getNonceFactory() {

		return nonceFactory;
	}

	@Override
	public void remoteInit() throws IOException, ExchangeException {
		HuobiMarketDataServiceRaw dataService = (HuobiMarketDataServiceRaw) this.marketDataService;
		List<CurrencyPair> currencyPairs = dataService.getExchangeSymbols();
		exchangeMetaData = HuobiAdapters.adaptMetaData(currencyPairs, exchangeMetaData);

	}

	public long deltaServerTime() throws IOException {
		if (deltaServerTime == null) {
			Huobi huobi = RestProxyFactory.createProxy(Huobi.class, getExchangeSpecification().getSslUri());
			deltaServerTime = huobi.time().getData() - System.currentTimeMillis();
		}
		return deltaServerTime;
	}

}
