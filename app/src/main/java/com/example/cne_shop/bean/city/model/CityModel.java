package com.example.cne_shop.bean.city.model;

import java.util.List;

/**
 * Created by 博 on 2017/7/27.
 */

public class CityModel {

    private String name ;
    private List<DistrictModel> districtModels ;

    public List<DistrictModel> getDistrictModels() {
        return districtModels;
    }

    public void setDistrictModels(List<DistrictModel> districtModels) {
        this.districtModels = districtModels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
