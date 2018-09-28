package jdjt.com.homepager.domain.back;

/**
 * Created by xxd on 2018/9/28.
 */

public class BackVacationLevel {

    private String commentNum;  //评论数
    private String sellerId;  //酒店id
    private String productId;  //商品id
    private String hasChanged;  //已付款人数
    private String price;  //价格
    private String imageUrl;  //图片地址
    private String groupId;  //组id
    private String productFirstId;  //商品一级分类id
    private String grade;  //评分
    private String name;  //名称
    private String type;  //类型
    private String productType;  //商品类型0-新零售,1-美食，2-包车，3-门票
    private String url;  //广告专用跳转地址
    private String discountMoney;  //优惠金额

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getHasChanged() {
        return hasChanged;
    }

    public void setHasChanged(String hasChanged) {
        this.hasChanged = hasChanged;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getProductFirstId() {
        return productFirstId;
    }

    public void setProductFirstId(String productFirstId) {
        this.productFirstId = productFirstId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(String discountMoney) {
        this.discountMoney = discountMoney;
    }
}
