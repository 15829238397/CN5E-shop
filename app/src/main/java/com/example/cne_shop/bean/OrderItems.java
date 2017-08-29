package com.example.cne_shop.bean;

/**
 * Created by 博 on 2017/7/28.
 */

public class OrderItems {

    private int id ;
    private float amount ;
    private int orderId ;
    private int ware_id ;
    private Ware wares ;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getWare_id() {
        return ware_id;
    }

    public void setWare_id(int ware_id) {
        this.ware_id = ware_id;
    }

    public Ware getWares() {
        return wares;
    }

    public void setWares(Ware wares) {
        this.wares = wares;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }


}
