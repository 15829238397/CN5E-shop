package com.example.cne_shop.bean;

import java.util.List;

/**
 * Created by 博 on 2017/7/28.
 */

public class OrderMsg {

    private long id ;
    private String orderNum ;
    private String createdTime ;
    private int amount ;
    private int status ;
    private List<OrderItems> items ;
    private ConsigneeMsg consigneeMsg ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OrderItems> getItems() {
        return items;
    }

    public void setItems(List<OrderItems> items) {
        this.items = items;
    }

    public ConsigneeMsg getConsigneeMsg() {
        return consigneeMsg;
    }

    public void setConsigneeMsg(ConsigneeMsg consigneeMsg) {
        this.consigneeMsg = consigneeMsg;
    }
}
