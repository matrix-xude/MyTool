package jdjt.com.homepager.domain.back;

import java.util.List;

/**
 * Created by xxd on 2018/9/28.
 */

public class BackHotRecommend {

    private String type; // 类型 1：热门目的地 2：热门度假区 3：大家都爱去
    private String name; // 标题名称
    private List<BackHotRecommendLevel> children;
    private List<BackChinaCity> chinaCityList;  // 所有城市，首页没有，目的地页面才有

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BackHotRecommendLevel> getChildren() {
        return children;
    }

    public void setChildren(List<BackHotRecommendLevel> children) {
        this.children = children;
    }

    public List<BackChinaCity> getChinaCityList() {
        return chinaCityList;
    }

    public void setChinaCityList(List<BackChinaCity> chinaCityList) {
        this.chinaCityList = chinaCityList;
    }
}
