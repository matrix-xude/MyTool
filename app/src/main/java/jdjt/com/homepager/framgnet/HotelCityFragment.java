package jdjt.com.homepager.framgnet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxImageTool;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jdjt.com.homepager.R;
import jdjt.com.homepager.decoration.CommonDecoration;
import jdjt.com.homepager.decoration.PinYinDecoration;
import jdjt.com.homepager.domain.HotelDestination;
import jdjt.com.homepager.domain.HotCityItem;
import jdjt.com.homepager.domain.PinyinItem;
import jdjt.com.homepager.domain.back.BackChinaCity;
import jdjt.com.homepager.domain.back.BackHotRecommend;
import jdjt.com.homepager.domain.back.BackHotRecommendLevel;
import jdjt.com.homepager.http.requestHelper.RequestHelperHomePager;
import jdjt.com.homepager.util.PinYinUtil;
import jdjt.com.homepager.util.LayoutParamsUtil;
import jdjt.com.homepager.util.ToastUtil;
import jdjt.com.homepager.view.PinYinSideBar;
import jdjt.com.homepager.view.commonRecyclerView.AdapterMultipleRecycler;
import jdjt.com.homepager.view.commonRecyclerView.AdapterRecycler;
import jdjt.com.homepager.view.commonRecyclerView.ViewHolderRecycler;
import jdjt.com.homepager.view.commonRecyclerView.MultipleTypeSupport;

/**
 * Created by xxd on 2018/9/20.
 * 城市选择
 */

public class HotelCityFragment extends BaseFragment {

    private List<PinyinItem> dataList;

    private RecyclerView recycler_fragment_hotel_city;
    private PinYinSideBar pinyin_side_bar_fragment_hotel_city;

    private String type; // 1：热门目的地 2：热门度假区 3：大家都爱去

    private String mTitle; // 后台判断用，当前类型
    private String mType; // 后台判断用，当前类型id

    @Override
    public int getLayoutId() {
        return R.layout.fragment_hotel_city;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initView();
        initData();
    }


    private void initView() {
        recycler_fragment_hotel_city = view.findViewById(R.id.recycler_fragment_hotel_city);
        pinyin_side_bar_fragment_hotel_city = view.findViewById(R.id.pinyin_side_bar_fragment_hotel_city);
    }

    private void initData() {
        type = getArguments().getString("type");
        if (dataList == null)  // 没有数据才请求，已经有了就不请求
            requestData();
    }

    // 带着数据返回前一个页面
    private void backWithData(HotelDestination data) {
        Intent intent = new Intent();
        intent.putExtra("destination", data);
        getActivity().setResult(1, intent);
        getActivity().finish();
    }

