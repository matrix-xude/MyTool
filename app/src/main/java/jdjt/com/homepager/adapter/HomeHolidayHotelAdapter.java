package jdjt.com.homepager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.rxtool.RxDataTool;

import java.util.List;

import jdjt.com.homepager.R;
import jdjt.com.homepager.domain.SimpleString;
import jdjt.com.homepager.util.LayoutParamsUtil;

/**
 * Created by xxd on 2018/9/5.
 */

public class HomeHolidayHotelAdapter extends RecyclerView.Adapter<HomeHolidayHotelAdapter.MealViewHolder> {

    private List<SimpleString> dataList;
    private int itemHeight; // 每个条目的高度
    private int maxShowNumber; // 最大显示的条目

    public HomeHolidayHotelAdapter(List<SimpleString> dataList, int itemHeight, int maxShowNumber) {
        this.dataList = dataList;
        this.itemHeight = itemHeight;
        this.maxShowNumber = maxShowNumber;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_hotel, null);
        MealViewHolder holder = new MealViewHolder(v);
        // 替换每个条目的高度
        LayoutParamsUtil.setHeight(holder.rlItem, itemHeight);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        holder.tvName.setText(dataList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return RxDataTool.isEmpty(dataList) ? 0 : dataList.size() < maxShowNumber ? dataList.size() : maxShowNumber;
    }

    class MealViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlItem;
        TextView tvName;

        public MealViewHolder(View v) {
            super(v);
            rlItem = v.findViewById(R.id.rl_item_home_hotel);
            tvName = v.findViewById(R.id.tv_item_hotel_name);
        }
    }
}
