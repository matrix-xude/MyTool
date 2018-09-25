package jdjt.com.homepager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vondear.rxtool.RxDataTool;

import java.util.List;

import jdjt.com.homepager.R;
import jdjt.com.homepager.domain.HomeFirstModuleItemBean;
import jdjt.com.homepager.util.ViewUtil;

/**
 * Created by xxd on 2018/9/5.
 */

public class HomeModuleAdapter extends RecyclerView.Adapter<HomeModuleAdapter.HomeModuleViewHolder> {

    private List<HomeFirstModuleItemBean> dataList;
    private int itemHeight; // 每个条目的高度

    public HomeModuleAdapter(List<HomeFirstModuleItemBean> dataList, int itemHeight) {
        this.dataList = dataList;
        this.itemHeight = itemHeight;
    }

    @NonNull
    @Override
    public HomeModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_home_module, null);
        HomeModuleViewHolder holder = new HomeModuleViewHolder(v);
        // 替换每个条目的高度
        ViewUtil.setHeight(holder.tvName, itemHeight);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeModuleViewHolder holder, int position) {
        holder.tvName.setText(dataList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return RxDataTool.isEmpty(dataList) ? 0 : dataList.size();
    }

    class HomeModuleViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;

        public HomeModuleViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_item_common_text);
        }
    }
}
