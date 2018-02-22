package org.knowm.xchange.huobi.dto.marketdata;

import java.math.BigDecimal;

/**
 * @Author ISME
 * @Date 2018/1/14
 * @Time 11:35
 */

public class Kline {


    private BigDecimal id;
    private double amount;
    private int count;
    private double open;
    private int close;
    private int low;
    private int high;
    private double vol;

    public BigDecimal getId() {         return id;     }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public int getClose() {
        return close;
    }

    public void setClose(int close) {
        this.close = close;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public double getVol() {
        return vol;
    }

    public void setVol(double vol) {
        this.vol = vol;
    }
}
