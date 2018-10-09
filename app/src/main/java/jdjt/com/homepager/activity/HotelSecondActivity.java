package jdjt.com.homepager.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.RxImageTool;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jdjt.com.homepager.R;
import jdjt.com.homepager.adapter.HotelSecondClassifyAdapter;
import jdjt.com.homepager.decoration.CommonDecoration;
import jdjt.com.homepager.domain.SimpleString;
import jdjt.com.homepager.util.LayoutParamsUtil;
import jdjt.com.homepager.util.MakeDataUtil;
import jdjt.com.homepager.util.StatusBarUtil;
import jdjt.com.homepager.view.commonPopupWindow.CommonPopupWindow;
import jdjt.com.homepager.view.commonRecyclerView.AdapterRecycler;
import jdjt.com.homepager.view.commonRecyclerView.ViewHolderRecycler;
import jdjt.com.homepager.view.verticalCalendar.CalendarData;
import jdjt.com.homepager.view.verticalCalendar.CalendarUtil;
import jdjt.com.homepager.view.verticalCalendar.HotelCalendarView;

/**
 * Created by xxd on 2018/9/10.
 * 酒店频道
 */

public class HotelSecondActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_hotel_second_parent; // 整个页面，弹框使用
    private NestedScrollView nest_scroll_hotel_second; //

    private RelativeLayout rl_hotel_second_head_all; // 包含状态头的头，渐变
    private RelativeLayout ll_hotel_second_head; // 不包含状态头的头
    private ImageView iv_hotel_second_back; // 返回
    private Banner banner_hotel_second;
    private CardView card_view_hotel_second; // 查询条件整个

    // 圆角查询条件中所有控件
    private RelativeLayout rlDestination; // 目的地
    private RelativeLayout rlLocation; // 定位按钮
    private LinearLayout llTime; // 入离时间
    private TextView tvCheckIn;
    private TextView tvCheckOut;
    private TextView tvDayCount;
    private TextView tvKeyword; // 关键字
    private TextView tvQuery; // 查询

    private RecyclerView recyclerClassify; // 分类
    private RecyclerView recyclerLike; // 猜你喜欢

    // 当前选择的起始日期
    private String mStartDate;
    private String mEndDate;
    private boolean isSelected; // 是否选择过了日历

    private CommonPopupWindow popupWindow;
    private Handler handler = new Handler() {
    };


    private final static int mHeadDp = 40; // 头的高度，不包含导航栏
    private final static int mHeadBannerDp = 180; // 头图banner高度
    private final static int mModuleTop = 5; // 搜索条件突出到头图的高度

    private final int REQUEST_CODE_KEYWORD = 1;
    private final int REQUEST_CODE_DESTINATION = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.fitSystemWindow(this);
        setContentView(R.layout.activity_hotel_second);
        initView();
        initData();
    }

    private void initView() {
        rl_hotel_second_parent = findViewById(R.id.rl_hotel_second_parent);
        nest_scroll_hotel_second = findViewById(R.id.nest_scroll_hotel_second);

        rl_hotel_second_head_all = findViewById(R.id.rl_hotel_second_head_all);
        LayoutParamsUtil.setHeightPx(rl_hotel_second_head_all, RxImageTool.dp2px(mHeadDp) + StatusBarUtil.getStatusBarHeight(this));
        ll_hotel_second_head = findViewById(R.id.ll_hotel_second_head);
        // 留出导航栏高度
        LayoutParamsUtil.setMargins(ll_hotel_second_head, 0, StatusBarUtil.getStatusBarHeight(this), 0, 0);
        iv_hotel_second_back = findViewById(R.id.iv_hotel_second_back);

        banner_hotel_second = findViewById(R.id.banner_hotel_second);
        LayoutParamsUtil.setHeight(banner_hotel_second, mHeadBannerDp);
        card_view_hotel_second = findViewById(R.id.card_view_hotel_second);
        LayoutParamsUtil.setMargins(card_view_hotel_second, RxImageTool.dp2px(10), RxImageTool.dp2px(mHeadBannerDp - mModuleTop), RxImageTool.dp2px(10), 0);

        recyclerClassify = findViewById(R.id.recycler_hotel_second_classify);
        recyclerLike = findViewById(R.id.recycler_hotel_second_like);
    }

    private void initData() {
        // 渐变
        final int distance = RxImageTool.dp2px(mHeadBannerDp - mHeadDp) - StatusBarUtil.getStatusBarHeight(this);
        nest_scroll_hotel_second.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                float gap = (float) scrollY / distance;
                if (gap > 1)
                    gap = 1;
                if (gap < 0)
                    gap = 0;
                rl_hotel_second_head_all.setAlpha(gap);
            }
        });

        iv_hotel_second_back.setOnClickListener(this);

        initBanner();
        initSearchModule();
        refreshClassifyModule();
        refreshHolidayHotel();
    }

    /**
     * 初始化搜索条件的module
     */
    private void initSearchModule() {

        rlDestination = findViewById(R.id.rl_hotel_second_search_destination); // 目的地
        rlDestination.setOnClickListener(this);

        rlLocation = findViewById(R.id.rl_hotel_second_search_location); // 定位按钮
        rlLocation.setOnClickListener(this);

        llTime = findViewById(R.id.ll_hotel_second_search_time); // 入离时间
        llTime.setOnClickListener(this);
        tvCheckIn = findViewById(R.id.tv_hotel_second_search_check_in);
        tvCheckOut = findViewById(R.id.tv_hotel_second_search_check_out);
        tvDayCount = findViewById(R.id.tv_hotel_second_search_day_count);

        Calendar c = Calendar.getInstance(); // 初始化当前时间，默认一天
        mStartDate = CalendarUtil.getStringDate(c);
        c.add(Calendar.DAY_OF_MONTH, 1);
        mEndDate = CalendarUtil.getStringDate(c);
        setDateText(mStartDate, mEndDate);

        tvKeyword = findViewById(R.id.tv_hotel_second_search_keyword); // 关键字
        tvKeyword.setOnClickListener(this);

        tvQuery = findViewById(R.id.tv_hotel_second_search_query); // 查询按钮
        tvQuery.setOnClickListener(this);
    }

    /**
     * 刷新酒店分类页面
     */
    private void refreshClassifyModule() {
        int maxShowNumber = 7;  // 最后显示的item,之后添加全部分类
        int spanCount = 4; // 一行显示几个

        // 设置item
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerClassify.setLayoutManager(gridLayoutManager);
        recyclerClassify.setAdapter(new HotelSecondClassifyAdapter(MakeDataUtil.makeClassifyString(), maxShowNumber));
    }

    /**
     * 刷新猜你喜欢模块
     */
    private void refreshHolidayHotel() {

        int maxShowNumber = 10;
        int divide = RxImageTool.dp2px(0.48f);
        List<SimpleString> list = MakeDataUtil.makeSimpleString(11);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerLike.setLayoutManager(manager);
        recyclerLike.setAdapter(new AdapterRecycler<SimpleString>(R.layout.item_hotel, list,
                new AdapterRecycler.Builder().setMaxShowCount(maxShowNumber)) {
            @Override
            public void convert(ViewHolderRecycler holder, SimpleString simpleString, int position) {
                holder.setText(R.id.tv_item_hotel_name, simpleString.getName());
            }
        });
        recyclerLike.addItemDecoration(new CommonDecoration(divide,1, Color.parseColor("#3E3F41")));
    }

    // 弹出日历选择框
    private void popCalendar() {
        int screenHeight = RxDeviceTool.getScreenHeight(this);
        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.pop_calendar)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight * 4 / 5)
                .setBackGroundLevel(0.5f)
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.PopBottom)
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        ImageView ivClose = view.findViewById(R.id.iv_pop_calendar_close);
                        ivClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (popupWindow != null && popupWindow.isShowing()) {
                                    popupWindow.dismiss();
                                    popupWindow = null;
                                }
                            }
                        });
                        HotelCalendarView hotelCalendarView = view.findViewById(R.id.hotel_calendar_view_pop);
                        if (isSelected && mStartDate != null && mEndDate != null) {
                            hotelCalendarView.show(new HotelCalendarView.Builder()
                                    .preStartDate(mStartDate).preEndDate(mEndDate));
                        } else {
                            hotelCalendarView.show();
                        }
                        hotelCalendarView.setSelectedListener(new HotelCalendarView.SelectedListener() {
                            @Override
                            public void selected(final CalendarData start, final CalendarData end) {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        isSelected = true;
                                        mStartDate = start.toString();
                                        mEndDate = end.toString();
                                        setDateText(mStartDate, mEndDate);
                                        if (popupWindow != null && popupWindow.isShowing()) {
                                            popupWindow.dismiss();
                                            popupWindow = null;
                                        }
                                    }
                                }, 500);
                            }
                        });
                    }
                })
                .create();
        popupWindow.showAtLocation(rl_hotel_second_parent, Gravity.BOTTOM, 0, 0);
    }

    // 设置显示时间
    private void setDateText(String start, String end) {
        CalendarData startCalendarData = CalendarUtil.getCalendarData(start);
        CalendarData endCalendarData = CalendarUtil.getCalendarData(end);
        tvCheckIn.setText(startCalendarData.getMonth() + "月" + startCalendarData.getDay() + "日");
        tvCheckOut.setText(endCalendarData.getMonth() + "月" + endCalendarData.getDay() + "日");
        int dayCount = CalendarUtil.getDayCountBetweenBothCalendar(startCalendarData, endCalendarData);
        tvDayCount.setText(dayCount + "晚");
    }

    // 初始化自动滑动的头图
    private void initBanner() {
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.bg2);
        list.add(R.drawable.bg1);
        list.add(R.drawable.bg4);
        list.add(R.drawable.bg3);
        list.add(R.drawable.bg5);
        MyImageLoader imageLoader = new MyImageLoader();

        banner_hotel_second.setImages(list)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_KEYWORD && resultCode == 1) {
            String searchName = data.getStringExtra("searchName");
            tvKeyword.setText(searchName);
        } else if (requestCode == REQUEST_CODE_DESTINATION && resultCode == 1) {

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_hotel_second_back:
                onBackPressed();
                break;
            case R.id.rl_hotel_second_search_destination: // 目的地
                Intent intent = new Intent(HotelSecondActivity.this, HotelDestinationActivity.class);
                startActivityForResult(intent, REQUEST_CODE_DESTINATION);
                break;
            case R.id.rl_hotel_second_search_location: // 定位按钮
                break;
            case R.id.ll_hotel_second_search_time: // 入离时间
                popCalendar();
                break;
            case R.id.tv_hotel_second_search_keyword: // 关键字
                Intent intent2 = new Intent(HotelSecondActivity.this, HotelSearchActivity.class);
                startActivityForResult(intent2, REQUEST_CODE_KEYWORD);
                break;
            case R.id.tv_hotel_second_search_query: // 查询按钮
                break;
        }
    }
}
