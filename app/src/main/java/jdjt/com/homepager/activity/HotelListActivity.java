package jdjt.com.homepager.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.RxImageTool;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jdjt.com.homepager.R;
import jdjt.com.homepager.decoration.CommonDecoration;
import jdjt.com.homepager.domain.HotelDestination;
import jdjt.com.homepager.domain.HotelType;
import jdjt.com.homepager.domain.back.BackHotel;
import jdjt.com.homepager.domain.back.BackHotelType;
import jdjt.com.homepager.domain.back.BackHotelTypeLevel;
import jdjt.com.homepager.domain.back.BackSearchHotel;
import jdjt.com.homepager.http.requestHelper.RequestHelperHotel;
import jdjt.com.homepager.util.GlideLoadUtil;
import jdjt.com.homepager.util.LayoutParamsUtil;
import jdjt.com.homepager.util.StatusBarUtil;
import jdjt.com.homepager.util.ToastUtil;
import jdjt.com.homepager.view.commonPopupWindow.CommonPopupWindow;
import jdjt.com.homepager.view.commonRecyclerView.AdapterMultipleRecycler;
import jdjt.com.homepager.view.commonRecyclerView.AdapterRecycler;
import jdjt.com.homepager.view.commonRecyclerView.MultipleTypeSupport;
import jdjt.com.homepager.view.commonRecyclerView.ViewHolderRecycler;
import jdjt.com.homepager.view.verticalCalendar.CalendarData;
import jdjt.com.homepager.view.verticalCalendar.CalendarUtil;
import jdjt.com.homepager.view.verticalCalendar.HotelCalendarView;

/**
 * Created by xxd on 2018/10/10.
 * 酒店列表页
 */

public class HotelListActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_hotel_list_parent; // 整个页面，pop用到
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

    private LinearLayout ll_hotel_no_internet;
    private TextView tv_hotel_no_internet_reset;

    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;

    private CommonPopupWindow popCalendar; // 日历弹框
    private CommonPopupWindow popSort;  // 排序弹框
    private CommonPopupWindow popHotelType;  // 酒店类型弹框

    private boolean hasKeyword; // 是否已填写关键字
    private boolean hasTime; // 是否已选择入离时间
    private boolean hasDestination; // 是否已选择目的地
    private boolean hasSort; // 是否已选择排序
    private boolean hasType; // 是否已选择过酒店类型
    private boolean isTypeConfirm; // 是否已确定过酒店类型，如果确定，不再使用上个页面传入的数据

    private String mKeyword; // 关键字
    private String mStartDate; // 当前选择的起始日期
    private String mEndDate;
    private HotelDestination mHotelDestination; // 目的地
    private List<BackHotelTypeLevel> mSortList; // 所有排序
    private int mCurrentChoiceSort = -1; // 当前选择的排序方式
    private HotelType mLastHotelType; // 上个页面传入的类型，可能没有
    private List<BackHotelTypeLevel> mTypeList; // 所有类型

    private AdapterMultipleRecycler mAdapter;
    private List<BackHotel> mDataList; // 实际的查询结果数据
    private List<BackHotel> mRecommendList; // 推荐的酒店数据
    private List<BackHotel> mShowList; // 展示的酒店数据


    private Handler handler = new Handler() {
    };

    private final int PAGER_COUNT = 20; // 一次加载20条数据

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
        rl_hotel_list_parent = findViewById(R.id.rl_hotel_list_parent);
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
        ll_hotel_no_internet = findViewById(R.id.ll_hotel_no_internet);
        tv_hotel_no_internet_reset = findViewById(R.id.tv_hotel_no_internet_reset);
    }

    private void initData() {
        iv_hotel_list_back.setOnClickListener(this);
        tv_hotel_list_search.setOnClickListener(this);
        iv_hotel_list_text_clear.setOnClickListener(this);
        rlTime.setOnClickListener(this);
        rlDestination.setOnClickListener(this);
        rlSort.setOnClickListener(this);
        rlType.setOnClickListener(this);
        tv_hotel_no_internet_reset.setOnClickListener(this);

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
            setDestinationText();
        }

        // 初始化酒店类型
        HotelType hotelType = (HotelType) intent.getSerializableExtra("hotel_type");
        if (hotelType != null) { // 带着目的地进入此页面
            hasType = true;
            mLastHotelType = hotelType;
            setHotelTypeText();
        }

        // 初始化关键字
        String keyword = intent.getStringExtra("keyword");
        if (!RxDataTool.isEmpty(keyword)) {
            hasKeyword = true;
            mKeyword = keyword;
            setKeywordText();
        }

