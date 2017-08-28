package com.example.cne_shop.bean;

/**
 * Created by 博 on 2017/7/25.
 */

public class OrderData {

    private String orderNum ;
    private Object charge ;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Object getCharge() {
        return charge;
    }

    public void setCharge(Object charge) {
        this.charge = charge;
    }
}
