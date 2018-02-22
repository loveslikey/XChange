package org.knowm.xchange.huobi.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.huobi.dto.HuobiResult;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2018/2/19.
 */
public class HuobiKlineResult extends HuobiResult<List<HuobiKline>> {
    public HuobiKlineResult(@JsonProperty("tick") List<HuobiKline> tick, @JsonProperty("status") String status, @JsonProperty("ts") BigDecimal ts, @JsonProperty("ch") String ch) {
        super(tick, status, ts, ch);
    }
}
