package com.example.cne_shop.bean;

import java.io.Serializable;

/**
 * Created by 博 on 2017/7/14.
 */

public class ShoppingCart extends Ware implements Serializable {

    private int count ;
    private Boolean isChecked = true ;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
