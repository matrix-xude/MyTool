package jdjt.com.homepager.domain;

import java.io.Serializable;

/**
 * Created by xxd on 2018/9/3.
 */

public class SimpleString implements Serializable {

    private String name;
    private int type;
    private boolean isChoice;
    private String id;

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

    public boolean isChoice() {
        return isChoice;
    }

    public void setChoice(boolean choice) {
        isChoice = choice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
