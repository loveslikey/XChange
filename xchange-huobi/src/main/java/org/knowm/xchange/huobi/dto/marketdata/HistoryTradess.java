package org.knowm.xchange.huobi.dto.marketdata;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author ISME
 * @Date 2018/1/14
 * @Time 15:25
 */

public class HistoryTradess {
    /**
     * id : 31459998
     * ts : 1502448920106
     * data : [{"id":17592256642623,"amount":0.04,"price":1997,"direction":"buy","ts":1502448920106}]
     */

    private BigDecimal id;
    private long ts;
    private List<HistoryTrade> data;


    public BigDecimal getId() {         return id;     }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }


    public List<HistoryTrade> getData() {
        return data;
    }

    public void setData(List<HistoryTrade> data) {
        this.data = data;
    }
}
