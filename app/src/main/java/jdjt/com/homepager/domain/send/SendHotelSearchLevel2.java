package jdjt.com.homepager.domain.send;

import java.util.List;

/**
 * Created by xxd on 2018/10/12.
 * 酒店条件查询的入参
 */

public class SendHotelSearchLevel2 {

    // 此参数需要根据上级的类型确定，如果是2 内容是“国内热门城市 国内城市 热门度假区 热门景点”,如果是4，内容是“ 酒店类型 ”
    // 定位出来的点没有title,后台默认为 国内城市
    private String title;
    private List<SendHotelSearchLevel3> content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SendHotelSearchLevel3> getContent() {
        return content;
    }

    public void setContent(List<SendHotelSearchLevel3> content) {
        this.content = content;
    }
}