//        // 造数据
//        mSortList = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            BackHotelTypeLevel level = new BackHotelTypeLevel();
//            level.setParamName("排序条目" + i);
//            mSortList.add(level);
//        }
//        mTypeList = new ArrayList<>();
//        for (int i = 0; i < 125; i++) {
//            BackHotelTypeLevel level = new BackHotelTypeLevel();
//            level.setParamName("类型*" + i);
//            mTypeList.add(level);
//        }

        mDataList = new ArrayList<>();
        mShowList = new ArrayList<>();
        mRecommendList = new ArrayList<>();
        initRefreshLayout();

        requestHotelType(0);
        // 根据所有初始化的条件查询酒店列表
        searchHotelReset();
    }

    /**
     * 是否重新创建
     *
     * @param createNew
     */
    private void refreshList(boolean createNew) {

        // 展示数据修改
        mShowList.clear();
        if (mDataList.isEmpty()) {
            if (mRecommendList.isEmpty()) {
                mShowList.add(new BackHotel(1));
            } else {
                mShowList.add(new BackHotel(1));
                mShowList.add(new BackHotel(2));
                mShowList.addAll(mRecommendList);
            }
        } else {
            mShowList.addAll(mDataList);
        }

        if (createNew || mAdapter == null) {
            mAdapter = new AdapterMultipleRecycler<BackHotel>(mShowList, new MultipleTypeSupport<BackHotel>() {
                @Override
                public int getLayoutId(int itemType) {
                    if (itemType == 1) {
                        return R.layout.item_hotel_no_data;
                    } else if (itemType == 2) {
                        return R.layout.item_hotel_recommend_head;
                    } else {
                        return R.layout.item_hotel_list;
                    }
                }

                @Override
                public int getItemViewType(int position, BackHotel backHotel) {
                    return backHotel.getNativeType();
                }
            }) {
                @Override
                public void convert(ViewHolderRecycler holder, BackHotel backHotel, int position) {
                    if (backHotel.getNativeType() != 0)
                        return;
                    ImageView ivIcon = holder.getView(R.id.iv_item_hotel_detail_icon);
                    GlideLoadUtil.loadImage(HotelListActivity.this, backHotel.getHotelHeadImage(), ivIcon);
                    holder.setText(R.id.tv_item_search_hotel_name, backHotel.getHotelName() + "");
                    holder.setText(R.id.tv_item_hotel_detail_address, backHotel.getHotelAddress() + "");

                    TextView tvCallLight = holder.getView(R.id.tv_item_hotel_detail_call_light);
                    TextView tvCallUnLight = holder.getView(R.id.tv_item_hotel_detail_call_unlight);
                    TextView tv3DMapLight = holder.getView(R.id.tv_item_hotel_detail_3d_map_light);
                    TextView tv3DMapUnLight = holder.getView(R.id.tv_item_hotel_detail_3d_map_unlight);
                    TextView tvWifiLight = holder.getView(R.id.tv_item_hotel_detail_wifi_light);
                    TextView tvWifiUnLight = holder.getView(R.id.tv_item_hotel_detail_wifi_unlight);
                    boolean hasCall = "1".equals(backHotel.getIsGhost());
                    boolean has3DMap = "1".equals(backHotel.getIs3DMap());
                    boolean hasWifi = "1".equals(backHotel.getIsFreeWifi());
                    tvCallLight.setVisibility(hasCall ? View.VISIBLE : View.GONE);
                    tvCallUnLight.setVisibility(!hasCall ? View.VISIBLE : View.GONE);
                    tv3DMapLight.setVisibility(has3DMap ? View.VISIBLE : View.GONE);
                    tv3DMapUnLight.setVisibility(!has3DMap ? View.VISIBLE : View.GONE);
                    tvWifiLight.setVisibility(hasWifi ? View.VISIBLE : View.GONE);
                    tvWifiUnLight.setVisibility(!hasWifi ? View.VISIBLE : View.GONE);

                    holder.setText(R.id.tv_item_hotel_detail_score, backHotel.getHotelScore() + "分");
                    holder.setText(R.id.tv_item_hotel_detail_comment_count, RxDataTool.stringToInt(backHotel.getScoreCount()) + "条点评");
                    ImageView ivGift = holder.getView(R.id.iv_item_hotel_detail_gift);
                    ivGift.setVisibility(View.GONE);
                    holder.setText(R.id.tv_item_hotel_detail_price_money, String.format("¥%s起", RxDataTool.stringToInt(backHotel.getRoomTypePriceMin())));

                    holder.setOnClickListener(R.id.iv_item_hotel_detail_map, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO 3D map
                        }
                    });
                    holder.setOnClickListener(R.id.rl_item_hotel_detail, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO 跳转单体酒店
                        }
                    });
                }
            };
            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new CommonDecoration(RxImageTool.dp2px(0.48f), 1, Color.parseColor("#393A3C")));
        }
    }

    private void initRefreshLayout() {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Flowable.fromArray(1)
                        .map(new Function<Integer, BackSearchHotel>() {
                            @Override
                            public BackSearchHotel apply(Integer integer) throws Exception {
                                return RequestHelperHotel.getInstance().searchHotel("1", PAGER_COUNT + ""
                                        , hasKeyword ? mKeyword : null, mStartDate, mEndDate, hasDestination ? mHotelDestination : null,
                                        hasSort ? mSortList.get(mCurrentChoiceSort) : null, hasType ? getChoiceHotelType() : null);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) throws Exception {
                            }
                        })
                        .subscribe(new Consumer<BackSearchHotel>() {
                            @Override
                            public void accept(BackSearchHotel backSearchHotel) throws Exception {
                                mDataList.clear(); // 刷新成功，清除之前的数据
                                mDataList.addAll(backSearchHotel.getListHotel());
                                refreshList(true);
                                smartRefreshLayout.finishRefresh();
                                if (mDataList.size() >= RxDataTool.stringToInt(backSearchHotel.getCount())) {
                                    smartRefreshLayout.setNoMoreData(true);
                                } else {
                                    smartRefreshLayout.setNoMoreData(false);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                smartRefreshLayout.finishRefresh(false);
                                ToastUtil.showToast(getApplicationContext(), throwable);
                            }
                        });
            }
        });

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Flowable.fromArray(1)
                        .map(new Function<Integer, BackSearchHotel>() {
                            @Override
                            public BackSearchHotel apply(Integer integer) throws Exception {
                                return RequestHelperHotel.getInstance().searchHotel("1", mDataList.size() / PAGER_COUNT + 1 + ""
                                        , hasKeyword ? mKeyword : null, mStartDate, mEndDate, hasDestination ? mHotelDestination : null,
                                        hasSort ? mSortList.get(mCurrentChoiceSort) : null, hasType ? getChoiceHotelType() : null);

                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) throws Exception {
                            }
                        })
                        .subscribe(new Consumer<BackSearchHotel>() {
                            @Override
                            public void accept(BackSearchHotel backSearchHotel) throws Exception {
                                if (mDataList.size() >= RxDataTool.stringToInt(backSearchHotel.getCount())) { // 已经加载完所有，下拉后重新加载的问题
                                    smartRefreshLayout.finishLoadMoreWithNoMoreData();
                                } else {
                                    mDataList.addAll(backSearchHotel.getListHotel());
                                    refreshList(false);
                                    if (mDataList.size() >= RxDataTool.stringToInt(backSearchHotel.getCount())) {
                                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                                    } else {
                                        smartRefreshLayout.finishLoadMore();
                                        smartRefreshLayout.setNoMoreData(false);
                                    }
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                smartRefreshLayout.finishLoadMore(false);
                                ToastUtil.showToast(getApplicationContext(), throwable);
                            }
                        });
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_hotel_list_back:
                onBackPressed();
                break;
            case R.id.tv_hotel_list_search: // 跳转到酒店关键字页面
                goToHotelSearch(true);
                break;
            case R.id.iv_hotel_list_text_clear: // 跳转到酒店关键字页面,不带关键字
                goToHotelSearch(false);
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
            case R.id.tv_hotel_no_internet_reset: // 无网络
                searchHotelReset();
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
                                        searchHotelReset();
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

        popCalendar.showAtLocation(rl_hotel_list_parent, Gravity.BOTTOM, 0, 0);
    }

    // 弹出排序选择框
    private void popSort() {
        if (mSortList == null) {
            requestHotelType(3);
            return;
        }
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
                                            searchHotelReset();
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

    private AdapterRecycler mAdapterHotelType; // 酒店类型的adapter

    // 弹出酒店类型选择框
    private void popHotelType() {
        if (mTypeList == null) {
            requestHotelType(4);
            return;
        }

        popHotelType = new CommonPopupWindow.Builder(this)
                .setView(R.layout.pop_hotel_list_type)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)
                .setOutsideTouchable(true)
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {

                    @Override
                    public void getChildView(View view, int layoutResId) {

                        final List<BackHotelTypeLevel> copyTypeData = copyTypeData();
                        // 确定、重置
                        TextView tvReset = view.findViewById(R.id.tv_pop_hotel_list_type_reset);
                        TextView tvConfirm = view.findViewById(R.id.tv_pop_hotel_list_type_confirm);

                        tvReset.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                clearTypeChoice(copyTypeData);
                                if (mAdapterHotelType != null)
                                    mAdapterHotelType.notifyDataSetChanged();
                            }
                        });
                        tvConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mTypeList = copyTypeData;
                                isTypeConfirm = true; // 确认点击过了
                                hasType = false;
                                for (BackHotelTypeLevel level : mTypeList) {
                                    if (level.isChoice()) {
                                        hasType = true;
                                        break;
                                    }
                                }
                                setHotelTypeText();
                                if (popHotelType != null && popHotelType.isShowing()) {
                                    popHotelType.dismiss();
                                    popHotelType = null;
                                }
                                searchHotelReset();
                            }
                        });

                        // recycler
                        mAdapterHotelType = null;
                        int lineCount = 4;
                        int itemHeight = RxImageTool.dp2px(30);
                        int divide = RxImageTool.dp2px(10);
                        RecyclerView recyclerView = view.findViewById(R.id.recycler_pop_hotel_list_type);
                        GridLayoutManager manager = new GridLayoutManager(HotelListActivity.this, lineCount, GridLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(manager);
                        mAdapterHotelType = new AdapterRecycler<BackHotelTypeLevel>(R.layout.item_hotel_type, copyTypeData,
                                new AdapterRecycler.Builder().setItemHeight(itemHeight)) {
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
                                        mAdapterHotelType.notifyDataSetChanged();
                                    }
                                });
                            }
                        };
                        recyclerView.setAdapter(mAdapterHotelType);
                        recyclerView.addItemDecoration(new CommonDecoration(divide, lineCount, Color.TRANSPARENT));

                        // 计算recycler的最大高度

                        int row = (mAdapterHotelType.getItemCount() + lineCount - 1) / lineCount;
                        int divideCount = row - 1 > 0 ? row - 1 : 0;

                        // 高度+padding
                        int recyclerHeight = itemHeight * row + divide * divideCount + RxImageTool.dp2px(10 + 10);

                        // 屏幕下方剩余的给recycler展示的最大高度
                        int[] position2 = new int[2];
                        v_divide_1.getLocationOnScreen(position2);
                        int screenHeights = RxDeviceTool.getScreenHeights(HotelListActivity.this);
                        // 2个控件的高度
                        int maxLeftHeight = screenHeights - position2[1] - RxImageTool.dp2px(1 + 38);

                        if (recyclerHeight > maxLeftHeight) {
                            LayoutParamsUtil.setHeightPx(recyclerView, maxLeftHeight);
                        }
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

    // 复制酒店类型
    private List<BackHotelTypeLevel> copyTypeData() {
        if (mTypeList == null)
            return null;
        List<BackHotelTypeLevel> copyList = new ArrayList<>();
        for (BackHotelTypeLevel level : mTypeList) {
            BackHotelTypeLevel copy = new BackHotelTypeLevel();
            copy.setChoice(level.isChoice());
            copy.setParamCode(level.getParamCode());
            copy.setParamName(level.getParamName());
            copyList.add(copy);
        }
        return copyList;
    }

    // 清除酒店类型的所有已勾选
    private void clearTypeChoice(List<BackHotelTypeLevel> list) {
        if (RxDataTool.isEmpty(list))
            return;
        for (BackHotelTypeLevel level : list) {
            level.setChoice(false);
        }
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

    // 设置关键字
    private void setKeywordText() {
        if (hasKeyword) {
            tv_hotel_list_search.setText(mKeyword);
            iv_hotel_list_text_clear.setVisibility(View.VISIBLE);
        } else {
            tv_hotel_list_search.setText(DEFAULT_SEARCH_HINT);
            iv_hotel_list_text_clear.setVisibility(View.GONE);
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
            } else if (!isTypeConfirm && mLastHotelType != null) {  // 没有点击过确定，并且上个页面传入的不为nul
                tvType.setText(String.format("%s", mLastHotelType.getName()));
            }
        } else {
            tvType.setTextColor(getResources().getColor(R.color.white));
            tvType.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.drawable.icon_hotel_arrow_down_gray), null);
            tvType.setText("酒店类型");
        }
    }

    /**
     * 跳转到酒店查询页面
     *
     * @param needKeyword 是否带上当前关键字
     */
    private void goToHotelSearch(boolean needKeyword) {
        Intent intent = new Intent(this, HotelSearchActivity.class);
        if (needKeyword && hasKeyword) {
            intent.putExtra("lastKeyword", mKeyword);
        }
        startActivityForResult(intent, TO_HOTEL_SEARCH_ACTIVITY);
    }

    // 跳转到目的地页面
    private void goToDestination() {
        Intent intent = new Intent(this, HotelDestinationActivity.class);
        startActivityForResult(intent, TO_DESTINATION_ACTIVITY);
    }

    // 清除排序数据
    private void clearSortData() {
        hasSort = false;
        mCurrentChoiceSort = -1;
        setSortText();
    }

    // 清除类型数据
    private void clearTypeData() {
        hasType = false;
        if (mTypeList != null) {
            for (BackHotelTypeLevel level : mTypeList) {
                level.setChoice(false);
            }
        }
        setHotelTypeText();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_HOTEL_SEARCH_ACTIVITY && resultCode == 1) {  // 关键字
            String searchName = data.getStringExtra("searchName");
            if (RxDataTool.isEmpty(searchName)) {
                hasKeyword = false;
                mKeyword = null;
                setKeywordText();
                clearSortData();
                clearTypeData();
                searchHotelReset();
            } else {
                hasKeyword = true;
                mKeyword = searchName;
                setKeywordText();
                clearSortData();
                clearTypeData();
                searchHotelReset();
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
                searchHotelReset();
            }
        }
    }

    // 获取所有选择过的酒店类型
    private List<BackHotelTypeLevel> getChoiceHotelType() {
        List<BackHotelTypeLevel> choiceTypeList = new ArrayList<>();
        if (hasType) {
            if (!isTypeConfirm) { // 用上个页面的数据查
                BackHotelTypeLevel level = new BackHotelTypeLevel();
                level.setParamCode(mLastHotelType.getId());
                level.setParamName(mLastHotelType.getName());
                choiceTypeList.add(level);
            } else {
                if (!RxDataTool.isEmpty(mTypeList)) {
                    for (BackHotelTypeLevel level : mTypeList) {
                        if (level.isChoice())
                            choiceTypeList.add(level);
                    }
                }
            }
        }
        return choiceTypeList;
    }

    // 重置list的查询，从第1也开始查询，查询后无论成功失败都展示新的页面，与上拉、下拉不同
    private void searchHotelReset() {
        Flowable.fromArray(1)
                .map(new Function<Integer, BackSearchHotel>() {
                    @Override
                    public BackSearchHotel apply(Integer integer) throws Exception {
                        return RequestHelperHotel.getInstance().searchHotel("1", PAGER_COUNT + ""
                                , hasKeyword ? mKeyword : null, mStartDate, mEndDate, hasDestination ? mHotelDestination : null,
                                hasSort ? mSortList.get(mCurrentChoiceSort) : null, hasType ? getChoiceHotelType() : null);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                    }
                })
                .subscribe(new Consumer<BackSearchHotel>() {
                    @Override
                    public void accept(BackSearchHotel backSearchHotel) throws Exception {
                        ll_hotel_no_internet.setVisibility(View.GONE);
                        mDataList.clear(); // 清除之前数据
                        mDataList.addAll(backSearchHotel.getListHotel());
                        refreshList(true);
                        if (mDataList.size() >= RxDataTool.stringToInt(backSearchHotel.getCount())) {
                            smartRefreshLayout.setNoMoreData(true);
                        } else {
                            smartRefreshLayout.setNoMoreData(false);
                        }
                        mRecommendList = backSearchHotel.getRecommendHotelList();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ll_hotel_no_internet.setVisibility(View.VISIBLE);
                        mDataList.clear(); // 清除之前数据
                        refreshList(true);
                        smartRefreshLayout.setNoMoreData(false);
                        ToastUtil.showToast(getApplicationContext(), throwable.getMessage());
                    }
                });
    }

    /**
     * 请求分类数据，并展示
     *
     * @param popType 3:展开排序 4：展开类型  其他：不展开
     */
    private void requestHotelType(final int popType) {
        Flowable.fromArray(1)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        List<BackHotelType> backHotelTypes = RequestHelperHotel.getInstance().requestHotelType();
                        if (backHotelTypes != null) {
                            for (BackHotelType type : backHotelTypes) {
                                if ("3".equals(type.getParamType())) {  // 酒店类型
                                    mTypeList = type.getParamContent();
                                    if (mLastHotelType != null && mTypeList != null) { // 对应选择
                                        for (BackHotelTypeLevel level : mTypeList) {
                                            if (level.getParamCode().equals(mLastHotelType.getId()))
                                                level.setChoice(true);
                                        }
                                    }
                                } else if ("4".equals(type.getParamType())) { // 排序规则
                                    mSortList = type.getParamContent();
                                }
                            }
                        }
                        return integer;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (popType == 3) {
                            popSort();
                        } else if (popType == 4) {
                            popHotelType();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showToast(getApplicationContext(), throwable.getMessage());
                    }
                });
    }

}
