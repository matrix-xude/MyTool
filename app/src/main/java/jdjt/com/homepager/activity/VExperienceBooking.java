package jdjt.com.homepager.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxImageTool;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jdjt.com.homepager.R;
import jdjt.com.homepager.domain.VExperienceCalendarData;
import jdjt.com.homepager.domain.back.BackVExperienceInfo;
import jdjt.com.homepager.domain.back.BackVExperienceInfoCalendar;
import jdjt.com.homepager.domain.back.BackVExperienceInfoLevel1;
import jdjt.com.homepager.domain.back.BackVExperienceInfoLevel2;
import jdjt.com.homepager.http.requestHelper.RequestHelperVCard;
import jdjt.com.homepager.util.LayoutParamsUtil;
import jdjt.com.homepager.util.StatusBarUtil;
import jdjt.com.homepager.util.ToastUtil;
import jdjt.com.homepager.view.commonPopupWindow.CommonPopupWindow;
import jdjt.com.homepager.view.commonRecyclerView.AdapterRecycler;
import jdjt.com.homepager.view.commonRecyclerView.ViewHolderRecycler;
import jdjt.com.homepager.view.verticalCalendar.CalendarData;
import jdjt.com.homepager.view.verticalCalendar.CalendarDelegate;
import jdjt.com.homepager.view.verticalCalendar.CalendarUtil;
import jdjt.com.homepager.view.verticalCalendar.VExperienceMonthView;

/**
 * Created by xxd on 2018/10/31.
 * V客会体验卡预约入住
 */

public class VExperienceBooking extends BaseActivity implements View.OnClickListener {

    private NestedScrollView nest_scroll_v_experience;
    private TextView tvHotelName; // 选择酒店
    private TextView tvYear;  // 当前月份对应的年份
    private SlidingTabLayout slidingTab; // 月份切换
    private ViewPager viewPagerCalendar; // 月份日历
    private TextView tvBigBed; // 大床
    private TextView tvDoubleBed; // 双床
    private ImageView ivRoomMinus; // -
    private ImageView ivRoomPlus; // +
    private TextView tvRoomCount; // 房间数
    private EditText etName;
    private EditText etPhone;
    private LinearLayout llMoreNeed; // 更多需求点击区域
    private TextView tvMoreNeed; // 更多需求文字
    private TextView tvCheckDescribe; // 选择核销码描述
    private RecyclerView recyclerViewCheck; // 选择核销码勾选
    private TextView tvCommit; // 提交

    private BackVExperienceInfo mVExperienceInfo; // V客会体验卡数据

    private boolean firstMeasure; // 是否第一次测量了高度
    private VExperienceCalendarData mCalendarData; // 当前日历数据

    private int mCurrentBedType; // 床型
    private final int BED_BIG = 1;
    private final int BED_DOUBLE = 2;

    private int mCurrentRoomCount; // 房间总数

    private String mStartData; // 入住时间
    private String mEndData; // 离店时间

