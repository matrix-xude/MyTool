package jdjt.com.homepager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.view.RxToast;

import java.util.List;

import jdjt.com.homepager.R;
import jdjt.com.homepager.domain.SimpleString;
import jdjt.com.homepager.util.ViewUtil;

/**
 * Created by xxd on 2018/9/5.
 */

public class HotelSecondClassifyAdapter extends RecyclerView.Adapter<HotelSecondClassifyAdapter.ClassifyViewHolder> {

    private List<SimpleString> dataList;
    private int itemHeight; // 每个条目的高度
    private int realShowNumber; // 实际显示的条目

    public HotelSecondClassifyAdapter(List<SimpleString> dataList, int itemHeight, int maxShowNumber) {
        this.dataList = dataList;
        this.itemHeight = itemHeight;
        this.realShowNumber = (RxDataTool.isEmpty(dataList) ? 0 : dataList.size() < maxShowNumber ? dataList.size() : maxShowNumber) + 1;
    }

    @NonNull
    @Override
    public ClassifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_hotel_second_classify, null);
        ClassifyViewHolder holder = new ClassifyViewHolder(v);
        // 替换每个条目的高度
        ViewUtil.setHeight(holder.rlItem, itemHeight);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassifyViewHolder holder, int position) {
        final SimpleString simpleString = dataList.get(position);

        holder.ivIcon.setImageResource(R.drawable.icon_hotel_type_all);
        if (position == realShowNumber - 1) {
            holder.tvName.setText("全部分类");
            holder.rlClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RxToast.normal("全部分类");
                }
            });
        } else {
            holder.tvName.setText(simpleString.getName());
            holder.rlClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RxToast.normal(simpleString.getName());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return realShowNumber;
    }

    class ClassifyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlItem;
        RelativeLayout rlClick;
        TextView tvName;
        ImageView ivIcon;

        public ClassifyViewHolder(View v) {
            super(v);
            rlItem = v.findViewById(R.id.rl_item_hotel_second_classify);
            rlClick = v.findViewById(R.id.rl_item_hotel_second_classify_click);
            tvName = v.findViewById(R.id.tv_item_hotel_second_classify);
            ivIcon = v.findViewById(R.id.iv_item_hotel_second_classify);
        }
    }
}
