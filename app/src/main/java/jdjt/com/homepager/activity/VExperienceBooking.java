package jdjt.com.homepager.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;

import jdjt.com.homepager.R;
import jdjt.com.homepager.util.StatusBarUtil;
import jdjt.com.homepager.util.ToastUtil;

/**
 * Created by xxd on 2018/10/31.
 * V客会体验卡预约入住
 */

public class VExperienceBooking extends BaseActivity implements View.OnClickListener {

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

    private int mCurrentBedType; // 床型
    private final int BED_BIG = 1;
    private final int BED_DOUBLE = 2;

    private int mCurrentRoomCount; // 房间总数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.bg_title));
        setContentView(R.layout.activity_v_experience_booking);

        initView();
        initData();
    }

    private void initView() {
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

        mCurrentBedType = BED_BIG; // 默认大床
        mCurrentRoomCount = 1; // 默认选择了1间房

        refreshCalendar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_v_experience_booking_hotel_name: // 选择酒店

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
        }
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

    // 改变房间总数（预加载）
    private void changeRoomCount(int roomCount) {
        if (roomCount <= 0) {
            ToastUtil.showToast(this, "至少需要预订一间房");
            return;
        }
        // TODO 做房间与日历的联动判断，不能超过核销码数量
        mCurrentRoomCount = roomCount;
        tvRoomCount.setText(String.format("%s", mCurrentRoomCount));
    }

    // 刷新日历
    private void refreshCalendar() {
        final String[] titles = new String[]{"8月", "9月", "10月", "11月", "12月", "1月"};
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
                View view = View.inflate(VExperienceBooking.this, R.layout.item_city, null);
                TextView tv_item_city = view.findViewById(R.id.tv_item_city);
                tv_item_city.setText(titles[position]);

                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });
        slidingTab.setViewPager(viewPagerCalendar, titles);
    }
}
