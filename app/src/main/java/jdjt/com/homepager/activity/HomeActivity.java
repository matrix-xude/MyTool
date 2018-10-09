package jdjt.com.homepager.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.taobao.library.BaseBannerAdapter;
import com.taobao.library.VerticalBannerView;
import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.RxImageTool;
import com.vondear.rxtool.view.RxToast;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jdjt.com.homepager.R;
import jdjt.com.homepager.decoration.CommonDecoration;
import jdjt.com.homepager.domain.back.BackHeadImage;
import jdjt.com.homepager.domain.back.BackHotActivity;
import jdjt.com.homepager.domain.back.BackHotRecommend;
import jdjt.com.homepager.domain.back.BackHotRecommendLevel;
import jdjt.com.homepager.domain.back.BackMVMNew;
import jdjt.com.homepager.domain.back.BackNavigation;
import jdjt.com.homepager.domain.back.BackNavigationLevel;
import jdjt.com.homepager.domain.back.BackVacation;
import jdjt.com.homepager.domain.back.BackVacationLevel;
import jdjt.com.homepager.framgnet.HotRecommendFragment;
import jdjt.com.homepager.http.requestHelper.RequestHelperHomePager;
import jdjt.com.homepager.util.GlideLoadUtil;
import jdjt.com.homepager.util.LayoutParamsUtil;
import jdjt.com.homepager.util.StatusBarUtil;
import jdjt.com.homepager.util.ToastUtil;
import jdjt.com.homepager.view.commonRecyclerView.AdapterRecycler;
import jdjt.com.homepager.view.commonRecyclerView.ViewHolderRecycler;

