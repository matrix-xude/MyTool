package jdjt.com.homepager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vondear.rxtool.RxImageTool;

import java.util.List;

import jdjt.com.homepager.R;
import jdjt.com.homepager.domain.SimpleString;

/**
 * Created by xxd on 2018/9/6.
 */

public class HomeHotRecommendAdapter extends RecyclerView.Adapter<HomeHotRecommendAdapter.HotRecommendViewHolder> {


    private int heightFirstRow;  // 第一行item高度
    private int heightOtherRow;  // 其他行item高度

    private List<SimpleString> dataList;

    public HomeHotRecommendAdapter(int heightFirstRow, int heightOtherRow, List<SimpleString> dataList) {
        this.heightFirstRow = heightFirstRow;
        this.heightOtherRow = heightOtherRow;
        this.dataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 3)
            return 0;
        else
            return 1;
    }

    @NonNull
    @Override
    public HotRecommendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_home_hot_recommend, null);
        HotRecommendViewHolder holder = new HotRecommendViewHolder(v);
        // 替换每个条目的高度
        ViewGroup.LayoutParams layoutParams = holder.tvName.getLayoutParams();
        layoutParams.height = RxImageTool.dp2px(viewType == 0 ? heightFirstRow : heightOtherRow);
        holder.tvName.setLayoutParams(layoutParams);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HotRecommendViewHolder holder, int position) {
        holder.tvName.setText(dataList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    class HotRecommendViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;

        public HotRecommendViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_item_home_hot_recommend_name);
        }
    }
}
