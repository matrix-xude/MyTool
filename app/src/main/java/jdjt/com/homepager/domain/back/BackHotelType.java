package jdjt.com.homepager.domain.back;

import java.util.List;

/**
 * Created by xxd on 2018/9/28.
 * 酒店列表页， 3：酒店类型 4:排序规则  可表示2个
 */

public class BackHotelType {

    private String paramType; // 3：酒店类型 4:排序规则
    private List<BackHotelTypeLevel> paramContent;

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public List<BackHotelTypeLevel> getParamContent() {
        return paramContent;
    }

    public void setParamContent(List<BackHotelTypeLevel> paramContent) {
        this.paramContent = paramContent;
    }
}
