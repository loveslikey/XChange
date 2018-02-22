package org.knowm.xchange.huobi.dto.meta;

/**
 * Created by Administrator on 2018/2/19.
 */
public class HuobiSymbol {


    //	基础币种
    private String baseCurrency;
    //计价币种
    private String quoteCurrency;
    //价格精度位数（0为个位）
    private String pricePrecision;
    //	数量精度位数（0为个位）
    private String amountPrecision;
    //交易区	main主区，innovation创新区，bifurcation分叉区
    private String symbolPartition;
    private String status;


}
