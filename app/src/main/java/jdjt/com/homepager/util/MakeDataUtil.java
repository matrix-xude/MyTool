package jdjt.com.homepager.util;

import java.util.ArrayList;
import java.util.List;

import jdjt.com.homepager.domain.HomeFirstModuleBean;
import jdjt.com.homepager.domain.HomeFirstModuleItemBean;
import jdjt.com.homepager.domain.HomeVerticalBannerBean;
import jdjt.com.homepager.domain.SimpleString;

/**
 * Created by xxd on 2018/9/4.
 */

public class MakeDataUtil {

    // 制造首页模块假数据
    public static List<HomeFirstModuleBean> getHolidayData() {
        List<HomeFirstModuleBean> list = new ArrayList<>();

        HomeFirstModuleBean collection1 = new HomeFirstModuleBean();
        collection1.setName("度假酒店");
        collection1.setType(1);
        List<HomeFirstModuleItemBean> itemList1 = new ArrayList<>();
        collection1.setList(itemList1);
        itemList1.add(new HomeFirstModuleItemBean("度假世界"));
        itemList1.add(new HomeFirstModuleItemBean("亲子酒店"));
        itemList1.add(new HomeFirstModuleItemBean("民宿客栈"));
        itemList1.add(new HomeFirstModuleItemBean("城市休闲"));
        itemList1.add(new HomeFirstModuleItemBean("海滨酒店"));
        itemList1.add(new HomeFirstModuleItemBean("精品酒店"));
        list.add(collection1);

        HomeFirstModuleBean collection2 = new HomeFirstModuleBean();
        collection2.setName("度假套餐");
        collection2.setType(2);
        List<HomeFirstModuleItemBean> itemList2 = new ArrayList<>();
        collection2.setList(itemList2);
        itemList2.add(new HomeFirstModuleItemBean("亲子套餐"));
        itemList2.add(new HomeFirstModuleItemBean("美食套餐"));
        itemList2.add(new HomeFirstModuleItemBean("游玩套餐"));
        itemList2.add(new HomeFirstModuleItemBean("游览套餐"));
        list.add(collection2);

        HomeFirstModuleBean collection3 = new HomeFirstModuleBean();
        collection3.setName("猫超市");
        collection3.setType(3);
        List<HomeFirstModuleItemBean> itemList3 = new ArrayList<>();
        collection3.setList(itemList3);
        itemList3.add(new HomeFirstModuleItemBean("猫玩乐"));
        itemList3.add(new HomeFirstModuleItemBean("现代生活MALL"));
        list.add(collection3);

        HomeFirstModuleBean collection4 = new HomeFirstModuleBean();
        collection4.setName("旅游");
        collection4.setType(4);
        List<HomeFirstModuleItemBean> itemList4 = new ArrayList<>();
        collection4.setList(itemList4);
        itemList4.add(new HomeFirstModuleItemBean("国内游"));
        itemList4.add(new HomeFirstModuleItemBean("出境游"));
        list.add(collection4);

        return list;
    }

    public static List<HomeVerticalBannerBean> makeHeadlineData() {
        List<HomeVerticalBannerBean> list = new ArrayList<>();
        list.add(new HomeVerticalBannerBean("红树林电影生活空间亮相啦", "这件让周杰伦惊叹的事"));
        list.add(new HomeVerticalBannerBean("据香港《文汇报》10日报道，韩国疾病管理本部9日公布，当局日前证实一名61岁男子", "这是韩国继2015年疫情结束后，首次出现新病例"));
        list.add(new HomeVerticalBannerBean("在网上被骗了5000元，便匆匆忙忙上网搜寻“网络警察”", "试图追回损失，不料又被“网络警察”骗走50000元"));
        return list;
    }

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
