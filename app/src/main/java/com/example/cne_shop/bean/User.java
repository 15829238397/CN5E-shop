package com.example.cne_shop.bean;

/**
 * Created by 博 on 2017/7/22.
 */

public class User {

    private long id ;
    private String mobi ;
    private String email ;
    private String username ;
    private String logo_url ;
    private ConsigneeMsg defauteConsigen ;

    public long getId() {
        return id;
    }

    public ConsigneeMsg getDefauteConsigen() {
        return defauteConsigen;
    }

    public void setDefauteConsigen(ConsigneeMsg defauteConsigen) {
        this.defauteConsigen = defauteConsigen;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMobi() {
        return mobi;
    }

    public void setMobi(String mobi) {
        this.mobi = mobi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }
}
