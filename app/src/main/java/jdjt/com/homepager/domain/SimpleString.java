package jdjt.com.homepager.domain;

import java.io.Serializable;

/**
 * Created by xxd on 2018/9/3.
 */

public class SimpleString implements Serializable {

    private String name;
    private int type;

    public SimpleString() {
    }

    public SimpleString(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
