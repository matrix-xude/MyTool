package jdjt.com.homepager.framgnet;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.rxtool.RxImageTool;

import jdjt.com.homepager.R;
import jdjt.com.homepager.decoration.CommonDecoration;
import jdjt.com.homepager.domain.HotelDestination;
import jdjt.com.homepager.domain.back.BackHotRecommend;
import jdjt.com.homepager.domain.back.BackHotRecommendLevel;
import jdjt.com.homepager.util.GlideLoadUtil;
import jdjt.com.homepager.util.LayoutParamsUtil;
import jdjt.com.homepager.util.ToastUtil;
import jdjt.com.homepager.view.commonRecyclerView.AdapterRecycler;
import jdjt.com.homepager.view.commonRecyclerView.ViewHolderRecycler;

/**
 * Created by xxd on 2018/9/6.
 */

public class HotRecommendFragment extends BaseFragment {

    private int heightFirstRow;  // 第一行item高度
    private int heightOtherRow;  // 其他行item高度
    private int divide; // 间隔
    private int lineCount; // 列数
    private int maxShowCount; // 最大显示的数量

    private BackHotRecommend hotRecommend;

    private RecyclerView recyclerView;

    public void setData(BackHotRecommend hotRecommend, int heightFirstRow, int heightOtherRow
            , int divide, int lineCount, int maxShowCount) {

        this.heightFirstRow = heightFirstRow;
        this.heightOtherRow = heightOtherRow;
        this.divide = divide;
        this.lineCount = lineCount;
        this.maxShowCount = maxShowCount;
        this.hotRecommend = hotRecommend;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_hot_recommend;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recycler_fragment_hot_recommend);
    }

    private void initData() {
        heightFirstRow = RxImageTool.dp2px(73);  // 第一行item高度
        heightOtherRow = RxImageTool.dp2px(38);  // 其他行item高度
        divide = RxImageTool.dp2px(8); // 间隔
        lineCount = 3;
        maxShowCount = lineCount * 3;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), lineCount, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new AdapterRecycler<BackHotRecommendLevel>(R.layout.item_home_hot_recommend, hotRecommend.getChildren(),
                new AdapterRecycler.Builder().setMaxShowCount(maxShowCount)) {
            @Override
            public void convert(ViewHolderRecycler holder, final BackHotRecommendLevel backHotRecommendLevel, int position) {
                // 替换每个条目的高度
                RelativeLayout rlItem = holder.getView(R.id.rl_item_home_hot_recommend);
                LayoutParamsUtil.setHeightPx(rlItem, position < lineCount ? heightFirstRow : heightOtherRow);
                BackHotRecommendLevel level = dataList.get(position);
                holder.setText(R.id.tv_item_home_hot_recommend_name, level.getName());
                if (position < lineCount) { // 第一行才有图背景
                    TextView tvName = holder.getView(R.id.tv_item_home_hot_recommend_name);
                    tvName.setBackgroundColor(Color.TRANSPARENT);
                    ImageView ivBg = holder.getView(R.id.iv_item_home_hot_recommend_bg);
                    GlideLoadUtil.loadImage(getContext(), level.getImage(), ivBg);
                }
                holder.setOnClickListener(R.id.tv_item_home_hot_recommend_name, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HotelDestination destination = new HotelDestination();
                        destination.setType(hotRecommend.getType());
                        destination.setTitle(hotRecommend.getName());
                        destination.setId(backHotRecommendLevel.getRefId());
                        destination.setName(backHotRecommendLevel.getName());
                        // TODO 跳转到全部搜索页面
                        ToastUtil.showToast(getActivity(),backHotRecommendLevel.getName());
                    }
                });
            }
        });
        recyclerView.addItemDecoration(new CommonDecoration(divide, lineCount, Color.TRANSPARENT));
    }


}
