package com.example.cne_shop.bean.msg;

/**
 * Created by 博 on 2017/7/22.
 */

public class LoginRespMsg<T> extends BaseRespMsg {

    private String token ;

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    private T data ;

    public String getTocken() {
        return token;
    }

    public void setTocken(String tocken) {
        this.token = tocken;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
