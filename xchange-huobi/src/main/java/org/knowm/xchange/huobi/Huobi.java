package org.knowm.xchange.huobi;


import org.knowm.xchange.huobi.dto.marketdata.*;
import org.knowm.xchange.huobi.dto.meta.CurrencysResponse;
import org.knowm.xchange.huobi.dto.meta.Symbol;
import org.knowm.xchange.huobi.dto.meta.SymbolsResponse;
import org.knowm.xchange.huobi.dto.meta.TimestampResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public interface Huobi {

    @GET
    @Path("v1/common/timestamp")
    public TimestampResponse time() throws ApiException;

    @GET
    @Path("v1/common/currencys")
    CurrencysResponse currencys()  throws ApiException;

    @GET
    @Path("v1/common/symbols")
    SymbolsResponse<List<Symbol>> symbols()  throws ApiException;

    /**
     *
     * @param symbol 交易对 btcusdt, bchbtc, rcneth
     * @param period K线类型 1min, 5min, 15min, 30min, 60min, 1day, 1mon, 1week, 1year
     * @param size 获取数量 默认150 [1,2000]
     * @return
     * @throws ApiException
     */
    @GET
    @Path("market/history/kline")
    KlineResponse<List<Kline>> kline(@QueryParam("symbol") String symbol, @QueryParam("period") String period, @QueryParam("size") Integer size) throws ApiException;


    /**
     *
     * @param symbol  交易对 btcusdt, bchbtc, rcneth ...
     * @return
     * @throws ApiException
     */
    @GET
    @Path("market/detail/merged")
    MergedResponse<Merged> merged(@QueryParam("symbol") String symbol) throws ApiException;


    /**
     *
     * @param symbol 交易对 btcusdt, bchbtc, rcneth
     * @param type   Depth 类型 step0, step1, step2, step3, step4, step5（合并深度0-5）；step0时，不合并深度
     * @return
     * @throws ApiException
     */
    @GET
    @Path("market/depth")
    DepthResponse depth(@QueryParam("symbol") String symbol, @QueryParam("type") String type) throws ApiException;

    /**
     *
     * @param symbol  交易对 btcusdt, bchbtc, rcneth
     * @return
     * @throws ApiException
     */
    @GET
    @Path("market/trade ")
    TradeResponse trade(@QueryParam("symbol") String symbol) throws ApiException;


    /**
     *
     * @param symbol 交易对 btcusdt, bchbtc, rcneth
     * @param size  获取交易记录的数量  1 [1, 2000]
     * @return
     * @throws ApiException
     */
    @GET
    @Path("market/history/trade")
    HistoryTradeResponse<List<HistoryTradess>>  historyTrade(@QueryParam("symbol") String symbol, @QueryParam("size") Integer size) throws ApiException;

    /**
     *
     * @param symbol  交易对 btcusdt, bchbtc, rcneth
     * @return
     * @throws ApiException
     */
    @GET
    @Path("market/detail")
    DetailResponse<Details>  detail(@QueryParam("symbol") String symbol) throws ApiException;











}
