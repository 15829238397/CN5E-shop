package com.example.cne_shop.bean;

/**
 * Created by 博 on 2017/7/25.
 */

public class SubmitOrderMsg {

    private int status ;
    private String message ;
    private OrderData data ;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrderData getData() {
        return data;
    }

    public void setData(OrderData data) {
        this.data = data;
    }

}
