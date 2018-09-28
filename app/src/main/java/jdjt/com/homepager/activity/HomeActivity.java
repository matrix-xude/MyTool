package jdjt.com.homepager.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.taobao.library.VerticalBannerView;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.RxImageTool;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jdjt.com.homepager.R;
import jdjt.com.homepager.adapter.HomeModuleAdapter;
import jdjt.com.homepager.adapter.VerticalBannerAdapter;
import jdjt.com.homepager.decoration.CommonDecoration;
import jdjt.com.homepager.decoration.HomeModuleDecoration;
import jdjt.com.homepager.domain.HomeFirstModuleBean;
import jdjt.com.homepager.domain.HomeFirstModuleItemBean;
import jdjt.com.homepager.domain.SimpleString;
import jdjt.com.homepager.domain.back.BackHeadImage;
import jdjt.com.homepager.domain.back.BackHotRecommend;
import jdjt.com.homepager.framgnet.HotRecommendFragment;
import jdjt.com.homepager.http.requestHelper.RequestHelperHomePager;
import jdjt.com.homepager.util.MakeDataUtil;
import jdjt.com.homepager.util.StatusBarUtil;
import jdjt.com.homepager.util.ToastUtil;
import jdjt.com.homepager.util.LayoutParamsUtil;
import jdjt.com.homepager.view.commonRecyclerView.AdapterRecycler;
import jdjt.com.homepager.view.commonRecyclerView.ViewHolderRecycler;

/**
 * Created by xxd on 2018/9/5.
 * 改版的首页
 */

public class HomeActivity extends BaseActivity {

    private LinearLayout llContent;

    private RelativeLayout rl_home_head_all; // 头部，包含状态栏部分
    private LinearLayout ll_home_head; // 头部，不包含状态栏部分
    private Banner banner_home_head;

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
        ll_home_head = findViewById(R.id.ll_home_head);
        // 留出导航栏高度
        LayoutParamsUtil.setMargins(ll_home_head, 0, StatusBarUtil.getStatusBarHeight(this), 0, 0);

