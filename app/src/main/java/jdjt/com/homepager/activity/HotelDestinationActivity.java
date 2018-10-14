package jdjt.com.homepager.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxImageTool;

import java.util.ArrayList;
import java.util.List;

import jdjt.com.homepager.R;
import jdjt.com.homepager.decoration.CommonDecoration;
import jdjt.com.homepager.framgnet.HotelCityFragment;
import jdjt.com.homepager.util.StatusBarUtil;
import jdjt.com.homepager.view.ClearNewEditText;
import jdjt.com.homepager.view.commonRecyclerView.AdapterRecycler;
import jdjt.com.homepager.view.commonRecyclerView.ViewHolderRecycler;

/**
 * Created by xxd on 2018/9/19.
 * 酒店城市、度假区、景点
 */

public class HotelDestinationActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_hotel_destination_back; // 返回
    private ClearNewEditText et_hotel_destination; // 搜索框
    private LinearLayout ll_hotel_destination_tab; // tab父类
    private TextView tv_hotel_destination_current_city; // 当前城市
    private TextView tv_hotel_destination_location; // 重新定位
    private RecyclerView recycler_destination_search; // recycler
    // 城市、度假、景点(整个条目，文字，下划线)
    private RelativeLayout rlCity;
    private RelativeLayout rlHoliday;
    private RelativeLayout rlScenic;

    private HotelCityFragment cityFragment;
    private HotelCityFragment holidayFragment;
    private HotelCityFragment scenicFragment;

    private int mCurrentTabIndex = -1; // 当前条目
    private AdapterRecycler<String> mAdapter;
    private List<String> dataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.bg_title));
        setContentView(R.layout.activity_hotel_destination);
        initView();
        initData();
    }

    private void initView() {
        iv_hotel_destination_back = findViewById(R.id.iv_hotel_destination_back);
        et_hotel_destination = findViewById(R.id.et_hotel_destination);
        ll_hotel_destination_tab = findViewById(R.id.ll_hotel_destination_tab);
        tv_hotel_destination_current_city = findViewById(R.id.tv_hotel_destination_current_city);
        tv_hotel_destination_location = findViewById(R.id.tv_hotel_destination_location);
        recycler_destination_search = findViewById(R.id.recycler_destination_search);
        rlCity = findViewById(R.id.rl_hotel_destination_city);
        rlHoliday = findViewById(R.id.rl_hotel_destination_holiday);
        rlScenic = findViewById(R.id.rl_hotel_destination_scenic);
    }

    private void initData() {
        iv_hotel_destination_back.setOnClickListener(this);
        rlCity.setOnClickListener(this);
        rlHoliday.setOnClickListener(this);
        rlScenic.setOnClickListener(this);

        switchTab(0);
        switchFragment(0);

        et_hotel_destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchContent = s.toString().trim();
                if (!RxDataTool.isEmpty(searchContent)) {
                    if (dataList == null)
                        dataList = new ArrayList<>();
                    dataList.clear();
                    for (int i = 0; i < searchContent.length(); i++) {
                        dataList.add("我是搜出来的条目" + (i + 1));
                    }
                    refreshList(searchContent);
                } else {
                    recycler_destination_search.setVisibility(View.GONE);
                }
            }
        });

        // 监听键盘搜索按钮
        et_hotel_destination.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String s = et_hotel_destination.getText().toString();
                    if (RxDataTool.isEmpty(s))
                        return false;
                    // TODO 搜索按钮被点击
                    return true;
                }
                return false;
            }
        });
    }

    private void refreshList(String searchContent) {
        if (recycler_destination_search.getVisibility() == View.GONE)
            recycler_destination_search.setVisibility(View.VISIBLE);
        if (mAdapter == null) {
            mAdapter = new AdapterRecycler<String>(R.layout.item_search_relative, dataList) {
                @Override
                public void convert(ViewHolderRecycler holder, final String s, int position) {
                    holder.setText(R.id.tv_name, s);
                    holder.setOnClickListener(R.id.tv_name, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            };
            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recycler_destination_search.setLayoutManager(manager);
            recycler_destination_search.setAdapter(mAdapter);
            recycler_destination_search.addItemDecoration(new CommonDecoration(RxImageTool.dp2px(1), 1, Color.parseColor("#393A3C")));
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    // 切换fragment
    private void switchFragment(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                if (cityFragment == null) {
                    cityFragment = new HotelCityFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "1");
                    cityFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.rl_hotel_destination_content, cityFragment).commit();
                }
                if (holidayFragment != null)
                    ft.hide(holidayFragment);
                if (scenicFragment != null)
                    ft.hide(scenicFragment);
                ft.show(cityFragment).commit();
                break;
            case 1:
                if (holidayFragment == null) {
                    holidayFragment = new HotelCityFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "2");
                    holidayFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.rl_hotel_destination_content, holidayFragment).commit();
                }
                if (cityFragment != null)
                    ft.hide(cityFragment);
                if (scenicFragment != null)
                    ft.hide(scenicFragment);
                ft.show(holidayFragment).commit();
                break;
            case 2:
                if (scenicFragment == null) {
                    scenicFragment = new HotelCityFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "3");
                    scenicFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.rl_hotel_destination_content, scenicFragment).commit();
                }
                if (cityFragment != null)
                    ft.hide(cityFragment);
                if (holidayFragment != null)
                    ft.hide(holidayFragment);
                ft.show(scenicFragment).commit();
                break;
        }
    }


    // 切换tab的颜色
    private void switchTab(int tabIndex) {
        if (mCurrentTabIndex == tabIndex) // 点击自己
            return;
        if (mCurrentTabIndex >= 0) {  //  切换上一个颜色
            RelativeLayout rlTab = (RelativeLayout) ll_hotel_destination_tab.getChildAt(mCurrentTabIndex);
            if (rlTab != null) {
                TextView tvName = (TextView) rlTab.getChildAt(0);
                View vUnderline = rlTab.getChildAt(1);
                tvName.setTextColor(Color.WHITE);
                vUnderline.setVisibility(View.GONE);
                tvName.setCompoundDrawablesWithIntrinsicBounds(getTabDrawable(mCurrentTabIndex, false), null, null, null);
            }
        }
        mCurrentTabIndex = tabIndex;
        if (mCurrentTabIndex >= 0) {  //  切换当前
            RelativeLayout rlTab = (RelativeLayout) ll_hotel_destination_tab.getChildAt(mCurrentTabIndex);
            if (rlTab != null) {
                TextView tvName = (TextView) rlTab.getChildAt(0);
                View vUnderline = rlTab.getChildAt(1);
                tvName.setTextColor(getResources().getColor(R.color.bg_light));
                vUnderline.setVisibility(View.VISIBLE);
                tvName.setCompoundDrawablesWithIntrinsicBounds(getTabDrawable(mCurrentTabIndex, true), null, null, null);
            }
        }
    }

    private Drawable getTabDrawable(int tabIndex, boolean light) {
        Drawable drawable = null;
        switch (tabIndex) {
            case 0:
                drawable = getResources().getDrawable(light ? R.drawable.icon_city_orange : R.drawable.icon_city_white);
                break;
            case 1:
                drawable = getResources().getDrawable(light ? R.drawable.icon_holiday_orange : R.drawable.icon_holiday_white);
                break;
            case 2:
                drawable = getResources().getDrawable(light ? R.drawable.icon_scenic_orange : R.drawable.icon_scenic_white);
                break;
        }
        return drawable;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_hotel_destination_city:
                switchTab(0);
                switchFragment(0);
                break;
            case R.id.rl_hotel_destination_holiday:
                switchTab(1);
                switchFragment(1);
                break;
            case R.id.rl_hotel_destination_scenic:
                switchTab(2);
                switchFragment(2);
                break;
            case R.id.iv_hotel_destination_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {  // 实现触摸非输入框任何区域收起键盘
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
