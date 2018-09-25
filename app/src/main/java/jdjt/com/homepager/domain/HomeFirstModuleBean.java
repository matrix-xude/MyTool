package jdjt.com.homepager.domain;

import java.util.List;

/**
 * Created by xxd on 2018/9/4.
 */

public class HomeFirstModuleBean {

    private int type;
    private String name;
    private List<HomeFirstModuleItemBean> list;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HomeFirstModuleItemBean> getList() {
        return list;
    }

    public void setList(List<HomeFirstModuleItemBean> list) {
        this.list = list;
    }
}
