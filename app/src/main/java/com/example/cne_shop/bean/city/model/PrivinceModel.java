package com.example.cne_shop.bean.city.model;

import java.util.List;

/**
 * Created by 博 on 2017/7/27.
 */

public class PrivinceModel {

    private String name ;
    private List<CityModel> cityModels ;

    public List<CityModel> getCityModels() {
        return cityModels;
    }

    public void setCityModels(List<CityModel> cityModels) {
        this.cityModels = cityModels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
