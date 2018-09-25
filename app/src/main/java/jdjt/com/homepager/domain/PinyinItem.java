package jdjt.com.homepager.domain;

/**
 * Created by xxd on 2018/9/20.
 */

public class PinyinItem {

    private String name;  // 排序用的名字
    private String letter; // 首字母
    private int type;  // 类型   0：拼音排序类型  1：其他类型，可以自己确定，不会用到name,letter
    private Object object; // 真正的数据

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
