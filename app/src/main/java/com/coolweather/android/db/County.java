package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by admin on 2017/12/27.
 */

public class County extends DataSupport {
    private int id;

    private String countyName;      //县名

    private String weatherId;       //县所对应的天气Id

    private int cityId;            //记录当前所属市的id值

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

}
