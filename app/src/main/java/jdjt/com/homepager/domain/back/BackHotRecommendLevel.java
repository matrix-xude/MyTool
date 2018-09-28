package jdjt.com.homepager.domain.back;

/**
 * Created by xxd on 2018/9/28.
 */

public class BackHotRecommendLevel {

    private String refId; // 类型 ： 1 ，3 表示 城市id 类型：2 表示 度假区id
    private String name; // 类型 ： 1 ，3 是城市名称 类型：2 是度假区名称
    private String image; // 首页才有，第二页只有文字
    private String type; // 首页才有，第二页只有文字  类型 1：热门目的地 2：热门度假区 3：大家都爱去

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
