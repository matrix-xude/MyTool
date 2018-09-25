package jdjt.com.homepager.framgnet;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vondear.rxtool.RxImageTool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdjt.com.homepager.R;
import jdjt.com.homepager.decoration.CommonDecoration;
import jdjt.com.homepager.decoration.PinYinDecoration;
import jdjt.com.homepager.domain.HotCityItem;
import jdjt.com.homepager.domain.PinyinItem;
import jdjt.com.homepager.util.PinYinUtil;
import jdjt.com.homepager.util.ViewUtil;
import jdjt.com.homepager.view.PinYinSideBar;
import jdjt.com.homepager.view.commonRecyclerView.CommonAdapterMultiple;
import jdjt.com.homepager.view.commonRecyclerView.CommonAdapterRecycler;
import jdjt.com.homepager.view.commonRecyclerView.CommonViewHolderRecycler;
import jdjt.com.homepager.view.commonRecyclerView.MultipleTypeSupport;

/**
 * Created by xxd on 2018/9/20.
 * 城市选择
 */

public class HotelCityFragment extends BaseFragment {

    private String[] citys = new String[]{"保定", "潮汕", "大连", "大同", "朝阳", "三亚", "北京", "阿克苏", "鞍山", "宝鸡", "沧州", "沧州", "沧州"
            , "重庆", "毕节", "锦州", "哈密", "鄂尔多斯", "六盘水", "花果山", "布宜诺斯艾利斯", "耶路撒冷", "毛里求斯", "！！！", "fff", "火星"
            , "M78星云", "三体星", "二向箔",};
    private List<PinyinItem> dataList;

    private RecyclerView recycler_fragment_hotel_city;
    private PinYinSideBar pinyin_side_bar_fragment_hotel_city;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_hotel_city;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
        initView();
        initData();
    }


    private void initView() {
        recycler_fragment_hotel_city = view.findViewById(R.id.recycler_fragment_hotel_city);
        pinyin_side_bar_fragment_hotel_city = view.findViewById(R.id.pinyin_side_bar_fragment_hotel_city);
    }

    private void initData() {
        dataList = sortCity();

        final PinyinItem item = new PinyinItem();
        item.setType(1); // 其他类型，并非拼音
        item.setLetter("热");
        HotCityItem hotCityItem = new HotCityItem();
        hotCityItem.setTitle("热门城市");
        List<String> cityList = new ArrayList<>();
        cityList.add("北京");
        cityList.add("上海");
        cityList.add("三亚");
        cityList.add("非洲");
        cityList.add("亚细亚");
        cityList.add("欧罗巴");
        cityList.add("香港");
        cityList.add("额");
        hotCityItem.setList(cityList);
        item.setObject(hotCityItem);
        dataList.add(0, item);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_fragment_hotel_city.setLayoutManager(linearLayoutManager);
        recycler_fragment_hotel_city.setAdapter(new CommonAdapterMultiple<PinyinItem>(getActivity(), dataList, new MultipleTypeSupport<PinyinItem>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == 0)
                    return R.layout.item_pinyin;
                else
                    return R.layout.item_hot_city;
            }

            @Override
            public int getItemViewType(int position, PinyinItem pinyinItem) {
                return pinyinItem.getType();
            }
        }) {
            @Override
            public void convert(CommonViewHolderRecycler holder, PinyinItem pinyinItem, int position) {
                if (pinyinItem.getType() == 0) {
                    holder.setText(R.id.tv_item_pinyin, pinyinItem.getName());
                } else {
                    int lineCount = 4;
                    float itemHeight = 28;
                    RecyclerView recyclerView = holder.getView(R.id.recycler_item_hot_city);
                    GridLayoutManager manager = new GridLayoutManager(getActivity(), lineCount, GridLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(manager);
                    HotCityItem hotCityItem = (HotCityItem) pinyinItem.getObject();
                    List<String> list = hotCityItem.getList();
                    ViewUtil.setHeightPx(recyclerView, (list.size() + lineCount - 1) / lineCount * RxImageTool.dp2px(itemHeight)
                            + 10 * 1);
                    recyclerView.setAdapter(new CommonAdapterRecycler<String>(getActivity(), R.layout.item_city, list, itemHeight) {
                        @Override
                        public void convert(CommonViewHolderRecycler holder, String string, int position) {
                            holder.setText(R.id.tv_item_city, string);
                        }
                    });
                    if (recyclerView.getItemDecorationCount() < 1)
                        recyclerView.addItemDecoration(new CommonDecoration(10, lineCount, Color.RED));
                }
            }
        });
        recycler_fragment_hotel_city.addItemDecoration(new PinYinDecoration(dataList));

        List<String> barDataList = new ArrayList<>();
        final Map<String, Integer> barMap = new HashMap<>();
        for (int i = 0; i < dataList.size(); i++) {
            PinyinItem bean = dataList.get(i);
            String letter = bean.getLetter();
            if (letter != null) {
                if (!barDataList.contains(letter)) {  // 不包含
                    barDataList.add(letter);
                    barMap.put(letter, i);
                }
            }
        }
        pinyin_side_bar_fragment_hotel_city.setDataList(barDataList);
        pinyin_side_bar_fragment_hotel_city.setOnTouchLetterListener(new PinYinSideBar.OnTouchLetterListener() {
            @Override
            public void onTouchLetterChanged(String s) {
                Integer integer = barMap.get(s);
                if (integer >= 0)
                    recycler_fragment_hotel_city.scrollToPosition(integer);
            }

            @Override
            public void onTouchStart(String s) {

            }

            @Override
            public void onTouchEnd(String s) {
            }
        });
    }

    // 对城市进行排序
    private List<PinyinItem> sortCity() {
        List<PinyinItem> list = new ArrayList<>();
        for (String s : citys) {
            PinyinItem item = new PinyinItem();
            list.add(item);
            item.setObject(s);
            item.setName(s);
            item.setLetter(PinYinUtil.getFirstLetter(s));
            item.setType(0);
        }
        Collections.sort(list, new Comparator<PinyinItem>() {
            @Override
            public int compare(PinyinItem o1, PinyinItem o2) {
                if (o1.getLetter().equals("#")) {
                    return 1;
                } else if (o2.getLetter().equals("#")) {
                    return -1;
                } else {
                    return o1.getLetter().compareTo(o2.getLetter());
                }
            }
        });
        return list;
    }

}
