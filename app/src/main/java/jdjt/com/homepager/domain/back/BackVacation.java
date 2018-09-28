package jdjt.com.homepager.domain.back;

import java.util.List;

/**
 * Created by xxd on 2018/9/28.
 * 首页最下面的度假套餐、酒店
 */

public class BackVacation {

    private String type;  //类型0-度假套餐， 1-度假酒店
    private String title;  //分类标题
    private List<BackVacationLevel> dataList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<BackVacationLevel> getDataList() {
        return dataList;
    }

    public void setDataList(List<BackVacationLevel> dataList) {
        this.dataList = dataList;
    }
}
