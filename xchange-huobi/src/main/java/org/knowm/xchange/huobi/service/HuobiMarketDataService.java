package org.knowm.xchange.huobi.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.huobi.HuobiAdapters;
import org.knowm.xchange.huobi.dto.marketdata.Depth;
import org.knowm.xchange.huobi.dto.marketdata.HistoryTradess;
import org.knowm.xchange.huobi.utils.HuobiUtils;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/2/20.
 */
public class HuobiMarketDataService extends HuobiMarketDataServiceRaw implements MarketDataService {

    public HuobiMarketDataService(Exchange exchange) {
        super(exchange);
    }

    @Override
    public Ticker getTicker(CurrencyPair currencyPair, Object... args) throws IOException {
        return HuobiAdapters.adaptTicker(getHuobiTicker(HuobiUtils.toPairString(currencyPair)), currencyPair);
    }



    /**
     * @param args If two integers are provided, then those count as limit bid and limit ask count
     */
    @Override
    public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args) throws IOException {

        // null will cause fetching of full order book, the default behavior in XChange
        Integer limitBids = null;
        Integer limitAsks = null;

        if (args != null && args.length == 2) {
            Object arg0 = args[0];
            if (!(arg0 instanceof Integer)) {
                throw new ExchangeException("Argument 0 must be an Integer!");
            } else {
                limitBids = (Integer) arg0;
            }
            Object arg1 = args[1];
            if (!(arg1 instanceof Integer)) {
                throw new ExchangeException("Argument 1 must be an Integer!");
            } else {
                limitAsks = (Integer) arg1;
            }
        }

        Depth huobiDepth = getHuobiOrderBook(HuobiUtils.toPairString(currencyPair), limitBids, limitAsks);

        OrderBook orderBook = HuobiAdapters.adaptOrderBook(huobiDepth, currencyPair);

        return orderBook;
    }


    /**
     * @param currencyPair The CurrencyPair for which to query trades.
     * @param args         获取交易记录的数量 默认1  [1, 2000]
     *                     The argument may be of type java.util.Date or Number (milliseconds since Jan 1, 1970)
     */
    @Override
    public Trades getTrades(CurrencyPair currencyPair, Object... args) throws IOException {

        Integer size = 1;
        if (args != null && args.length == 1) {
            // parameter 1, if present, is the last trade timestamp
            if (args[0] instanceof Number) {
                Number arg = (Number) args[0];
                if (arg.longValue() > 2000) {
                    throw new IllegalArgumentException("火币不支持时间过滤,只支持前2000条数据");
                }
                size = arg.intValue();
                //lastTradeTime = arg.longValue() / 1000; // divide by 1000 to convert to unix timestamp (seconds)
            } else if (args[0] instanceof Date) {
                throw new IllegalArgumentException("火币不支持时间过滤");
            } else {
                throw new IllegalArgumentException(
                        "Extra argument #1, the last trade time, must be a Date or Long (millisecond timestamp) (was " + args[0].getClass() + ")");
            }
        }
        List<HistoryTradess> historyTradess = getHuobiTrades(HuobiUtils.toPairString(currencyPair), size);

        return HuobiAdapters.adaptTrades(historyTradess, currencyPair);
    }
}