    private int mCurrentHotelPosition = -1; // 当前选择的酒店
    private int mTempHotelPosition = -1; // 临时选择的酒店


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.bg_title));
        setContentView(R.layout.activity_v_experience_booking);

        initView();
        initData();

        requestVExperienceInfo("11596");
    }

    private void initView() {
        nest_scroll_v_experience = findViewById(R.id.nest_scroll_v_experience);
        tvHotelName = findViewById(R.id.tv_v_experience_booking_hotel_name);
        tvYear = findViewById(R.id.tv_v_experience_booking_year);
        slidingTab = findViewById(R.id.sliding_tab_v_experience_booking);
        viewPagerCalendar = findViewById(R.id.vp_v_experience_booking_calendar);
        tvBigBed = findViewById(R.id.tv_v_experience_booking_big_bed);
        tvDoubleBed = findViewById(R.id.tv_v_experience_booking_double_bed);
        ivRoomMinus = findViewById(R.id.iv_v_experience_booking_check_people_minus);
        ivRoomPlus = findViewById(R.id.iv_v_experience_booking_check_people_plus);
        tvRoomCount = findViewById(R.id.tv_v_experience_booking_check_people_rooms);
        etName = findViewById(R.id.et_v_experience_booking_name);
        etPhone = findViewById(R.id.et_v_experience_booking_phone);
        llMoreNeed = findViewById(R.id.ll_v_experience_booking_more_need);
        tvMoreNeed = findViewById(R.id.tv_v_experience_booking_more_need);
        tvCheckDescribe = findViewById(R.id.tv_v_experience_booking_check_describe);
        recyclerViewCheck = findViewById(R.id.recycler_v_experience_booking_check);
        tvCommit = findViewById(R.id.tv_v_experience_booking_commit);
    }

    private void initData() {
        tvHotelName.setOnClickListener(this);
        tvBigBed.setOnClickListener(this);
        tvDoubleBed.setOnClickListener(this);
        ivRoomMinus.setOnClickListener(this);
        ivRoomPlus.setOnClickListener(this);
        tvCommit.setOnClickListener(this);

        mCurrentBedType = BED_BIG; // 默认大床
        mCurrentRoomCount = 1; // 默认选择了1间房
        mCalendarData = new VExperienceCalendarData();
        mCalendarData.setRoomCount(mCurrentRoomCount);

        requestVExperienceInfo("111");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_v_experience_booking_hotel_name: // 选择酒店
                popHotel();
                break;
            case R.id.tv_v_experience_booking_big_bed: // 大床
                choiceBed(BED_BIG);
                break;
            case R.id.tv_v_experience_booking_double_bed: // 双床
                choiceBed(BED_DOUBLE);
                break;
            case R.id.iv_v_experience_booking_check_people_minus: // -
                changeRoomCount(mCurrentRoomCount - 1);
                break;
            case R.id.iv_v_experience_booking_check_people_plus: // +
                changeRoomCount(mCurrentRoomCount + 1);
                break;
            case R.id.tv_v_experience_booking_commit: // 提交
                boolean checkCommit = checkCommit();
                if (checkCommit) {  // 通过了检测，可以提交

                }
                break;
        }
    }

    private CommonPopupWindow mPopupHotel;

    // 展示酒店集合
    private void popHotel() {
        if (mVExperienceInfo == null)
            return;
        final List<BackVExperienceInfoLevel1> reserveInfos = mVExperienceInfo.getReserveInfos();
        if (RxDataTool.isEmpty(reserveInfos))
            return;

        mTempHotelPosition = mCurrentHotelPosition;
        mPopupHotel = new CommonPopupWindow.Builder(this)
                .setView(R.layout.pop_v_experience_hotel)
                .setAnimationStyle(R.style.PopBottom)
                .setBackGroundLevel(0.7f)
                .setOutsideTouchable(true)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        TextView tvCancel = view.findViewById(R.id.tv_pop_v_experience_cancel);
                        TextView tvConfirm = view.findViewById(R.id.tv_pop_v_experience_confirm);
                        RecyclerView recyclerView = view.findViewById(R.id.recycler_pop_v_experience);

                        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(new AdapterRecycler<BackVExperienceInfoLevel1>(R.layout.item_v_experience_hotel,
                                reserveInfos) {

                            @Override
                            public void convert(ViewHolderRecycler holder, BackVExperienceInfoLevel1 level1, final int position) {
                                TextView tvName = holder.getView(R.id.tv_item_v_experience_hotel_name);
                                View view1 = holder.getView(R.id.v_item_v_experience_hotel_1);
                                View view2 = holder.getView(R.id.v_item_v_experience_hotel_2);
                                tvName.setText(String.format("%s", level1.getSellerName()));
                                if (mTempHotelPosition == position) {
                                    tvName.setTextColor(Color.parseColor("#D2D2D3"));
                                    view1.setVisibility(View.VISIBLE);
                                    view2.setVisibility(View.VISIBLE);
                                } else {
                                    tvName.setTextColor(Color.parseColor("#6C6D6E"));
                                    view1.setVisibility(View.GONE);
                                    view2.setVisibility(View.GONE);
                                }
                                tvName.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mTempHotelPosition = position;
                                        notifyDataSetChanged();
                                    }
                                });
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mPopupHotel != null && mPopupHotel.isShowing())
                                    mPopupHotel.dismiss();
                                mPopupHotel = null;
                            }
                        });
                        tvConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mPopupHotel != null && mPopupHotel.isShowing())
                                    mPopupHotel.dismiss();
                                mPopupHotel = null;
                                changeHotel(mTempHotelPosition);
                            }
                        });
                    }
                })
                .create();

        mPopupHotel.showAtLocation(nest_scroll_v_experience, Gravity.BOTTOM, 0, 0);
    }

    // 检测是否填写完全
    private boolean checkCommit() {
        if (mStartData == null || mEndData == null) {
            ToastUtil.showToast(this, "请选择入离时间");
            return false;
        }
        if (mCurrentRoomCount <= 0) {
            ToastUtil.showToast(this, "至少需要预订一间房");
            return false;
        }
        if (RxDataTool.isEmpty(etName.getText().toString().trim())) {
            ToastUtil.showToast(this, "请填写入住人姓名");
            return false;
        }
        String phone = etPhone.getText().toString().trim();
        if (RxDataTool.isEmpty(phone) || phone.length() != 11) {
            ToastUtil.showToast(this, "请填正确填写入住人手机号码");
            return false;
        }
        // 检测核销码
        int dayCount = CalendarUtil.getDayCountBetweenBothCalendar(CalendarUtil.getCalendarData(mStartData),
                CalendarUtil.getCalendarData(mEndData));
        int checkCount = 0;
        List<BackVExperienceInfoLevel2> verificationCodes = mCalendarData.getVerificationCodes();
        if (verificationCodes != null)
            for (BackVExperienceInfoLevel2 level2 : verificationCodes) {
                if (level2.isCheck())
                    checkCount++;
            }
        if (checkCount != dayCount * mCurrentRoomCount) {
            ToastUtil.showToast(this, String.format("您已选择%s间房，入住%s天，共需选择%s个核销码",
                    mCurrentRoomCount, dayCount, mCurrentRoomCount * dayCount));
            return false;
        }
        return true;
    }

    // 选择床型
    private void choiceBed(int bedType) {
        if (mCurrentBedType == bedType)
            return;
        mCurrentBedType = bedType;
        if (mCurrentBedType == BED_BIG) {
            tvBigBed.setCompoundDrawablesWithIntrinsicBounds
                    (getResources().getDrawable(R.drawable.icon_bed_choice_v_experience_booking), null, null, null);
        } else {
            tvBigBed.setCompoundDrawablesWithIntrinsicBounds
                    (getResources().getDrawable(R.drawable.icon_bed_unchoice_v_experience_booking), null, null, null);
        }
        if (mCurrentBedType == BED_DOUBLE) {
            tvDoubleBed.setCompoundDrawablesWithIntrinsicBounds
                    (getResources().getDrawable(R.drawable.icon_bed_choice_v_experience_booking), null, null, null);
        } else {
            tvDoubleBed.setCompoundDrawablesWithIntrinsicBounds
                    (getResources().getDrawable(R.drawable.icon_bed_unchoice_v_experience_booking), null, null, null);
        }
    }

    // 刷新核销码的描述
    private void refreshCodeDescribe() {
        if (mStartData == null || mEndData == null) // 没有入离日期，无法判断
            return;
        int dayCount = CalendarUtil.getDayCountBetweenBothCalendar(CalendarUtil.getCalendarData(mStartData),
                CalendarUtil.getCalendarData(mEndData));
        tvCheckDescribe.setText(String.format(Html.fromHtml(getResources().getString(R.string.code_describe)).toString(),
                dayCount, dayCount * mCurrentRoomCount));
    }

    // 改变房间总数（预加载）
    private void changeRoomCount(int roomCount) {
        if (RxDataTool.isEmpty(mCalendarData.getVerificationCodes())) {
            ToastUtil.showToast(this, "请先选择酒店");
            return;
        }
        if (roomCount <= 0) {
            ToastUtil.showToast(this, "至少需要预订一间房");
            return;
        }
        // 做房间与日历的联动判断，不能超过核销码数量
        if (mStartData != null && mEndData != null) {
            int dayCount = CalendarUtil.getDayCountBetweenBothCalendar(CalendarUtil.getCalendarData(mStartData),
                    CalendarUtil.getCalendarData(mEndData));
            if (dayCount * roomCount > mCalendarData.getUsefulCodeSize()) {
                ToastUtil.showToast(this, "核销码不足，不能预定更多房间");
                return;
            }
        }
        mCurrentRoomCount = roomCount;
        mCalendarData.setRoomCount(mCurrentRoomCount);
        tvRoomCount.setText(String.format("%s", mCurrentRoomCount));
        refreshCodeDescribe();
    }

    // 改变选择的酒店
    private void changeHotel(int hotelPosition) {
        if (hotelPosition < 0)
            return;
        if (mVExperienceInfo == null)
            return;
        if (hotelPosition == mCurrentHotelPosition) // 就是当前选择的酒店
            return;
        List<BackVExperienceInfoLevel1> reserveInfos = mVExperienceInfo.getReserveInfos();
        if (reserveInfos.isEmpty()) // 酒店集合null
            return;
        BackVExperienceInfoLevel1 hotel = reserveInfos.get(hotelPosition);
        if (hotel == null) // 酒店null
            return;

        final String startDate = mVExperienceInfo.getStartDate();
        final String endDate = mVExperienceInfo.getEndDate();
        String sysDate = mVExperienceInfo.getSysDate();
        List<BackVExperienceInfoCalendar> rateDates = mVExperienceInfo.getRateDates();
        final List<BackVExperienceInfoLevel2> verificationCodes = hotel.getVerificationCodes();
        if (RxDataTool.isEmpty(startDate) || RxDataTool.isEmpty(endDate) || RxDataTool.isEmpty(sysDate)
                || RxDataTool.isEmpty(rateDates) || RxDataTool.isEmpty(verificationCodes)) {
            ToastUtil.showToast(this, "日历数据缺失");
            return;
        }

        // 构建不为null的日历数据
        mCalendarData.setStartDate(startDate);
        mCalendarData.setEndDate(endDate);
        mCalendarData.setSysDate(sysDate);
        mCalendarData.setRoomCount(mCurrentRoomCount);
        mCalendarData.setRateDates(rateDates);
        mCalendarData.setVerificationCodes(verificationCodes);
        int usefulCodeCount = 0;
        for (BackVExperienceInfoLevel2 level2 : verificationCodes) {
            level2.setCheck(false); // 初始化数据
            if ("0".equals(level2.getValid()))
                usefulCodeCount++;
        }
        mCalendarData.setUsefulCodeSize(usefulCodeCount);
        Map<String, Boolean> notBookingMap = new HashMap<>();
        mCalendarData.setNotBookingMap(notBookingMap);
        for (BackVExperienceInfoCalendar infoCalendar : rateDates) {
            if ("0".equals(infoCalendar.getValid())) { // 不可用
                notBookingMap.put(infoCalendar.getRateDate(), true);
            }
        }

        mCurrentHotelPosition = hotelPosition;
        tvHotelName.setText(String.format("%s", hotel.getSellerName()));
        // 初始化数据
        mStartData = null;
        mEndData = null;
        changeRoomCount(1);
        refreshCalendar(hotel);
        refreshCode(hotel.getVerificationCodes());
    }


    // 刷新核销码
    private void refreshCode(List<BackVExperienceInfoLevel2> list) {

        List<BackVExperienceInfoLevel2> dataList = new ArrayList<>();
        if (list != null) {
            for (BackVExperienceInfoLevel2 level2 : list) {
                if ("0".equals(level2.getValid()))
                    dataList.add(level2);
            }
        }

        GridLayoutManager manager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerViewCheck.setLayoutManager(manager);
        recyclerViewCheck.setAdapter(new AdapterRecycler<BackVExperienceInfoLevel2>(R.layout.item_v_experience_code, dataList) {
            @Override
            public void convert(ViewHolderRecycler holder, final BackVExperienceInfoLevel2 level2, int position) {
                TextView tvCode = holder.getView(R.id.tv_item_v_experience_code);
                tvCode.setText(String.format("%s", level2.getVerificationCode()));
                if (level2.isCheck()) {
                    tvCode.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_check_choice_v_experience_booking)
                            , null, null, null);
                } else {
                    tvCode.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_check_unchoice_v_experience_booking)
                            , null, null, null);
                }
                tvCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        level2.setCheck(!level2.isCheck());
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }


    // 刷新日历
    private void refreshCalendar(BackVExperienceInfoLevel1 level1) {

        if (mVExperienceInfo == null || level1 == null)
            return;

        // 构建日历delegate
        final CalendarDelegate delegate = new CalendarDelegate();
        delegate.setCalendarItemHeight(RxImageTool.dp2px(55));
        delegate.setCurrentDay(mCalendarData.getSysDate());
        delegate.setRange(mCalendarData.getSysDate(), mCalendarData.getEndDate());

        // 构建title数据
        final List<MonthData> monthList = getMonthList(mCalendarData.getStartDate(), mCalendarData.getEndDate());
        List<String> titleList = new ArrayList<>();
        for (MonthData monthData : monthList) {
            titleList.add(monthData.getMonth() + "月");
        }
        final String[] titles = titleList.toArray(new String[titleList.size()]);

        // view集合
        final VExperienceMonthView[] views = new VExperienceMonthView[monthList.size()];

        firstMeasure = false;
        viewPagerCalendar.setOffscreenPageLimit(monthList.size() - 1);
        viewPagerCalendar.setAdapter(new PagerAdapter() {
            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                if (views[position] == null) {
                    MonthData month = monthList.get(position);
                    VExperienceMonthView view = new VExperienceMonthView(VExperienceBooking.this);
                    view.setCurrentDate(month.year, month.month, delegate, mCalendarData);
                    views[position] = view;
                    view.setOnRangeSelectedListener(new VExperienceMonthView.OnRangeSelectedListener() {
                        @Override
                        public void onRangeSelected(CalendarData start, CalendarData end) {
                            mStartData = start.toString();
                            mEndData = end.toString();
                            refreshCodeDescribe();
                        }

                        @Override
                        public void onStartSelected(CalendarData start) {
                            mStartData = start.toString();
                            mEndData = null;
                        }
                    });
                }
                if (!firstMeasure && position == 0) {
                    tvYear.setText(String.format("出发日期：%s年", monthList.get(position).getYear()));
                    int itemHeight = views[position].getItemHeight();
                    int lineCount = views[position].getLineCount();
                    int height = RxImageTool.dp2px(0.48f) * (lineCount - 1) + itemHeight * lineCount;
                    LayoutParamsUtil.setHeightPx(viewPagerCalendar, height);
                    firstMeasure = true;
                }
                container.addView(views[position]);
                return views[position];
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });
        if (slidingTab.getTabCount() > 0) // 重复加载前跳到第一个月
            slidingTab.setCurrentTab(0);
        slidingTab.setViewPager(viewPagerCalendar, titles);
        viewPagerCalendar.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvYear.setText(String.format("出发日期：%s年", monthList.get(position).getYear()));
                VExperienceMonthView view = views[position];
                int itemHeight = view.getItemHeight();
                int lineCount = view.getLineCount();
                int height = RxImageTool.dp2px(0.48f) * (lineCount - 1) + itemHeight * lineCount;
                LayoutParamsUtil.setHeightPx(viewPagerCalendar, height);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private List<MonthData> getMonthList(String startData, String endData) {
        List<MonthData> list = new ArrayList<>();

        CalendarData startCalendarData = CalendarUtil.getCalendarData(startData);
        CalendarData endCalendarData = CalendarUtil.getCalendarData(endData);
        int startYear = startCalendarData.getYear();
        int endYear = endCalendarData.getYear();
        int startMonth = startCalendarData.getMonth();
        int endMonth = endCalendarData.getMonth();
        if (startYear == endYear) { // 同一年
            for (int i = startMonth; i <= endMonth; i++) {
                MonthData monthData = new MonthData();
                monthData.setYear(startYear);
                monthData.setMonth(i);
                list.add(monthData);
            }
        } else { // 非同一年
            for (int i = startYear; i <= endYear; i++) {
                // 确定月份的循环
                int startMonthIndex = 1;
                int endMonthIndex = 12;
                if (i == startYear)
                    startMonthIndex = startMonth;
                if (i == endYear)
                    endMonthIndex = endMonth;
                for (int j = startMonthIndex; j <= endMonthIndex; j++) {
                    MonthData monthData = new MonthData();
                    monthData.setYear(i);
                    monthData.setMonth(j);
                    list.add(monthData);
                }
            }
        }
        return list;
    }

    private class MonthData {
        private int year;
        private int month;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

    }

    private void requestVExperienceInfo(final String memberId) {
        Flowable.fromArray(1)
                .map(new Function<Integer, BackVExperienceInfo>() {
                    @Override
                    public BackVExperienceInfo apply(Integer integer) throws Exception {
                        return RequestHelperVCard.getInstance().requestVExperienceInfo(memberId);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                    }
                })
                .subscribe(new Consumer<BackVExperienceInfo>() {
                    @Override
                    public void accept(BackVExperienceInfo backVExperienceInfo) throws Exception {
                        mVExperienceInfo = backVExperienceInfo;

                        // 造假数据
                        mVExperienceInfo.setReserveInfos(fackData());
                        List<BackVExperienceInfoCalendar> rateDates = mVExperienceInfo.getRateDates();
                        rateDates.get(15).setValid("0");
                        rateDates.get(16).setValid("0");
                        rateDates.get(17).setValid("0");
                        mVExperienceInfo.setEndDate("2019-02-08");

                        changeHotel(0);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showToast(getApplicationContext(), throwable.getMessage());
                    }
                });
    }

    private List<BackVExperienceInfoLevel1> fackData() {
        List<BackVExperienceInfoLevel1> list = new ArrayList<>();
        BackVExperienceInfoLevel1 level11 = new BackVExperienceInfoLevel1();
        list.add(level11);
        level11.setSellerId("1");
        level11.setSellerName("酒店1");
        List<BackVExperienceInfoLevel2> level2s = new ArrayList<>();
        level11.setVerificationCodes(level2s);

        BackVExperienceInfoLevel2 level2 = new BackVExperienceInfoLevel2();
        level2s.add(level2);
        level2.setValid("0");
        level2.setVerificationCode("0000");

        BackVExperienceInfoLevel2 level3 = new BackVExperienceInfoLevel2();
        level2s.add(level3);
        level3.setValid("0");
        level3.setVerificationCode("0110");

        BackVExperienceInfoLevel2 level4 = new BackVExperienceInfoLevel2();
        level2s.add(level4);
        level4.setValid("0");
        level4.setVerificationCode("12400");

        BackVExperienceInfoLevel2 level5 = new BackVExperienceInfoLevel2();
        level2s.add(level5);
        level5.setValid("0");
        level5.setVerificationCode("0621");

        BackVExperienceInfoLevel2 level6 = new BackVExperienceInfoLevel2();
        level2s.add(level6);
        level6.setValid("1");
        level6.setVerificationCode("1113");

        BackVExperienceInfoLevel1 level12 = new BackVExperienceInfoLevel1();
        list.add(level12);
        level12.setSellerId("2");
        level12.setSellerName("酒店2");
        List<BackVExperienceInfoLevel2> level2s2 = new ArrayList<>();
        level12.setVerificationCodes(level2s2);

        BackVExperienceInfoLevel2 level22 = new BackVExperienceInfoLevel2();
        level2s2.add(level22);
        level22.setValid("0");
        level22.setVerificationCode("8888");

        return list;
    }

}