/**
 * Created by xxd on 2018/9/5.
 * 改版的首页
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_home_head_all; // 头部，包含状态栏部分
    private LinearLayout ll_home_head; // 头部，不包含状态栏部分
    private ImageView iv_home_head_scan; // 扫一扫
    private TextView tv_home_head_search; // search
    private ImageView iv_home_head_v_member; // V客会

    private NestedScrollView nest_scroll_home;

    private Banner banner_home_head;  // 顶部轮播图

    private LinearLayout ll_home_navigation; // 导航状态，动态添加

    private VerticalBannerView vertical_banner_home; // 头条
    private ImageView iv_home_house_car; // 房车营地

    private LinearLayout ll_home_hot_recommend; // 热门推荐整个模块
    private SlidingTabLayout sliding_tab_home_hot_recommend; // tab切换
    private ViewPager vp_home_hot_recommend;
    private TextView tv_home_hot_recommend_more; // 热门推荐查看更多

    private LinearLayout ll_home_hot_activity; // 热门活动轮播图 整个模块
    private Banner banner_home_hot_activity; // 热门活动轮播图

    private LinearLayout ll_home_vacation; // 底部度假


    private final int mHeadDp = 40; // 头的高度，不包含导航栏
    private final int mHeadBannerDp = 180; // 头图banner高度


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.fitSystemWindow(this);
        setContentView(R.layout.activity_home);
        initView();
        initData();
    }

    private void initView() {
        rl_home_head_all = findViewById(R.id.rl_home_head_all);
        LayoutParamsUtil.setHeightPx(rl_home_head_all, RxImageTool.dp2px(mHeadDp) + StatusBarUtil.getStatusBarHeight(this));
        ll_home_head = findViewById(R.id.ll_home_head);
        // 留出导航栏高度
        LayoutParamsUtil.setMargins(ll_home_head, 0, StatusBarUtil.getStatusBarHeight(this), 0, 0);
        iv_home_head_scan = findViewById(R.id.iv_home_head_scan);
        tv_home_head_search = findViewById(R.id.tv_home_head_search);
        iv_home_head_v_member = findViewById(R.id.iv_home_head_v_member);

        nest_scroll_home = findViewById(R.id.nest_scroll_home);
        banner_home_head = findViewById(R.id.banner_home_head);
        LayoutParamsUtil.setHeight(banner_home_head, mHeadBannerDp);
        ll_home_navigation = findViewById(R.id.ll_home_navigation);
        vertical_banner_home = findViewById(R.id.vertical_banner_home);
        iv_home_house_car = findViewById(R.id.iv_home_house_car);
        ll_home_hot_recommend = findViewById(R.id.ll_home_hot_recommend);
        sliding_tab_home_hot_recommend = findViewById(R.id.sliding_tab_home_hot_recommend);
        vp_home_hot_recommend = findViewById(R.id.vp_home_hot_recommend);
        tv_home_hot_recommend_more = findViewById(R.id.tv_home_hot_recommend_more);
        ll_home_hot_activity = findViewById(R.id.ll_home_hot_activity);
        banner_home_hot_activity = findViewById(R.id.banner_home_hot_activity);
        ll_home_vacation = findViewById(R.id.ll_home_vacation);
    }

    private void initData() {
        rl_home_head_all.setOnClickListener(this); // 消耗点击事件
        iv_home_house_car.setOnClickListener(this);
        iv_home_head_scan.setOnClickListener(this);
        tv_home_head_search.setOnClickListener(this);
        iv_home_head_v_member.setOnClickListener(this);

        // 变化的值
        final int distance = RxImageTool.dp2px(mHeadBannerDp - mHeadDp) - StatusBarUtil.getStatusBarHeight(this);
        nest_scroll_home.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                float gap = (float) scrollY / distance;
                if (gap > 1)
                    gap = 1;
                if (gap < 0)
                    gap = 0;
                rl_home_head_all.setAlpha(gap);
            }
        });
        requestHeadBanner();
        requestNavigation();
        requestMVMNew();
        requestHotRecommend();
        requestHotActivity();
        requestVacation();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_home_head_all:
                break;
            case R.id.iv_home_house_car:
                RxToast.showToast("房车营地");
                break;
            case R.id.iv_home_head_scan:
                RxToast.showToast("扫一扫");
                break;
            case R.id.tv_home_head_search:
                RxToast.showToast("search");
                break;
            case R.id.iv_home_head_v_member:
//                RxToast.showToast("V客会");
                Intent intent = new Intent(HomeActivity.this, HotelSecondActivity.class);
                startActivity(intent);
                break;
        }
    }

    // 刷新度假模块
    private void refreshVacation(List<BackVacation> list) {
        if (RxDataTool.isEmpty(list)) {
            ll_home_vacation.removeAllViews();
            ll_home_vacation.setVisibility(View.GONE);
        } else {
            ll_home_vacation.removeAllViews();
            ll_home_vacation.setVisibility(View.VISIBLE);
            for (BackVacation vacation : list) {
                ll_home_vacation.addView(createOneVacation(vacation));
            }
        }
    }

    // 创建一个度假模块
    private View createOneVacation(BackVacation vacation) {
        final View view = View.inflate(this, R.layout.home_vacation, null);

        final int type = RxDataTool.stringToInt(vacation.getType()); // 0-度假套餐， 1-度假酒店

        // title文字
        TextView tvTitle = view.findViewById(R.id.tv_home_vacation_title);
        tvTitle.setText(vacation.getTitle() + "");

        // 查看更多的位置、点击事件
        TextView tvMore = null;
        if (type == 0) { // 度假套餐
            tvMore = view.findViewById(R.id.tv_home_vacation_more_2);
            tvMore.setVisibility(View.VISIBLE);
        } else if (type == 1) { // 度假酒店
            tvMore = view.findViewById(R.id.tv_home_vacation_more_1);
            tvMore.setVisibility(View.VISIBLE);
        }
        if (tvMore != null) {
            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 0) { // 度假套餐
                        RxToast.showToast("度假套餐-更多");
                    } else if (type == 1) { // 度假酒店
                        RxToast.showToast("度假酒店-更多");
                    }
                }
            });
        }

        // recycler
        RecyclerView recyclerView = view.findViewById(R.id.recycler_home_vacation);
        int itemHeight = 0;  // item高度
        int divide = 0; // 间隔
        int lineCount = 1;
        int maxShowCount = 0; // 最大显示的数量
        int layoutId = 0;
        int divideColor = Color.TRANSPARENT;
        if (type == 0) { // 度假套餐
            itemHeight = RxImageTool.dp2px(170);
            divide = RxImageTool.dp2px(10);
            maxShowCount = 3;
            layoutId = R.layout.item_home_vacation_set_meal;
        } else if (type == 1) { // 度假酒店
            divide = RxImageTool.dp2px(1);
            maxShowCount = 6;
            layoutId = R.layout.item_hotel;
            divideColor = Color.parseColor("#3E3F41");
        }
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new AdapterRecycler<BackVacationLevel>(layoutId, vacation.getDataList(),
                new AdapterRecycler.Builder().setItemHeight(itemHeight).setMaxShowCount(maxShowCount)) {
            @Override
            public void convert(ViewHolderRecycler holder, BackVacationLevel level, int position) {
                if (type == 0) { // 度假套餐
                    holder.setText(R.id.tv_item_home_vacation_set_meal_name, level.getName() + "");
                    holder.setText(R.id.tv_item_home_vacation_set_meal_price, level.getPrice() + "");
                    holder.setText(R.id.tv_item_home_vacation_set_meal_save, level.getDiscountMoney() + "");
                    ImageView ivBg = holder.getView(R.id.iv_item_home_vacation_set_meal_bg);
                    GlideLoadUtil.loadImage(getApplicationContext(), level.getImageUrl(), ivBg);
                } else if (type == 1) { // 度假酒店
                    ImageView ivIcon = holder.getView(R.id.iv_item_hotel_icon);
                    GlideLoadUtil.loadImage(getApplicationContext(), level.getImageUrl(), ivIcon);
                    holder.setText(R.id.tv_item_hotel_name, level.getName() + "");
                    // TODO 还没返回address
                    holder.setText(R.id.tv_item_hotel_address, level.getName() + "");
                    holder.setText(R.id.tv_item_hotel_grade, level.getGrade() + "分");
                    holder.setText(R.id.tv_item_hotel_people, RxDataTool.stringToInt(level.getHasChanged()) + "人已出游");
                    holder.setText(R.id.tv_item_hotel_price, "¥" + level.getPrice() + "起");
                }
            }
        });
        recyclerView.addItemDecoration(new CommonDecoration(divide, lineCount, divideColor));

        return view;
    }

    // 刷新热门活动
    private void refreshHotActivity(final List<BackHotActivity> list) {
        if (RxDataTool.isEmpty(list)) {
            ll_home_hot_activity.setVisibility(View.GONE);
            banner_home_hot_activity.stopAutoPlay(); // 停止轮播
            return;
        } else {
            ll_home_hot_activity.setVisibility(View.VISIBLE);
        }

        ActivityImageLoader imageLoader = new ActivityImageLoader();
        banner_home_hot_activity.setImages(list)
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        BackHotActivity backHotActivity = list.get(position);
                        RxToast.showToast(backHotActivity.getTitle());
                    }
                })
                .setImageLoader(imageLoader)
                .start();

    }

    private class ActivityImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            BackHotActivity backHotActivity = (BackHotActivity) path;
            GlideLoadUtil.loadImage(getApplicationContext(), backHotActivity.getImageUrl(), imageView);
        }
    }

    // 刷新热门推荐模块
    private void refreshHotRecommend(List<BackHotRecommend> list) {
        if (RxDataTool.isEmpty(list)) {
            ll_home_hot_recommend.setVisibility(View.GONE);
            return;
        } else {
            ll_home_hot_recommend.setVisibility(View.VISIBLE);
        }

        int heightFirstRow = RxImageTool.dp2px(73);  // 第一行item高度
        int heightOtherRow = RxImageTool.dp2px(38);  // 其他行item高度
        int divide = RxImageTool.dp2px(8); // 间隔
        int lineCount = 3; // 列数
        int maxShowCount = lineCount * 3; // 最大显示的数量

        // 必须在初始化viewpager的高度，才能显示出切换的fragment
        int maxDataCount = 0; // 最大的那个数据条目数，确定高度
        for (int i = 0; i < list.size(); i++) {
            List<BackHotRecommendLevel> children = list.get(i).getChildren();
            if (children != null) {
                if (children.size() > maxDataCount)
                    maxDataCount = children.size();
            }
        }

        // 实际展示的最大条目数
        int count = maxDataCount > maxShowCount ? maxShowCount : maxDataCount;
        int row = (count + lineCount - 1) / lineCount;
        int firstRow = row > 0 ? 1 : 0;
        int otherRow = row - 1 > 0 ? row - 1 : 0;
        int divideCount = row - 1 > 0 ? row - 1 : 0;
        LayoutParamsUtil.setHeightPx(vp_home_hot_recommend, heightFirstRow * firstRow + heightOtherRow * otherRow + divide * divideCount);


        // 初始化切换tab宽度
        int size = list.size();
        sliding_tab_home_hot_recommend.setTabWidth(RxImageTool.px2dp((RxDeviceTool.getScreenWidths(this))
                - sliding_tab_home_hot_recommend.getPaddingStart() - sliding_tab_home_hot_recommend.getPaddingEnd()) / size);

        // 数据
        String[] titles = new String[size];
        for (int i = 0; i < size; i++) {
            titles[i] = list.get(i).getName();
        }
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            HotRecommendFragment hotRecommendFragment = new HotRecommendFragment();
            hotRecommendFragment.setData(list.get(i), heightFirstRow, heightOtherRow, divide, lineCount, maxShowCount);
            fragments.add(hotRecommendFragment);
        }
        sliding_tab_home_hot_recommend.setViewPager(vp_home_hot_recommend, titles, this, fragments);

        // 查看更多
        tv_home_hot_recommend_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HotelDestinationActivity.class);
                startActivity(intent);
            }
        });
    }

    // 刷新动态头条
    private void refreshMVMNews(final List<BackMVMNew> list) {
        // 用来索引的
        List<Integer> indexList = new ArrayList<>();
        // 每页展示2行数据
        int pagerCount = 2;
        if (list == null) {
            vertical_banner_home.stop();
        } else {
            for (int i = 0; pagerCount * i < list.size(); i++) {
                indexList.add(i);
            }
            vertical_banner_home.setAdapter(new BaseBannerAdapter<Integer>(indexList) {
                @Override
                public View getView(VerticalBannerView verticalBannerView) {
                    return View.inflate(verticalBannerView.getContext(), R.layout.item_home_vertical_banner, null);
                }

                @Override
                public void setItem(View view, Integer integer) {
                    TextView tv1 = view.findViewById(R.id.tv_item_home_vertical_banner_1);
                    TextView tv2 = view.findViewById(R.id.tv_item_home_vertical_banner_2);
                    if (integer < list.size()) {
                        final BackMVMNew backMVMNew = list.get(integer);
                        tv1.setText(backMVMNew.getTitle());
                        tv1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RxToast.normal(backMVMNew.getContent());
                            }
                        });
                    }
                    if (integer + 1 < list.size()) {
                        final BackMVMNew backMVMNew = list.get(integer + 1);
                        tv2.setText(backMVMNew.getTitle());
                        tv2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RxToast.normal(backMVMNew.getContent());
                            }
                        });
                    }
                }
            });
            vertical_banner_home.start();
        }
    }

    // 刷新导航模块，动态添加，没有数据隐藏
    private void refreshNavigation(List<BackNavigation> list) {
        if (RxDataTool.isEmpty(list)) {
            ll_home_navigation.removeAllViews();
            ll_home_navigation.setVisibility(View.GONE);
        } else {
            ll_home_navigation.removeAllViews();
            ll_home_navigation.setVisibility(View.VISIBLE);
            for (BackNavigation navigation : list) {
                ll_home_navigation.addView(createOneNavigation(navigation));
            }
        }
    }

    // 创建一个导航模块
    private View createOneNavigation(BackNavigation navigation) {

        int itemHeight = RxImageTool.dp2px(40);
        int divide = RxImageTool.dp2px(1);
        int lineCount = 2; // 一行有几列

        View view = View.inflate(this, R.layout.home_navigation, null);

        // 设置高度
        RelativeLayout rlNavigation = view.findViewById(R.id.rl_home_navigation);
        int row = 1; // 默认只有一行
        //1：度假酒店；2：度假套餐；3：猫超市；4：线路游；5:猫玩乐;6:现在生活mall
        final int typeId = RxDataTool.stringToInt(navigation.getNavigationTypeId());
        if (typeId == 1) {
            row = 3;
        } else if (typeId == 2) {
            row = 2;
        }
        LayoutParamsUtil.setHeightPx(rlNavigation, itemHeight, divide, row);

        // 设置背景
        final int backgroundType = RxDataTool.stringToInt(navigation.getBackgroundType());
        String typeContent = navigation.getTypeContent();
        if (backgroundType == 1) { // 图片
            ImageView iv_home_navigation_bg = view.findViewById(R.id.iv_home_navigation_bg);
            GlideLoadUtil.loadImage(this, typeContent, iv_home_navigation_bg);
        } else if (backgroundType == 2) { // 色值
            rlNavigation.setBackgroundColor(Color.parseColor(typeContent));
        }

        // 设置左侧大模块的文字、点击事件
        TextView tv_home_navigation_name = view.findViewById(R.id.tv_home_navigation_name);
        tv_home_navigation_name.setText(navigation.getNavigationTypeName() + "");
        tv_home_navigation_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                switch (typeId) {  //1：度假酒店；2：度假套餐；3：猫超市；4：线路游；5:猫玩乐;6:现在生活mall
                    case 1:
                        intent.setClass(HomeActivity.this, HotelSecondActivity.class);
                        break;
                    case 2:
                        break;
                }
                startActivity(intent);
            }
        });

        // recycler
        RecyclerView recyclerView = view.findViewById(R.id.recycler_home_navigation);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), lineCount, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new AdapterRecycler<BackNavigationLevel>(R.layout.item_home_navigation, navigation.getNavigationParams(),
                new AdapterRecycler.Builder().setItemHeight(itemHeight)) {
            @Override
            public void convert(ViewHolderRecycler holder, final BackNavigationLevel backNavigationLevel, int position) {
                holder.setText(R.id.tv_name, backNavigationLevel.getParamTitle() + "");
                holder.setOnClickListener(R.id.tv_name, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxToast.showToast(backNavigationLevel.getParamTitle() + "");
                    }
                });
            }
        });
        recyclerView.addItemDecoration(new CommonDecoration(divide, lineCount, Color.parseColor("#2A2A2A")));

        return view;
    }


    // 刷新头图
    private void refreshHeadBanner(final List<BackHeadImage> list) {
        if (list == null)
            return;

        MyImageLoader imageLoader = new MyImageLoader();
        banner_home_head.setImages(list)
                .setImageLoader(imageLoader)
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        ToastUtil.showToast(getApplicationContext(), list.get(position).getTitle() + "");
                    }
                })
                .start();
    }

    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            BackHeadImage backHeadImage = (BackHeadImage) path;
            GlideLoadUtil.loadImage(getApplicationContext(), backHeadImage.getImageUrl(), imageView);
        }
    }

    private void requestHeadBanner() {
        Flowable.fromArray(1)
                .map(new Function<Integer, List<BackHeadImage>>() {
                    @Override
                    public List<BackHeadImage> apply(Integer integer) throws Exception {
                        return RequestHelperHomePager.getInstance().requestHeadImage("4");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                    }
                })
                .subscribe(new Consumer<List<BackHeadImage>>() {
                    @Override
                    public void accept(List<BackHeadImage> backHeadImageList) throws Exception {
                        refreshHeadBanner(backHeadImageList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showToast(getApplicationContext(), throwable.getMessage());
                    }
                });
    }

    private void requestNavigation() {
        Flowable.fromArray(1)
                .map(new Function<Integer, List<BackNavigation>>() {
                    @Override
                    public List<BackNavigation> apply(Integer integer) throws Exception {
                        List<BackNavigation> list = RequestHelperHomePager.getInstance().requestNavigation("0");
                        if (list != null) {  // 异步线程排序
                            Collections.sort(list, new Comparator<BackNavigation>() {
                                @Override
                                public int compare(BackNavigation o1, BackNavigation o2) { // 排序
                                    return RxDataTool.stringToInt(o1.getSeq()) - RxDataTool.stringToInt(o2.getSeq());
                                }
                            });
                            for (int i = 0; i < list.size(); i++) {
                                BackNavigation navigation = list.get(i);
                                List<BackNavigationLevel> navigationParams = navigation.getNavigationParams();
                                if (navigationParams != null) {
                                    Collections.sort(navigationParams, new Comparator<BackNavigationLevel>() {
                                        @Override
                                        public int compare(BackNavigationLevel o1, BackNavigationLevel o2) { // 排序
                                            return RxDataTool.stringToInt(o1.getSeq()) - RxDataTool.stringToInt(o2.getSeq());
                                        }
                                    });
                                }
                            }
                        }
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
                .subscribe(new Consumer<List<BackNavigation>>() {
                    @Override
                    public void accept(List<BackNavigation> backNavigationList) throws Exception {
                        refreshNavigation(backNavigationList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showToast(getApplicationContext(), throwable.getMessage());
                    }
                });
    }

    private void requestMVMNew() {
        Flowable.fromArray(1)
                .map(new Function<Integer, List<BackMVMNew>>() {
                    @Override
                    public List<BackMVMNew> apply(Integer integer) throws Exception {
                        return RequestHelperHomePager.getInstance().requestMVMNew("12");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                    }
                })
                .subscribe(new Consumer<List<BackMVMNew>>() {
                    @Override
                    public void accept(List<BackMVMNew> backMVMNews) throws Exception {
                        refreshMVMNews(backMVMNews);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showToast(getApplicationContext(), throwable.getMessage());
                    }
                });
    }

    private void requestHotRecommend() {
        Flowable.fromArray(1)
                .map(new Function<Integer, List<BackHotRecommend>>() {
                    @Override
                    public List<BackHotRecommend> apply(Integer integer) throws Exception {
                        return RequestHelperHomePager.getInstance().requestHotRecommend();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                    }
                })
                .subscribe(new Consumer<List<BackHotRecommend>>() {
                    @Override
                    public void accept(List<BackHotRecommend> backHotRecommendList) throws Exception {
                        refreshHotRecommend(backHotRecommendList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showToast(getApplicationContext(), throwable.getMessage());
                    }
                });
    }

    private void requestHotActivity() {
        Flowable.fromArray(1)
                .map(new Function<Integer, List<BackHotActivity>>() {
                    @Override
                    public List<BackHotActivity> apply(Integer integer) throws Exception {
                        return RequestHelperHomePager.getInstance().requestHotActivity("4");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                    }
                })
                .subscribe(new Consumer<List<BackHotActivity>>() {
                    @Override
                    public void accept(List<BackHotActivity> backHotActivities) throws Exception {
                        refreshHotActivity(backHotActivities);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showToast(getApplicationContext(), throwable.getMessage());
                    }
                });
    }

    private void requestVacation() {
        Flowable.fromArray(1)
                .map(new Function<Integer, List<BackVacation>>() {
                    @Override
                    public List<BackVacation> apply(Integer integer) throws Exception {
                        return RequestHelperHomePager.getInstance().requestVacation();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                    }
                })
                .subscribe(new Consumer<List<BackVacation>>() {
                    @Override
                    public void accept(List<BackVacation> backVacationList) throws Exception {
                        refreshVacation(backVacationList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showToast(getApplicationContext(), throwable.getMessage());
                    }
                });
    }


}
