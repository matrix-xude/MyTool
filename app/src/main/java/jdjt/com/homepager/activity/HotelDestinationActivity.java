package jdjt.com.homepager.activity;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jdjt.com.homepager.R;
import jdjt.com.homepager.framgnet.HotelCityFragment;
import jdjt.com.homepager.framgnet.HotelHolidayFragment;
import jdjt.com.homepager.framgnet.HotelScenicFragment;

/**
 * Created by xxd on 2018/9/19.
 * 酒店城市、度假区、景点
 */

public class HotelDestinationActivity extends BaseActivity implements View.OnClickListener {

    // 城市、度假、景点(整个条目，文字，下划线)
    private RelativeLayout rlCity;
    private RelativeLayout rlHoliday;
    private RelativeLayout rlScenic;
    private TextView tvCity;
    private TextView tvHoliday;
    private TextView tvScenic;
    private View vCity;
    private View vHoliday;
    private View vScenic;

    private Fragment mCurrentFragment;
    private HotelCityFragment cityFragment;
    private HotelHolidayFragment holidayFragment;
    private HotelScenicFragment scenicFragment;

    private RelativeLayout mCurrentTab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_destination);
        initView();
        initData();
    }

    private void initView() {
        rlCity = findViewById(R.id.rl_hotel_destination_city);
        rlHoliday = findViewById(R.id.rl_hotel_destination_holiday);
        rlScenic = findViewById(R.id.rl_hotel_destination_scenic);
        tvCity = findViewById(R.id.tv_hotel_destination_city);
        tvHoliday = findViewById(R.id.tv_hotel_destination_holiday);
        tvScenic = findViewById(R.id.tv_hotel_destination_scenic);
        vCity = findViewById(R.id.v_hotel_destination_city);
        vHoliday = findViewById(R.id.v_hotel_destination_holiday);
        vScenic = findViewById(R.id.v_hotel_destination_scenic);
    }

    private void initData() {
        rlCity.setOnClickListener(this);
        rlHoliday.setOnClickListener(this);
        rlScenic.setOnClickListener(this);

        switchTab(rlCity);
        switchFragment(0);

    }

    // 切换fragment
    private void switchFragment(int position) {
        switch (position) {
            case 0:
                if (cityFragment == null) {
                    cityFragment = new HotelCityFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.rl_hotel_destination_content, cityFragment).commit();
                break;
            case 1:
                if (holidayFragment == null) {
                    holidayFragment = new HotelHolidayFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.rl_hotel_destination_content, holidayFragment).commit();
                break;
            case 2:
                if (scenicFragment == null) {
                    scenicFragment = new HotelScenicFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.rl_hotel_destination_content, scenicFragment).commit();
                break;
        }
    }


    // 切换tab的颜色
    private void switchTab(RelativeLayout rl) {

        if (mCurrentTab == rl) // 点击自己
            return;
        if (mCurrentTab != null) {
            TextView tvName = (TextView) mCurrentTab.getChildAt(0);
            View vUnderline = mCurrentTab.getChildAt(1);
            tvName.setTextColor(Color.WHITE);
            vUnderline.setVisibility(View.GONE);
        }
        mCurrentTab = rl;
        TextView tvName = (TextView) mCurrentTab.getChildAt(0);
        View vUnderline = mCurrentTab.getChildAt(1);
        tvName.setTextColor(getResources().getColor(R.color.bg_light));
        vUnderline.setVisibility(View.VISIBLE);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_hotel_destination_city:
                switchTab((RelativeLayout) v);
                switchFragment(0);
                break;
            case R.id.rl_hotel_destination_holiday:
                switchTab((RelativeLayout) v);
                switchFragment(1);
                break;
            case R.id.rl_hotel_destination_scenic:
                switchTab((RelativeLayout) v);
                switchFragment(2);
                break;
        }
    }
}
