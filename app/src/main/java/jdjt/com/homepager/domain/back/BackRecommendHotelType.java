package jdjt.com.homepager.domain.back;

/**
 * Created by xxd on 2018/9/28.
 * 酒店频道页，推荐酒店类型
 */

public class BackRecommendHotelType {

    private String paramCode; // 类型code
    private String paramName; // 类型名称
    private String imageUrl;
    private String seq; // 排序

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }
}