        banner_home_head = findViewById(R.id.banner_home_head);
//        llContent = findViewById(R.id.ll_home_content);
    }

    private void initData() {
        requestHeadBanner();
//        initNestScrollView();
    }

    // 初始化
    private void initNestScrollView() {
        addFirstModule();
        addVerticalBanner();
        addHotRecommend();
        addHotActivity();
        refreshHolidaySetMeal();
        refreshHolidayHotel();
    }

    private View mViewHolidayHotel; // 度假酒店view
    private AdapterRecycler mAdapterHolidayHotel; // 度假酒店adapter
    private List<SimpleString> mDataHolidayHotel;// 度假酒店数据

    /**
     * 刷新度假酒店模块
     */
    private void refreshHolidayHotel() {
        int itemHeight = RxImageTool.dp2px(132);
        int maxShowNumber = 6;
        int divideSpace = RxImageTool.dp2px(1);
        mDataHolidayHotel = MakeDataUtil.makeSimpleString(8, "度假酒店");

        if (mViewHolidayHotel == null) {
            mViewHolidayHotel = View.inflate(this, R.layout.home_holiday_hotel, null);
            llContent.addView(mViewHolidayHotel);

            // 查看更多
            TextView tvMore = mViewHolidayHotel.findViewById(R.id.tv_home_holiday_hotel_more);
            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            // 热门酒店
            RecyclerView recyclerView = mViewHolidayHotel.findViewById(R.id.recycler_home_view_holiday_hotel);
            mAdapterHolidayHotel = new AdapterRecycler<SimpleString>(R.layout.item_hotel, mDataHolidayHotel,
                    new AdapterRecycler.Builder().setItemHeight(itemHeight).setMaxShowCount(maxShowNumber)) {
                @Override
                public void convert(ViewHolderRecycler holder, SimpleString simpleString, int position) {
                    holder.setText(R.id.tv_item_hotel_name, simpleString.getName());
                }
            };
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mAdapterHolidayHotel);
            recyclerView.addItemDecoration(new CommonDecoration(divideSpace, 1, Color.parseColor("#3E3F41")));
            LayoutParamsUtil.setHeightPx(recyclerView, itemHeight, divideSpace, mAdapterHolidayHotel.getItemCount());
        } else {
            mAdapterHolidayHotel.notifyDataSetChanged();
            RecyclerView recyclerView = mViewHolidayHotel.findViewById(R.id.recycler_home_view_holiday_hotel);
            LayoutParamsUtil.setHeightPx(recyclerView, itemHeight, divideSpace, mAdapterHolidayHotel.getItemCount());
        }
    }

    private View mViewHolidaySetMeal; // 度假套餐view
    private AdapterRecycler mAdapterHolidaySetMeal; // 度假套餐adapter
    private List<SimpleString> mDataHolidaySetMeal;// 度假套餐数据

    /**
     * 刷新度假套餐模块
     */
    private void refreshHolidaySetMeal() {
        int itemHeight = RxImageTool.dp2px(170);
        int maxShowNumber = 3;
        int divideSpace = RxImageTool.dp2px(10);
        mDataHolidaySetMeal = MakeDataUtil.makeSimpleString(5, "度假套餐");

        if (mViewHolidaySetMeal == null) {
            mViewHolidaySetMeal = View.inflate(this, R.layout.home_holiday_set_meal, null);
            llContent.addView(mViewHolidaySetMeal);

            // 查看更多
            TextView tvMore = mViewHolidaySetMeal.findViewById(R.id.tv_home_holiday_set_meal_more);
            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            // 热门度假列表
            RecyclerView recyclerView = mViewHolidaySetMeal.findViewById(R.id.recycler_view_home_holiday_set_meal);
            mAdapterHolidaySetMeal = new AdapterRecycler<SimpleString>(R.layout.item_home_holiday_set_meal, mDataHolidaySetMeal,
                    new AdapterRecycler.Builder().setItemHeight(itemHeight).setMaxShowCount(maxShowNumber)) {
                @Override
                public void convert(ViewHolderRecycler holder, SimpleString simpleString, int position) {
                    holder.setText(R.id.tv_item_home_holiday_set_meal_name, simpleString.getName());
                }
            };
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mAdapterHolidaySetMeal);
            recyclerView.addItemDecoration(new CommonDecoration(divideSpace));
            LayoutParamsUtil.setHeightPx(recyclerView, itemHeight, divideSpace, mAdapterHolidaySetMeal.getItemCount());
        } else {
            mAdapterHolidaySetMeal.notifyDataSetChanged();
            RecyclerView recyclerView = mViewHolidaySetMeal.findViewById(R.id.recycler_view_home_holiday_set_meal);
            LayoutParamsUtil.setHeightPx(recyclerView, itemHeight, divideSpace, mAdapterHolidaySetMeal.getItemCount());
        }
    }

    /**
     * 添加热门活动模块
     */
    private void addHotActivity() {
        View view = View.inflate(this, R.layout.home_hot_activity, null);
        Banner banner = view.findViewById(R.id.banner_home_hot_activity);
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.bg5);
        list.add(R.drawable.bg1);
        list.add(R.drawable.bg3);
        list.add(R.drawable.bg2);
        list.add(R.drawable.bg4);
        ActivityImageLoader imageLoader = new ActivityImageLoader();

        banner.setImages(list)
                .setImageLoader(imageLoader)
                .start();

        llContent.addView(view);
    }

    private class ActivityImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Integer id = (Integer) path;
            Glide.with(getApplicationContext()).load(id).into(imageView);
        }
    }

    /**
     * 添加热门推荐模块
     */
    private void addHotRecommend() {

        Flowable.fromArray(1)
                .map(new Function<Integer, List<BackHotRecommend>>() {
                    @Override
                    public List<BackHotRecommend> apply(Integer integer) throws Exception {
                        RequestHelperHomePager.getInstance().requestHeadImage("4");
                        return RequestHelperHomePager.getInstance().requestHotRecommend();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
//                        showProDialo();
                    }
                })
                .subscribe(new Consumer<List<BackHotRecommend>>() {
                    @Override
                    public void accept(List<BackHotRecommend> backHotRecommendList) throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        dismissProDialog();
                        ToastUtil.showToast(getApplicationContext(), throwable.getMessage());
                    }
                });


        int heightFirstRow = 73;  // 第一行item高度
        int heightOtherRow = 38;  // 其他行item高度
        int divideSpace = 8; // 间隔

        View view = View.inflate(this, R.layout.home_hot_recommend, null);
        SlidingTabLayout slidingTabLayout = view.findViewById(R.id.sliding_tab_home_hot_recommend);
        slidingTabLayout.setTabWidth(RxImageTool.px2dp((RxDeviceTool.getScreenWidths(this))
                - slidingTabLayout.getPaddingStart() - slidingTabLayout.getPaddingEnd()) / 3);
        ViewPager viewPager = view.findViewById(R.id.vp_home_hot_recommend);
        // 设置高度
        LayoutParamsUtil.setHeight(viewPager, heightFirstRow + heightOtherRow * 2 + divideSpace * 2);
        TextView tvMore = view.findViewById(R.id.tv_home_hot_recommend_more);
        String[] titles = new String[]{"热门目的地", "热门度假区", "大家都爱去"};
        ArrayList<Fragment> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            HotRecommendFragment hotRecommendFragment = new HotRecommendFragment();
            hotRecommendFragment.setData(heightFirstRow, heightOtherRow, divideSpace, MakeDataUtil.makeSimpleString(9));
            list.add(hotRecommendFragment);
        }
        slidingTabLayout.setViewPager(viewPager, titles, this, list);
        llContent.addView(view);
    }

    /**
     * 添加滚动头条模块
     */
    private void addVerticalBanner() {
        View view = View.inflate(this, R.layout.home_vertical_banner, null);
        VerticalBannerView vertical_banner = view.findViewById(R.id.vertical_banner);

        vertical_banner.setAdapter(new VerticalBannerAdapter(MakeDataUtil.makeHeadlineData()));
        vertical_banner.start();
        llContent.addView(view);
    }

    /**
     * 添加首页4个模块，没有数据不添加
     */
    private void addFirstModule() {
        List<HomeFirstModuleBean> holidayData = MakeDataUtil.getHolidayData();
        for (HomeFirstModuleBean homeFirstModuleBean : holidayData) {
            View holidayModule = createHolidayModule(homeFirstModuleBean);
            llContent.addView(holidayModule);
        }
    }

    // 创建模块
    private View createHolidayModule(final HomeFirstModuleBean module) {

        int itemHeight = 40;
        int divideSpace = 1;
        int spanCount = 2; // 一行有几列
        List<HomeFirstModuleItemBean> dataList = module.getList();

        View moduleView = View.inflate(this, R.layout.home_module_first, null);

        // 设置高度
        RelativeLayout rlBg = moduleView.findViewById(R.id.rl_home_module_first);
        int row = (dataList.size() - 1) / spanCount + 1; // 总共有几行
        LayoutParamsUtil.setHeight(rlBg, itemHeight * row + divideSpace * (row - 1));

        // 设置背景
        final int type = module.getType();
        if (type == 1) {
            rlBg.setBackgroundResource(R.drawable.bg_home_module_hotel_default);
        } else if (type == 2) {
            rlBg.setBackgroundColor(Color.parseColor("#4A9BEB"));
        } else if (type == 3) {
            rlBg.setBackgroundColor(Color.parseColor("#CD7C55"));
        } else if (type == 4) {
            rlBg.setBackgroundColor(Color.parseColor("#CD9955"));
        }

        // 设置左边文字
        final TextView tvNameAll = moduleView.findViewById(R.id.tv_home_module_first_name);
        tvNameAll.setText(module.getName());

        // 设置点击事件
        RelativeLayout rlAll = moduleView.findViewById(R.id.rl_home_module_first_all);
        rlAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 1) {
                    Intent intent = new Intent(HomeActivity.this, HotelSecondActivity.class);
                    startActivity(intent);
                }
            }
        });

        // 设置item
        RecyclerView recyclerView = moduleView.findViewById(R.id.recycler_view_home_module_first);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), spanCount, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new HomeModuleAdapter(dataList, itemHeight));
        recyclerView.addItemDecoration(new HomeModuleDecoration(row, spanCount, divideSpace, Color.parseColor("#2A2A2A")));

        return moduleView;
    }


    // 刷新头图
    private void refreshHeadBanner(final List<BackHeadImage> list) {
//        List<Integer> list = new ArrayList<>();
//        list.add(R.drawable.bg1);
//        list.add(R.drawable.bg2);
//        list.add(R.drawable.bg3);
//        list.add(R.drawable.bg4);
//        list.add(R.drawable.bg5);
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
            Glide.with(getApplicationContext()).load(backHeadImage.getImageUrl()).into(imageView);
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
}
