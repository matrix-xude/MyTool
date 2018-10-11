package jdjt.com.homepager.domain;

import java.io.Serializable;

/**
 * Created by xxd on 2018/10/11.
 * 筛选酒店类型传参用的
 */

public class HotelType implements Serializable {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
