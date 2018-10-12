package jdjt.com.homepager.domain.back;

/**
 * Created by xxd on 2018/9/28.
 * 酒店列表页， 3：酒店类型 4:排序规则  可表示2个
 */

public class BackHotelTypeLevel {

    private String paramCode; // 类型code
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
