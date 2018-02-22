package org.knowm.xchange.huobi.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/2/19.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HuobiKline {
    //K线id
    private String id;
    //成交量
    private String amount;
    //成交笔数
    private String count;
    //开盘价
    private String open;
    //收盘价,当K线为最晚的一根时，是最新成交价
    private String close;
    //最低价,
    private String low;
    //最高价,
    private String high;
    //成交额, 即 sum(每一笔成交价 * 该笔的成交量)
    private String vol;

    public HuobiKline(String id, String amount, String count, String open, String close, String low, String high, String vol) {
        this.id = id;
        this.amount = amount;
        this.count = count;
        this.open = open;
        this.close = close;
        this.low = low;
        this.high = high;
        this.vol = vol;
    }

    public String getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public String getCount() {
        return count;
    }

    public String getOpen() {
        return open;
    }

    public String getClose() {
        return close;
    }

    public String getLow() {
        return low;
    }

    public String getHigh() {
        return high;
    }

    public String getVol() {
        return vol;
    }
}
