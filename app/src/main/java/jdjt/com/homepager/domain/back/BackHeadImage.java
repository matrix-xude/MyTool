package jdjt.com.homepager.domain.back;

/**
 * Created by xxd on 2018/9/28.
 * 所有首页轮播图的公共
 * 2:酒店首页;3:度假套餐首页;4:MVM首页;5旅游首页;6:猫超市首页
 */

public class BackHeadImage {

    private String imageUrl; //图片地址
    private String linkUrl; //链接地址
    private String title; //图片标题
    private String refId; //关联id 如商品id,酒店id,业态id
    private String parentTypeId; //一级类型id
    private String childTypeId; //二级类型id

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getParentTypeId() {
        return parentTypeId;
    }

    public void setParentTypeId(String parentTypeId) {
        this.parentTypeId = parentTypeId;
    }

    public String getChildTypeId() {
        return childTypeId;
    }

    public void setChildTypeId(String childTypeId) {
        this.childTypeId = childTypeId;
    }
}