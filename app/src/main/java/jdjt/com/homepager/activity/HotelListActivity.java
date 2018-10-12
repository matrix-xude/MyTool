package jdjt.com.homepager.activity;

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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.RxImageTool;
import com.vondear.rxtool.view.RxToast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jdjt.com.homepager.R;
import jdjt.com.homepager.decoration.CommonDecoration;
import jdjt.com.homepager.domain.HotelDestination;
import jdjt.com.homepager.domain.HotelType;
import jdjt.com.homepager.domain.back.BackHotelTypeLevel;
import jdjt.com.homepager.util.StatusBarUtil;
import jdjt.com.homepager.view.commonPopupWindow.CommonPopupWindow;
import jdjt.com.homepager.view.commonRecyclerView.AdapterRecycler;
import jdjt.com.homepager.view.commonRecyclerView.ViewHolderRecycler;
import jdjt.com.homepager.view.verticalCalendar.CalendarData;
import jdjt.com.homepager.view.verticalCalendar.CalendarUtil;
import jdjt.com.homepager.view.verticalCalendar.HotelCalendarView;

/**
 * Created by xxd on 2018/10/10.
 * 酒店列表页
 */

public class HotelListActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_hotel_list_parent; // 整个页面，pop用到
    private View v_divide_1; // 搜索条件下的分割线，pop用到

    private ImageView iv_hotel_list_back; // 返回
    private TextView tv_hotel_list_search; // search
    private ImageView iv_hotel_list_text_clear; // 小叉号

    private RelativeLayout rlTime; // 入离时间
    private RelativeLayout rlDestination; // 目的地
    private RelativeLayout rlSort; // 排序
    private RelativeLayout rlType; // 酒店类型
    private TextView tvTime;
    private TextView tvDestination;
    private TextView tvSort;
    private TextView tvType;

    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;

    private CommonPopupWindow popCalendar; // 日历弹框
    private CommonPopupWindow popSort;  // 排序弹框
    private CommonPopupWindow popHotelType;  // 酒店类型弹框

    private boolean hasKeyword; // 是否已填写关键字
    private boolean hasTime; // 是否已选择入离时间
    private boolean hasDestination; // 是否已选择目的地
    private boolean hasSort; // 是否已选择排序
    private boolean hasType; // 是否已选择酒店类型

    private String mKeyword; // 关键字
    private String mStartDate; // 当前选择的起始日期
    private String mEndDate;
    private HotelDestination mHotelDestination; // 目的地
    private List<BackHotelTypeLevel> mSortList; // 所有排序
    private int mCurrentChoiceSort = -1; // 当前选择的排序方式
    private HotelType mLastHotelType; // 上个页面传入的类型，可能没有
    private List<BackHotelTypeLevel> mTypeList; // 所有类型

    private Handler handler = new Handler() {
    };

    private final int TO_HOTEL_SEARCH_ACTIVITY = 1; // 跳转到酒店关键字
    private final int TO_DESTINATION_ACTIVITY = 2; // 跳转到目的地页面
    private final String DEFAULT_SEARCH_HINT = "大家都在搜：红树林"; // 默认搜索提示

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.bg_content));
        setContentView(R.layout.activity_hotel_list);
        initView();
        initData();
    }

    private void initView() {
        ll_hotel_list_parent = findViewById(R.id.ll_hotel_list_parent);
        v_divide_1 = findViewById(R.id.v_divide_1);
        iv_hotel_list_back = findViewById(R.id.iv_hotel_list_back);
        tv_hotel_list_search = findViewById(R.id.tv_hotel_list_search);
        iv_hotel_list_text_clear = findViewById(R.id.iv_hotel_list_text_clear);
        rlTime = findViewById(R.id.rl_hotel_list_time);
        rlDestination = findViewById(R.id.rl_hotel_list_destination);
        rlSort = findViewById(R.id.rl_hotel_list_sort);
        rlType = findViewById(R.id.rl_hotel_list_type);
        tvTime = findViewById(R.id.tv_hotel_list_time);
        tvDestination = findViewById(R.id.tv_hotel_list_destination);
        tvSort = findViewById(R.id.tv_hotel_list_sort);
        tvType = findViewById(R.id.tv_hotel_list_type);
        smartRefreshLayout = findViewById(R.id.smart_refresh_layout_hotel_list);
        recyclerView = findViewById(R.id.recycler_hotel_list);
    }

    private void initData() {
        iv_hotel_list_back.setOnClickListener(this);
        tv_hotel_list_search.setOnClickListener(this);
        iv_hotel_list_text_clear.setOnClickListener(this);
        rlTime.setOnClickListener(this);
        rlDestination.setOnClickListener(this);
        rlSort.setOnClickListener(this);
        rlType.setOnClickListener(this);

        Intent intent = getIntent();

        // 初始化入离时间
        mStartDate = intent.getStringExtra("startDate");
        mEndDate = intent.getStringExtra("endDate");
        if (mStartDate != null && mEndDate != null) {
            hasTime = true;
            setDateText();
        } else { // 没选时间进入此页，默认一天
            Calendar c = Calendar.getInstance();
            mStartDate = CalendarUtil.getStringDate(c);
            c.add(Calendar.DAY_OF_MONTH, 1);
            mEndDate = CalendarUtil.getStringDate(c);
            hasTime = true;
            setDateText();
        }

        // 初始化目的地
        HotelDestination hotelDestination = (HotelDestination) intent.getSerializableExtra("destination");
        if (hotelDestination != null) { // 带着目的地进入此页面
            hasDestination = true;
            mHotelDestination = hotelDestination;
            setDateText();
        }

        // 初始化酒店类型
        HotelType hotelType = (HotelType) intent.getSerializableExtra("hotel_type");
        if (hotelType != null) { // 带着目的地进入此页面
            hasType = true;
            mLastHotelType = hotelType;
        }

        // 造数据
        mSortList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BackHotelTypeLevel level = new BackHotelTypeLevel();
            level.setParamName("排序条目" + i);
            mSortList.add(level);
        }
        mTypeList = new ArrayList<>();
        for (int i = 0; i < 125; i++) {
            BackHotelTypeLevel level = new BackHotelTypeLevel();
            level.setParamName("类型*" + i);
            mTypeList.add(level);
        }
    }

    // 刷新列表
    private void refreshList() {
        RxToast.showToast("我刷新了");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_hotel_list_back:
                onBackPressed();
                break;
            case R.id.tv_hotel_list_search: // 跳转到酒店关键字页面
                goToHotelSearch();
                break;
            case R.id.iv_hotel_list_text_clear: // 跳转到酒店关键字页面,并不在此时清空关键字
                goToHotelSearch();
                break;
            case R.id.rl_hotel_list_time: // 入离时间
                popCalendar();
                break;
            case R.id.rl_hotel_list_destination: // 目的地
                goToDestination();
                break;
            case R.id.rl_hotel_list_sort: // 排序
                popSort();
                break;
            case R.id.rl_hotel_list_type: // 酒店类型
                popHotelType();
                break;
        }
    }

    // 弹出日历选择框
    private void popCalendar() {
        int screenHeight = RxDeviceTool.getScreenHeight(this);
        popCalendar = new CommonPopupWindow.Builder(this)
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
                                if (popCalendar != null && popCalendar.isShowing()) {
                                    popCalendar.dismiss();
                                    popCalendar = null;
                                }
                            }
                        });
                        HotelCalendarView hotelCalendarView = view.findViewById(R.id.hotel_calendar_view_pop);
                        if (hasTime && mStartDate != null && mEndDate != null) {
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
                                        hasTime = true;
                                        mStartDate = start.toString();
                                        mEndDate = end.toString();
                                        setDateText();
                                        if (popCalendar != null && popCalendar.isShowing()) {
                                            popCalendar.dismiss();
                                            popCalendar = null;
                                        }
                                        refreshList();
                                    }
                                }, 500);
                            }
                        });
                    }
                })
                .create();

        // 箭头朝向、文字颜色处理
        popCalendar.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tvTime.setTextColor(getResources().getColor(hasTime ? R.color.bg_light : R.color.white));
                tvTime.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable
                        (hasTime ? R.drawable.icon_hotel_arrow_down_orange : R.drawable.icon_hotel_arrow_down_gray), null);
            }
        });
        tvTime.setTextColor(getResources().getColor(R.color.bg_light));
        tvTime.setCompoundDrawablesWithIntrinsicBounds(null, null,
                getResources().getDrawable(R.drawable.icon_hotel_arrow_up_orange), null);

        popCalendar.showAtLocation(ll_hotel_list_parent, Gravity.BOTTOM, 0, 0);
    }

    // 弹出排序选择框
    private void popSort() {
        popSort = new CommonPopupWindow.Builder(this)
                .setView(R.layout.pop_hotel_list_sort)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)
                .setOutsideTouchable(true)
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {

                    @Override
                    public void getChildView(View view, int layoutResId) {
                        RecyclerView recyclerView = view.findViewById(R.id.recycler_pop_hotel_list_sort);
                        LinearLayoutManager manager = new LinearLayoutManager(HotelListActivity.this, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(new AdapterRecycler<BackHotelTypeLevel>(R.layout.item_hotel_sort, mSortList) {
                            @Override
                            public void convert(ViewHolderRecycler holder, final BackHotelTypeLevel backHotelTypeLevel, final int position) {
                                RelativeLayout rlItem = holder.getView(R.id.rl_item_hotel_sort);
                                TextView tvName = holder.getView(R.id.tv_item_hotel_sort_name);
                                ImageView ivCheck = holder.getView(R.id.iv_item_hotel_sort_check);
                                tvName.setText(backHotelTypeLevel.getParamName());
                                if (mCurrentChoiceSort == position) {
                                    tvName.setTextColor(getResources().getColor(R.color.bg_light));
                                    ivCheck.setVisibility(View.VISIBLE);
                                } else {
                                    tvName.setTextColor(getResources().getColor(R.color.white));
                                    ivCheck.setVisibility(View.GONE);
                                }
                                rlItem.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (mCurrentChoiceSort == position) { // 点击了已经选择的排序
                                            if (popSort != null && popSort.isShowing()) {
                                                popSort.dismiss();
                                                popSort = null;
                                            }
                                        } else {
                                            hasSort = true;
                                            mCurrentChoiceSort = position;
                                            setSortText();
                                            if (popSort != null && popSort.isShowing()) {
                                                popSort.dismiss();
                                                popSort = null;
                                            }
                                            refreshList();
                                        }
                                    }
                                });
                            }
                        });
                        recyclerView.addItemDecoration(new CommonDecoration(RxImageTool.dp2px(0.96f), 1, Color.parseColor("#393A3C")));
                    }
                })
                .create();

        // 箭头朝向、文字颜色处理
        popSort.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tvSort.setTextColor(getResources().getColor(hasSort ? R.color.bg_light : R.color.white));
                tvSort.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable
                        (hasSort ? R.drawable.icon_hotel_arrow_down_orange : R.drawable.icon_hotel_arrow_down_gray), null);
            }
        });
        tvSort.setTextColor(getResources().getColor(R.color.bg_light));
        tvSort.setCompoundDrawablesWithIntrinsicBounds(null, null,
                getResources().getDrawable(R.drawable.icon_hotel_arrow_up_orange), null);

        popSort.showAsDropDown(v_divide_1);
    }

    private AdapterRecycler adapter; // 酒店类型的adapter

    // 弹出酒店类型选择框
    private void popHotelType() {

        popHotelType = new CommonPopupWindow.Builder(this)
                .setView(R.layout.pop_hotel_list_type)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)
                .setOutsideTouchable(true)
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {

                    @Override
                    public void getChildView(View view, int layoutResId) {
                        adapter = null;
                        int lineCount = 4;
                        RecyclerView recyclerView = view.findViewById(R.id.recycler_pop_hotel_list_type);
                        GridLayoutManager manager = new GridLayoutManager(HotelListActivity.this, lineCount, GridLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(manager);
                        adapter = new AdapterRecycler<BackHotelTypeLevel>(R.layout.item_hotel_type, mTypeList) {
                            @Override
                            public void convert(ViewHolderRecycler holder, final BackHotelTypeLevel backHotelTypeLevel, final int position) {
                                TextView tvName = holder.getView(R.id.tv_item_hotel_type_name);
                                tvName.setText(backHotelTypeLevel.getParamName());
                                if (backHotelTypeLevel.isChoice()) {
                                    tvName.setTextColor(getResources().getColor(R.color.bg_light));
                                    tvName.setBackground(getResources().getDrawable(R.drawable.shape_hotel_type_choice));
                                } else {
                                    tvName.setTextColor(getResources().getColor(R.color.white));
                                    tvName.setBackground(getResources().getDrawable(R.drawable.shape_hotel_type_unchoice));
                                }
                                tvName.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        backHotelTypeLevel.setChoice(!backHotelTypeLevel.isChoice());
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        };
                        recyclerView.setAdapter(adapter);
                        recyclerView.addItemDecoration(new CommonDecoration(RxImageTool.dp2px(10), lineCount, Color.TRANSPARENT));
                    }
                })
                .create();

        // 箭头朝向、文字颜色处理
        popHotelType.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tvType.setTextColor(getResources().getColor(hasType ? R.color.bg_light : R.color.white));
                tvType.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable
                        (hasType ? R.drawable.icon_hotel_arrow_down_orange : R.drawable.icon_hotel_arrow_down_gray), null);
            }
        });
        tvType.setTextColor(getResources().getColor(R.color.bg_light));
        tvType.setCompoundDrawablesWithIntrinsicBounds(null, null,
                getResources().getDrawable(R.drawable.icon_hotel_arrow_up_orange), null);

        popHotelType.showAsDropDown(v_divide_1);
    }

    // 设置入离日期文字
    private void setDateText() {
        if (hasTime) {
            tvTime.setTextColor(getResources().getColor(R.color.bg_light));
            tvTime.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.drawable.icon_hotel_arrow_down_orange), null);
            CalendarData startCalendarData = CalendarUtil.getCalendarData(mStartDate);
            CalendarData endCalendarData = CalendarUtil.getCalendarData(mEndDate);
            int dayCount = CalendarUtil.getDayCountBetweenBothCalendar(startCalendarData, endCalendarData);
            tvTime.setText(String.format("%s月%s日\r\n入住%s晚", startCalendarData.getMonth(), startCalendarData.getDay(), dayCount));
        } else {
            tvTime.setTextColor(getResources().getColor(R.color.white));
            tvTime.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.drawable.icon_hotel_arrow_down_gray), null);
            tvTime.setText("入离日期");
        }
    }

    // 设置目的地
    private void setDestinationText() {
        if (hasDestination) {
            tvDestination.setTextColor(getResources().getColor(R.color.bg_light));
            tvDestination.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.drawable.icon_hotel_arrow_down_orange), null);
            tvDestination.setText(String.format("%s", mHotelDestination.getName()));
        } else {
            tvDestination.setTextColor(getResources().getColor(R.color.white));
            tvDestination.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.drawable.icon_hotel_arrow_down_gray), null);
            tvDestination.setText("目的地");
        }
    }

    // 设置排序
    private void setSortText() {
        if (hasSort) {
            tvSort.setTextColor(getResources().getColor(R.color.bg_light));
            tvSort.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.drawable.icon_hotel_arrow_down_orange), null);
            if (mCurrentChoiceSort >= 0 && mSortList != null) {
                tvSort.setText(String.format("%s", mSortList.get(mCurrentChoiceSort).getParamName()));
            }
        } else {
            tvSort.setTextColor(getResources().getColor(R.color.white));
            tvSort.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.drawable.icon_hotel_arrow_down_gray), null);
            tvSort.setText("推荐排序");
        }
    }


    // 设置酒店类型
    private void setHotelTypeText() {
        if (hasType) {
            tvType.setTextColor(getResources().getColor(R.color.bg_light));
            tvType.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.drawable.icon_hotel_arrow_down_orange), null);
            if (mTypeList != null) {
                for (BackHotelTypeLevel level : mTypeList) {
                    if (level.isChoice()) {
                        tvType.setText(String.format("%s", level.getParamName()));
                        break;
                    }
                }
            }
        } else {
            tvType.setTextColor(getResources().getColor(R.color.white));
            tvType.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.drawable.icon_hotel_arrow_down_gray), null);
            tvType.setText("酒店类型");
        }
    }

    // 跳转到酒店查询页面
    private void goToHotelSearch() {
        Intent intent = new Intent(this, HotelSearchActivity.class);
        if (hasKeyword && !RxDataTool.isEmpty(tv_hotel_list_search.getText().toString())) {
            intent.putExtra("lastKeyword", mKeyword);
        }
        startActivityForResult(intent, TO_HOTEL_SEARCH_ACTIVITY);
    }

    // 跳转到目的地页面
    private void goToDestination() {
        Intent intent = new Intent(this, HotelDestinationActivity.class);
        startActivityForResult(intent, TO_DESTINATION_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_HOTEL_SEARCH_ACTIVITY && resultCode == 1) {  // 关键字
            String searchName = data.getStringExtra("searchName");
            if (RxDataTool.isEmpty(searchName)) {
                hasKeyword = false;
                mKeyword = null;
                tv_hotel_list_search.setText(DEFAULT_SEARCH_HINT);
                iv_hotel_list_text_clear.setVisibility(View.GONE);
                refreshList();
            } else {
                hasKeyword = true;
                mKeyword = searchName;
                tv_hotel_list_search.setText(mKeyword);
                iv_hotel_list_text_clear.setVisibility(View.VISIBLE);
                refreshList();
            }
        } else if (requestCode == TO_DESTINATION_ACTIVITY && resultCode == 1) { // 目的地页面
            HotelDestination hotelDestination = (HotelDestination) data.getSerializableExtra("destination");
            if (hotelDestination == null) {
                hasDestination = false;
                mHotelDestination = null;
            } else {
                hasDestination = true;
                mHotelDestination = hotelDestination;
                setDestinationText();
                refreshList();
            }
        }
    }
}
