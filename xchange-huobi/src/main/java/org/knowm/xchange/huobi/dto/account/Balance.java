package org.knowm.xchange.huobi.dto.account;

import java.math.BigDecimal;

/**
 * @Author ISME
 * @Date 2018/1/14
 * @Time 16:15
 */

public class Balance<T> {
    /**
     * id : 100009
     * type : spot
     * state : working
     * list : [{"currency":"usdt","type":"trade","balance":"500009195917.4362872650"}]
     * user-id : 1000
     */

    private BigDecimal id;
    private String type;
    private String state;
    private String userid;
    private T list;

    public BigDecimal getId() {         return id;     }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public T getList() {
        return list;
    }

    public void setList(T list) {
        this.list = list;
    }
}
