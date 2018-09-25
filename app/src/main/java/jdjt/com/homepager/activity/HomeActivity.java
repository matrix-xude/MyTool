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

import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.taobao.library.VerticalBannerView;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.RxImageTool;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import jdjt.com.homepager.R;
import jdjt.com.homepager.adapter.HomeHolidayHotelAdapter;
import jdjt.com.homepager.adapter.HomeHolidaySetMealAdapter;
import jdjt.com.homepager.adapter.HomeModuleAdapter;
import jdjt.com.homepager.adapter.VerticalBannerAdapter;
import jdjt.com.homepager.decoration.HomeModuleDecoration;
import jdjt.com.homepager.decoration.SimpleItemDecoration;
import jdjt.com.homepager.domain.HomeFirstModuleBean;
import jdjt.com.homepager.domain.HomeFirstModuleItemBean;
import jdjt.com.homepager.domain.SimpleString;
import jdjt.com.homepager.framgnet.HotRecommendFragment;
import jdjt.com.homepager.util.MakeDataUtil;
import jdjt.com.homepager.util.ViewUtil;

/**
 * Created by xxd on 2018/9/5.
 */

public class HomeActivity extends BaseActivity {

    private Banner banner;
    private LinearLayout llContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initData();
    }

    private void initView() {
        banner = findViewById(R.id.banner);
        llContent = findViewById(R.id.ll_home_content);
    }

    private void initData() {
        initBanner();
        initNestScrollView();
    }

    // 初始化
    private void initNestScrollView() {
        addFirstModule();
        addVerticalBanner();
        addHotRecommend();
        addHotActivity();
        addHolidaySetMeal();
        addHolidayHotel();
    }

    /**
     * 添加度假酒店模块
     */
    private void addHolidayHotel() {
        int itemHeight = 132;
        int maxShowNumber = 6;
        int divideSpace = 1;
        List<SimpleString> dataList = MakeDataUtil.makeSimpleString(8, "度假酒店");
        int size = dataList.size() < maxShowNumber ? dataList.size() : maxShowNumber;

        View view = View.inflate(this, R.layout.home_holiday_hotel, null);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_home_view_holiday_hotel);
        ViewUtil.setHeight(recyclerView, size * itemHeight + divideSpace * (size - 1));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new HomeHolidayHotelAdapter(dataList, itemHeight, maxShowNumber));
        recyclerView.addItemDecoration(new SimpleItemDecoration(divideSpace, Color.parseColor("#3E3F41")));

        llContent.addView(view);
    }

    /**
     * 添加度假套餐模块
     */
    private void addHolidaySetMeal() {
        int itemHeight = 170;
        int maxShowNumber = 3;
        int divideSpace = 10;
        List<SimpleString> dataList = MakeDataUtil.makeSimpleString(5, "度假套餐");
        int size = dataList.size() < maxShowNumber ? dataList.size() : maxShowNumber;

        View view = View.inflate(this, R.layout.home_holiday_set_meal, null);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_home_holiday_set_meal);
        ViewUtil.setHeight(recyclerView, itemHeight * size + divideSpace * (size - 1));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new HomeHolidaySetMealAdapter(dataList, itemHeight, maxShowNumber));
        recyclerView.addItemDecoration(new SimpleItemDecoration(divideSpace, Color.TRANSPARENT));

        llContent.addView(view);
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

        int heightFirstRow = 73;  // 第一行item高度
        int heightOtherRow = 38;  // 其他行item高度
        int divideSpace = 8; // 间隔

        View view = View.inflate(this, R.layout.home_hot_recommend, null);
        SlidingTabLayout slidingTabLayout = view.findViewById(R.id.sliding_tab_home_hot_recommend);
        slidingTabLayout.setTabWidth(RxImageTool.px2dp((RxDeviceTool.getScreenWidths(this))
                - slidingTabLayout.getPaddingStart() - slidingTabLayout.getPaddingEnd()) / 3);
        ViewPager viewPager = view.findViewById(R.id.vp_home_hot_recommend);
        // 设置高度
        ViewUtil.setHeight(viewPager, heightFirstRow + heightOtherRow * 2 + divideSpace * 2);
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
        ViewUtil.setHeight(rlBg, itemHeight * row + divideSpace * (row - 1));

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


    // 初始化自动滑动的头图
    private void initBanner() {
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.bg1);
        list.add(R.drawable.bg2);
        list.add(R.drawable.bg3);
        list.add(R.drawable.bg4);
        list.add(R.drawable.bg5);
        MyImageLoader imageLoader = new MyImageLoader();

        banner.setImages(list)
                .setImageLoader(imageLoader)
                .start();
    }

    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Integer id = (Integer) path;
            Glide.with(getApplicationContext()).load(id).into(imageView);
        }
    }
}
