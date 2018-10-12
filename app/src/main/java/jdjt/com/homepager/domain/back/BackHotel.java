package jdjt.com.homepager.domain.back;

/**
 * Created by xxd on 2018/10/12.
 * 数据全的酒店类型
 */

public class BackHotel {

    private String hotelCode;
    private String hotelName;
    private String hotelHeadImage;
    private String hotelAddress;
    private String hotelScore;  // 评分
    private String roomTypePriceMin; // 最低现金价格
    private String scoreCount; // 评论条数
    private String longitude;
    private String latitude;
    private String isGhost; // 是否开通呼叫服务  0:否 1:是
    private String is3DMap; // 是否开通3D地图     0:否 1:是
    private String isFreeWifi; // 是否有免费WIFI  0:否 1:是
    private String hotelType;  // 0：红树林系列酒店  1：加盟其他酒店

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelHeadImage() {
        return hotelHeadImage;
    }

    public void setHotelHeadImage(String hotelHeadImage) {
        this.hotelHeadImage = hotelHeadImage;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public String getHotelScore() {
        return hotelScore;
    }

    public void setHotelScore(String hotelScore) {
        this.hotelScore = hotelScore;
    }

    public String getRoomTypePriceMin() {
        return roomTypePriceMin;
    }

    public void setRoomTypePriceMin(String roomTypePriceMin) {
        this.roomTypePriceMin = roomTypePriceMin;
    }

    public String getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(String scoreCount) {
        this.scoreCount = scoreCount;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getIsGhost() {
        return isGhost;
    }

    public void setIsGhost(String isGhost) {
        this.isGhost = isGhost;
    }

    public String getIs3DMap() {
        return is3DMap;
    }

    public void setIs3DMap(String is3DMap) {
        this.is3DMap = is3DMap;
    }

    public String getIsFreeWifi() {
        return isFreeWifi;
    }

    public void setIsFreeWifi(String isFreeWifi) {
        this.isFreeWifi = isFreeWifi;
    }

    public String getHotelType() {
        return hotelType;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }
}
