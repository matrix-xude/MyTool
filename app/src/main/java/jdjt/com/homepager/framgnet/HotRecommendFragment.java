package jdjt.com.homepager.framgnet;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import jdjt.com.homepager.R;
import jdjt.com.homepager.adapter.HomeHotRecommendAdapter;
import jdjt.com.homepager.decoration.HomeHotRecommendDecoration;
import jdjt.com.homepager.domain.SimpleString;

/**
 * Created by xxd on 2018/9/6.
 */

public class HotRecommendFragment extends BaseFragment {

    private int heightFirstRow;  // 第一行item高度
    private int heightOtherRow;  // 其他行item高度
    private int divideSpace; // 间隔

    private List<SimpleString> dataList;

    private RecyclerView recyclerView;
    private HomeHotRecommendAdapter adapter;

    public void setData(int heightFirstRow, int heightOtherRow, int divideSpace, List<SimpleString> dataList) {
        this.heightFirstRow = heightFirstRow;
        this.heightOtherRow = heightOtherRow;
        this.divideSpace = divideSpace;
        this.dataList = dataList;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_hot_recommend;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_fragment_hot_recommend);
    }

    private void initData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new HomeHotRecommendAdapter(heightFirstRow, heightOtherRow, dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HomeHotRecommendDecoration(3, 3, divideSpace, Color.WHITE));
    }


}
