package org.knowm.xchange.huobi.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.huobi.HuobiAdapters;
import org.knowm.xchange.huobi.dto.marketdata.Depth;
import org.knowm.xchange.huobi.dto.marketdata.HistoryTradess;
import org.knowm.xchange.huobi.dto.marketdata.Merged;
import org.knowm.xchange.huobi.dto.marketdata.MergedResponse;
import org.knowm.xchange.huobi.dto.meta.Symbol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/20.
 */
public class HuobiMarketDataServiceRaw extends  HuobiBaseService {
    protected HuobiMarketDataServiceRaw(Exchange exchange) {
        super(exchange);
    }

    public Merged getHuobiTicker(String symbol){
        MergedResponse<Merged> mergedResponse= huobiAuthenticated.merged(symbol);
        return  mergedResponse.getTick();
    }

    public List<HistoryTradess> getHuobiTrades(String symbol, Integer size){
         return huobiAuthenticated.historyTrade(symbol,size).getData();
    }

    public Depth getHuobiOrderBook(String symbol, Integer limitBids, Integer limitAsks) {
         return huobiAuthenticated.depth(symbol,"step1").getTick();
    }

    public List<CurrencyPair> getExchangeSymbols() throws IOException {
        List<CurrencyPair> currencyPairs = new ArrayList<>();
        for (Symbol symbol : huobiAuthenticated.symbols().getData()) {
            currencyPairs.add(HuobiAdapters.adaptCurrencyPair(symbol));
        }
        return currencyPairs;
    }



}
