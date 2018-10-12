package jdjt.com.homepager.domain.send;

/**
 * Created by xxd on 2018/10/12.
 * 酒店条件查询的入参
 */

public class SendHotelSearchLevel3 {

    private String paramCode; // 类型编码
    private String paramName; // 类型名称

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
}
