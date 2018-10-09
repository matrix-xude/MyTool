package jdjt.com.homepager.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.TextView;

import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxImageTool;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import jdjt.com.homepager.R;
import jdjt.com.homepager.decoration.CommonDecoration;
import jdjt.com.homepager.util.StatusBarUtil;
import jdjt.com.homepager.view.ClearEditText;
import jdjt.com.homepager.view.commonRecyclerView.AdapterRecycler;
import jdjt.com.homepager.view.commonRecyclerView.ViewHolderRecycler;

/**
 * Created by xxd on 2018/9/18.
 * 酒店搜索页，可从酒店频道页、酒店列表页进入
 */

public class HotelSearchActivity extends BaseActivity implements View.OnClickListener {

    private ClearEditText et_hotel_search;
    private TextView tv_hotel_search_back;
    private ImageView iv_hotel_search_history_delete;
    private TagContainerLayout tagHistory;
    private TagContainerLayout tagHot;


    private RecyclerView recycler_pop_hotel_search;
    private AdapterRecycler<String> mAdapter;
    private List<String> dataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.bg_content));
        setContentView(R.layout.activity_hotel_search);
        initView();
        initData();
    }

    private void initView() {
        et_hotel_search = findViewById(R.id.et_hotel_search);
        recycler_pop_hotel_search = findViewById(R.id.recycler_pop_hotel_search);
        tv_hotel_search_back = findViewById(R.id.tv_hotel_search_back);
        iv_hotel_search_history_delete = findViewById(R.id.iv_hotel_search_history_delete);
        tagHistory = findViewById(R.id.tag_hotel_search_history);
        tagHot = findViewById(R.id.tag_hotel_search_hot);
    }

    private void initData() {
        final List<String> historyString = getHistoryString();

        iv_hotel_search_history_delete.setOnClickListener(this);
        tv_hotel_search_back.setOnClickListener(this);

        tagHistory.setTags(historyString);
        tagHistory.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                backToPreviousActivity(historyString.get(position));
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

        tagHot.setTags(historyString);
        tagHot.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                backToPreviousActivity(historyString.get(position));
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

        et_hotel_search.addTextChangedListener(new TextWatcher() {
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
                    recycler_pop_hotel_search.setVisibility(View.GONE);
                }
            }
        });

        // 监听键盘搜索按钮
        et_hotel_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String s = et_hotel_search.getText().toString();
                    if (RxDataTool.isEmpty(s))
                        return false;
                    backToPreviousActivity(s);
                    return true;
                }
                return false;
            }
        });
    }

    private void refreshList(String searchContent) {
        if (recycler_pop_hotel_search.getVisibility() == View.GONE)
            recycler_pop_hotel_search.setVisibility(View.VISIBLE);
        if (mAdapter == null) {
            mAdapter = new AdapterRecycler<String>(R.layout.item_search_relative, dataList) {
                @Override
                public void convert(ViewHolderRecycler holder, final String s, int position) {
                    holder.setText(R.id.tv_name, s);
                    holder.setOnClickListener(R.id.tv_name, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backToPreviousActivity(s);
                        }
                    });
                }
            };
            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recycler_pop_hotel_search.setLayoutManager(manager);
            recycler_pop_hotel_search.setAdapter(mAdapter);
            recycler_pop_hotel_search.addItemDecoration(new CommonDecoration(RxImageTool.dp2px(1), 1, Color.parseColor("#393A3C")));
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    // 将数据带回上一个页面
    private void backToPreviousActivity(String content) {
        Intent intent = new Intent();
        intent.putExtra("searchName", content);
        setResult(1, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_hotel_search_history_delete:
                tagHistory.removeAllTags();
                break;
            case R.id.tv_hotel_search_back:
                finish();
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
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    /*************************制造假数据********************************/
    private List<String> getHistoryString() {
        List<String> list = new ArrayList<>();
        list.add("水稻公园");
        list.add("青岛酒店");
        list.add("三亚湾");
        list.add("cnakcsjajagfsgs");
        list.add("吧啦吧啦巴拉巴拉啊吧阿里阿巴吧");
        list.add("入云龙");
        list.add("很好跟发");
        list.add("厚积薄发");
        return list;
    }
}
