package jdjt.com.homepager.domain;

import java.io.Serializable;

/**
 * Created by xxd on 2018/10/11.
 * 目的地页面选择参数传值
 */

public class HotelDestination implements Serializable {

    private String title; // 标题，后台区分
    private String id;
    private String name;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

