package jdjt.com.homepager.domain;

import java.util.List;

import jdjt.com.homepager.domain.back.BackHotRecommendLevel;

/**
 * Created by xxd on 2018/9/25.
 * 目的地页面热门条目封装
 */

public class HotCityItem {

    private String title;
    private List<BackHotRecommendLevel> list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<BackHotRecommendLevel> getList() {
        return list;
    }

    public void setList(List<BackHotRecommendLevel> list) {
        this.list = list;
    }
}
