package jdjt.com.homepager.domain.back;

/**
 * Created by xxd on 2018/9/28.
 * 城市搜索返回
 */

public class BackCitySearch {

    private String id; //目的地id
    private String regionName; // 目的地名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
