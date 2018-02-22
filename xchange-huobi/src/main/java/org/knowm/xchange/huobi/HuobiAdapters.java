package org.knowm.xchange.huobi;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.CurrencyPairMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.huobi.dto.marketdata.*;
import org.knowm.xchange.huobi.dto.meta.Symbol;
import org.knowm.xchange.utils.DateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/20.
 */
public class HuobiAdapters {

    public static ExchangeMetaData adaptMetaData(List<CurrencyPair> currencyPairs, ExchangeMetaData metaData) {

        Map<CurrencyPair, CurrencyPairMetaData> pairsMap = metaData.getCurrencyPairs();
        Map<Currency, CurrencyMetaData> currenciesMap = metaData.getCurrencies();
        for (CurrencyPair c : currencyPairs) {
            if (!pairsMap.containsKey(c)) {
                pairsMap.put(c, null);
            }
            if (!currenciesMap.containsKey(c.base)) {
                currenciesMap.put(c.base, null);
            }
            if (!currenciesMap.containsKey(c.counter)) {
                currenciesMap.put(c.counter, null);
            }
        }

        return metaData;
    }

    public static Ticker adaptTicker(Merged huobiTicker, CurrencyPair currencyPair) {
        BigDecimal last = new BigDecimal(huobiTicker.getClose());
        BigDecimal bid = new BigDecimal(huobiTicker.getBid().get(0));
        BigDecimal ask = new BigDecimal(huobiTicker.getAsk().get(0));
        BigDecimal high =new BigDecimal( huobiTicker.getHigh());
        BigDecimal low = new BigDecimal(huobiTicker.getLow());
        BigDecimal volume =new BigDecimal( huobiTicker.getVol());
        Date timestamp = DateUtils.fromMillisUtc(huobiTicker.getTs());
        return new Ticker.Builder().currencyPair(currencyPair).last(last).bid(bid).ask(ask).high(high).low(low).volume(volume).timestamp(timestamp)
                .build();
    }

    public static Ticker adaptTicker(Details huobiTicker, CurrencyPair currencyPair) {
        BigDecimal last = new BigDecimal(huobiTicker.getClose());
        BigDecimal high =new BigDecimal( huobiTicker.getHigh());
        BigDecimal low = new BigDecimal(huobiTicker.getLow());
        BigDecimal volume =new BigDecimal( huobiTicker.getVol());
        Date timestamp = DateUtils.fromMillisUtc(huobiTicker.getTs());
        return new Ticker.Builder().currencyPair(currencyPair).last(last).high(high).low(low).volume(volume).timestamp(timestamp)
                .build();
    }

    public static Trades adaptTrades(List<HistoryTradess> historyTradessList, CurrencyPair currencyPair) {
        List<Trade> tradesList = new ArrayList<>(historyTradessList.size());
        BigDecimal lastTradeId = new BigDecimal(0);
        for (HistoryTradess  historyTradess: historyTradessList) {
            BigDecimal tradeId = historyTradess.getId();
            if (tradeId .compareTo(lastTradeId)>0) {
                lastTradeId = tradeId;
            }
            for(HistoryTrade historyTrade: historyTradess.getData()){
                tradesList.add(adaptTrade(historyTrade, currencyPair));
            }
        }
        return new Trades(tradesList, lastTradeId.longValueExact(), Trades.TradeSortType.SortByID);
    }

    public static Trade adaptTrade(HistoryTrade historyTrade, CurrencyPair currencyPair) {
         Order.OrderType orderType = historyTrade.getDirection().equals("buy") ? Order.OrderType.BID : Order.OrderType.ASK;
        BigDecimal amount = new BigDecimal(historyTrade.getAmount());
        BigDecimal price = new BigDecimal( historyTrade.getPrice());
        Date date = DateUtils.fromMillisUtc(historyTrade.getTs()); // Bitfinex uses Unix timestamps
        final String tradeId = String.valueOf(historyTrade.getId());
        return new Trade(orderType, amount, currencyPair, price, date, tradeId);
    }

    public static OrderBook adaptOrderBook(Depth huobiDepth, CurrencyPair currencyPair) {

        OrdersContainer asksOrdersContainer = adaptOrders(huobiDepth.getAsks(),Long.parseLong(huobiDepth.getTs()), currencyPair, Order.OrderType.ASK);
        OrdersContainer bidsOrdersContainer = adaptOrders(huobiDepth.getBids(),Long.parseLong(huobiDepth.getTs()), currencyPair, Order.OrderType.BID);
        return new OrderBook(new Date(Math.max(asksOrdersContainer.getTimestamp(), bidsOrdersContainer.getTimestamp())),
                asksOrdersContainer.getLimitOrders(), bidsOrdersContainer.getLimitOrders());
    }

    public static OrdersContainer adaptOrders(List<List<BigDecimal>> bigDecimalListList , Long ts,CurrencyPair currencyPair, Order.OrderType orderType) {
        List<LimitOrder> limitOrders = new ArrayList<>(bigDecimalListList.size());
        Date timestamp = DateUtils.fromMillisUtc(ts);
        for (List<BigDecimal> bigDecimalList : bigDecimalListList) {
            limitOrders.add(adaptOrder(bigDecimalList.get(1), bigDecimalList.get(0), currencyPair, orderType, timestamp));
        }

        return new OrdersContainer(ts, limitOrders);
    }

    public static LimitOrder adaptOrder(BigDecimal originalAmount, BigDecimal price, CurrencyPair currencyPair, Order.OrderType orderType, Date timestamp) {

        return new LimitOrder(orderType, originalAmount, currencyPair, "", timestamp, price);
    }

    public static CurrencyPair adaptCurrencyPair(Symbol symbol) {
        return new CurrencyPair(symbol.getBaseCurrency(), symbol.getQuoteCurrency());
    }



    public static String adaptBitfinexCurrency(String bitfinexSymbol) {
        String currency = bitfinexSymbol.toUpperCase();
        return currency;
    }

    public static class OrdersContainer {

        private final long timestamp;
        private final List<LimitOrder> limitOrders;

        /**
         * Constructor
         *
         * @param timestamp
         * @param limitOrders
         */
        public OrdersContainer(long timestamp, List<LimitOrder> limitOrders) {

            this.timestamp = timestamp;
            this.limitOrders = limitOrders;
        }

        public long getTimestamp() {

            return timestamp;
        }

        public List<LimitOrder> getLimitOrders() {

            return limitOrders;
        }
    }
}
