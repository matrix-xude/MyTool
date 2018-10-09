package jdjt.com.homepager.util;

import java.util.ArrayList;
import java.util.List;

import jdjt.com.homepager.domain.SimpleString;

/**
 * Created by xxd on 2018/9/4.
 */

public class MakeDataUtil {

    public static List<SimpleString> makeClassifyString() {
        List<SimpleString> list = new ArrayList<>();
        list.add(new SimpleString("度假世界"));
        list.add(new SimpleString("亲子酒店"));
        list.add(new SimpleString("民宿客栈"));
        list.add(new SimpleString("城市休闲"));
        list.add(new SimpleString("海滨酒店"));
        list.add(new SimpleString("会展酒店"));
        list.add(new SimpleString("房车营地"));
        list.add(new SimpleString("111"));
        list.add(new SimpleString("22"));
        list.add(new SimpleString("33"));
        return list;
    }

    public static List<SimpleString> makeSimpleString(int total) {
        List<SimpleString> list = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            SimpleString ss = new SimpleString();
            ss.setName("条目" + i);
            ss.setType(i % 2);
            list.add(ss);
        }
        return list;
    }

    public static List<SimpleString> makeSimpleString(int total, String name) {
        List<SimpleString> list = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            SimpleString ss = new SimpleString();
            ss.setName(name + i);
            ss.setType(i % 2);
            list.add(ss);
        }
        return list;
    }
}
