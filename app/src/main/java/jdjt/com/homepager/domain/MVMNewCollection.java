package jdjt.com.homepager.domain;

import java.util.List;

import jdjt.com.homepager.domain.back.BackMVMNew;

/**
 * Created by xxd on 2018/10/16.
 * 组合猫头条的数据
 */

public class MVMNewCollection {

    private List<BackMVMNew> list;

    public List<BackMVMNew> getList() {
        return list;
    }

    public void setList(List<BackMVMNew> list) {
        this.list = list;
    }
}