    private void refreshListAndBar() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_fragment_hotel_city.setLayoutManager(linearLayoutManager);
        recycler_fragment_hotel_city.setAdapter(new AdapterMultipleRecycler<PinyinItem>(dataList, new MultipleTypeSupport<PinyinItem>() {
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
            public void convert(final ViewHolderRecycler holder, final PinyinItem pinyinItem, int position) {
                if (pinyinItem.getType() == 0) {
                    holder.setText(R.id.tv_item_pinyin, pinyinItem.getName());
                    holder.setOnClickListener(R.id.tv_item_pinyin, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HotelDestination hotelDestination = new HotelDestination();
                            hotelDestination.setTitle(mTitle);
                            hotelDestination.setType(mType);
                            BackChinaCity backChinaCity = (BackChinaCity) pinyinItem.getObject();
                            hotelDestination.setId(backChinaCity.getId());
                            hotelDestination.setName(backChinaCity.getRegionName());
                            backWithData(hotelDestination);
                        }
                    });
                } else {
                    // 标题
                    final HotCityItem hotCityItem = (HotCityItem) pinyinItem.getObject();
                    holder.setText(R.id.tv_hot_city_title, hotCityItem.getTitle());

                    // 热门集合
                    int lineCount = "1".equals(type) ? 4 : 3;
                    int maxShowCount = 12;
                    int itemHeight = RxImageTool.dp2px(28);
                    int divide = RxImageTool.dp2px(5);
                    RecyclerView recyclerView = holder.getView(R.id.recycler_item_hot_city);
                    GridLayoutManager manager = new GridLayoutManager(getActivity(), lineCount, GridLayoutManager.VERTICAL, false) {
                        @Override
                        public boolean canScrollVertically() {
                            return false;
                        }
                    };
                    recyclerView.setLayoutManager(manager);
                    List<BackHotRecommendLevel> list = hotCityItem.getList();
                    AdapterRecycler<BackHotRecommendLevel> adapter = new AdapterRecycler<BackHotRecommendLevel>(R.layout.item_city, list,
                            new Builder().setMaxShowCount(maxShowCount).setItemHeight(itemHeight)) {
                        @Override
                        public void convert(ViewHolderRecycler holder, final BackHotRecommendLevel backHotRecommendLevel, int position) {
                            holder.setText(R.id.tv_item_city, backHotRecommendLevel.getName());
                            holder.setOnClickListener(R.id.tv_item_city, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    HotelDestination hotelDestination = new HotelDestination();
                                    hotelDestination.setTitle(mTitle);
                                    hotelDestination.setType(mType);
                                    hotelDestination.setId(backHotRecommendLevel.getRefId());
                                    hotelDestination.setName(backHotRecommendLevel.getName());
                                    backWithData(hotelDestination);
                                }
                            });
                        }
                    };
                    LayoutParamsUtil.setHeightPx(recyclerView, itemHeight, divide, adapter.getItemCount(), lineCount);
                    recyclerView.setAdapter(adapter);

                    if (recyclerView.getItemDecorationCount() < 1)  // 不能重复添加
                        recyclerView.addItemDecoration(new CommonDecoration(divide, lineCount, Color.TRANSPARENT));
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
        pinyin_side_bar_fragment_hotel_city.requestLayout();
    }

    private void requestData() {
        Flowable.fromArray(1)
                .map(new Function<Integer, List<PinyinItem>>() {
                    @Override
                    public List<PinyinItem> apply(Integer integer) throws Exception {
                        BackHotRecommend backHotRecommend = RequestHelperHomePager.getInstance().requestHotRecommendAll(type);
                        mTitle = backHotRecommend.getName();
                        mType = backHotRecommend.getType();
                        List<PinyinItem> list = new ArrayList<>();

                        List<BackHotRecommendLevel> children = backHotRecommend.getChildren();
                        if (!RxDataTool.isEmpty(children)) { // 有数据，添加第一个PinYinItem
                            PinyinItem pinyinItem = new PinyinItem();
                            pinyinItem.setType(1);
                            pinyinItem.setLetter("热");
                            pinyinItem.setName(backHotRecommend.getName());
                            HotCityItem hotCityItem = new HotCityItem();
                            hotCityItem.setTitle(backHotRecommend.getName());
                            hotCityItem.setList(children);
                            pinyinItem.setObject(hotCityItem);
                            list.add(pinyinItem);
                        }
                        // 排序并且添加数据
                        list.addAll(sortCity(backHotRecommend.getChinaCityList()));

                        return list;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                    }
                })
                .subscribe(new Consumer<List<PinyinItem>>() {
                    @Override
                    public void accept(List<PinyinItem> pinyinItemList) throws Exception {
                        dataList = new ArrayList<>();
                        dataList.addAll(pinyinItemList);
                        refreshListAndBar();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showToast(getActivity(), throwable.getMessage());
                    }
                });
    }

    // 对城市进行排序
    private List<PinyinItem> sortCity(List<BackChinaCity> chinaCityList) {
        List<PinyinItem> list = new ArrayList<>();
        if (chinaCityList != null) {
            for (BackChinaCity city : chinaCityList) {
                PinyinItem item = new PinyinItem();
                list.add(item);
                item.setObject(city);
                item.setName(city.getRegionName());
                item.setLetter(PinYinUtil.getFirstLetter(city.getRegionName()));
                item.setType(0);
            }
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
