package jdjt.com.homepager.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jdjt.com.homepager.R;
import jdjt.com.homepager.adapter.HomeHolidayHotelAdapter;
import jdjt.com.homepager.adapter.HotelSecondClassifyAdapter;
import jdjt.com.homepager.decoration.SimpleItemDecoration;
import jdjt.com.homepager.domain.SimpleString;
import jdjt.com.homepager.util.MakeDataUtil;
import jdjt.com.homepager.util.ViewUtil;
import jdjt.com.homepager.view.commonPopupWindow.CommonPopupWindow;
import jdjt.com.homepager.view.verticalCalendar.CalendarData;
import jdjt.com.homepager.view.verticalCalendar.CalendarUtil;
import jdjt.com.homepager.view.verticalCalendar.HotelCalendarView;

/**
 * Created by xxd on 2018/9/10.
 * 酒店频道
 */

public class HotelSecondActivity extends BaseActivity {

    private Banner banner;
    private LinearLayout llContent;

    // 当前选择的起始日期
    private String mStartDate;
    private String mEndDate;
    private boolean isSelected; // 是否选择过了日历

    private Handler handler = new Handler() {
    };
    // 日历用到的控件
    private CommonPopupWindow popupWindow;
    private TextView tvCheckIn;
    private TextView tvCheckOut;
    private TextView tvDayCount;

    // 关键字
    private TextView tvKeyword;

    private final int REQUEST_CODE_KEYWORD = 1;
    private final int REQUEST_CODE_DESTINATION = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_second);
        initView();
        initData();
    }

    private void initView() {
        banner = findViewById(R.id.banner_hotel_second);
        llContent = findViewById(R.id.ll_nest_content_hotel_second);
    }

    private void initData() {
        initBanner();
        initNestScrollView();
    }

    // 初始化nestscrollview的内容
    private void initNestScrollView() {
        addSearchModule();
        addClassifyModule();
        addHolidayHotel();
    }

    /**
     * 添加猜你喜欢模块
     */
    private void addHolidayHotel() {
        int itemHeight = 132;
        int maxShowNumber = 10;
        int divideSpace = 1;
        List<SimpleString> dataList = MakeDataUtil.makeSimpleString(12, "度假酒店");
        int size = dataList.size() < maxShowNumber ? dataList.size() : maxShowNumber;

        View view = View.inflate(this, R.layout.home_holiday_hotel, null);
        TextView tvTitle = view.findViewById(R.id.tv_home_holiday_hotel_title);
        tvTitle.setText("猜你喜欢");
        TextView tvMore = view.findViewById(R.id.tv_home_holiday_hotel_more);
        tvMore.setVisibility(View.GONE);
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
     * 添加酒店分类页面
     */
    private void addClassifyModule() {
        int itemHeight = 65;
        int maxShowNumber = 7;  // 最后显示的item,之后添加全部分类
        int spanCount = 4; // 一行显示几个

        View view = View.inflate(this, R.layout.hotel_second_classify, null);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_hotel_second_classify);
        ViewUtil.setHeight(recyclerView, itemHeight * (maxShowNumber / spanCount + 1)); // 需要多一个全部
        // 设置item
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), spanCount, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new HotelSecondClassifyAdapter(MakeDataUtil.makeClassifyString(), itemHeight, maxShowNumber));
        llContent.addView(view);

    }

    /**
     * 添加搜索条件的module
     */
    private void addSearchModule() {
        View view = View.inflate(this, R.layout.hotel_second_search, null);

        RelativeLayout rlDestination = view.findViewById(R.id.rl_hotel_second_search_destination); // 入离时间
        rlDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotelSecondActivity.this, HotelDestinationActivity.class);
                startActivityForResult(intent, REQUEST_CODE_DESTINATION);
            }
        });

        LinearLayout llTime = view.findViewById(R.id.ll_hotel_second_search_time); // 入离时间
        tvCheckIn = view.findViewById(R.id.tv_hotel_second_search_check_in);
        tvCheckOut = view.findViewById(R.id.tv_hotel_second_search_check_out);
        tvDayCount = view.findViewById(R.id.tv_hotel_second_search_day_count);
        Calendar c = Calendar.getInstance(); // 初始化当前时间，默认一天
        mStartDate = CalendarUtil.getStringDate(c);
        c.add(Calendar.DAY_OF_MONTH, 1);
        mEndDate = CalendarUtil.getStringDate(c);
        setDateText(mStartDate, mEndDate);
        llTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popCalendar();
            }
        });

        tvKeyword = view.findViewById(R.id.tv_hotel_second_search_keyword); // 关键字
        tvKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotelSecondActivity.this, HotelSearchActivity.class);
                startActivityForResult(intent, REQUEST_CODE_KEYWORD);
            }
        });

        llContent.addView(view);
    }

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
        popupWindow.showAtLocation(llContent, Gravity.BOTTOM, 0, 0);
    }


    public void setDateText(String start, String end) {
        CalendarData startCalendarData = CalendarUtil.getCalendarData(start);
        CalendarData endCalendarData = CalendarUtil.getCalendarData(end);
        tvCheckIn.setText(startCalendarData.getMonth() + "月" + endCalendarData.getDay() + "日");
        tvCheckOut.setText(startCalendarData.getMonth() + "月" + endCalendarData.getDay() + "日");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_KEYWORD && resultCode == 1) {
            String searchName = data.getStringExtra("searchName");
            tvKeyword.setText(searchName);
        } else if (requestCode == REQUEST_CODE_DESTINATION && resultCode == 1) {

        }
    }
}
