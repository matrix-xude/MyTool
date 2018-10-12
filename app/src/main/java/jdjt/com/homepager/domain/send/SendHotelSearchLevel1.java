package jdjt.com.homepager.domain.send;

import java.util.List;

/**
 * Created by xxd on 2018/10/12.
 * 酒店条件查询的入参
 */

public class SendHotelSearchLevel1 {

    private String paramType; // 分类信息 2:目的地3：酒店类型4排序规则
    private List<SendHotelSearchLevel2> paramContent;

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public List<SendHotelSearchLevel2> getParamContent() {
        return paramContent;
    }

    public void setParamContent(List<SendHotelSearchLevel2> paramContent) {
        this.paramContent = paramContent;
    }
}
