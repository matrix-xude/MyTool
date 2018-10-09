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
import jdjt.com.homepager.util.LayoutParamsUtil;

/**
 * Created by xxd on 2018/9/5.
 */

public class HotelSecondClassifyAdapter extends RecyclerView.Adapter<HotelSecondClassifyAdapter.ClassifyViewHolder> {

    private List<SimpleString> dataList;
    private int realShowNumber; // 实际显示的条目

    public HotelSecondClassifyAdapter(List<SimpleString> dataList,int maxShowNumber) {
        this.dataList = dataList;
        this.realShowNumber = (RxDataTool.isEmpty(dataList) ? 0 : dataList.size() < maxShowNumber ? dataList.size() : maxShowNumber) + 1;
    }

    @NonNull
    @Override
    public ClassifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_hotel_second_classify, null);
        return new ClassifyViewHolder(v);
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

        RelativeLayout rlClick;
        TextView tvName;
        ImageView ivIcon;

        public ClassifyViewHolder(View v) {
            super(v);
            rlClick = v.findViewById(R.id.rl_item_hotel_second_classify_click);
            tvName = v.findViewById(R.id.tv_item_hotel_second_classify);
            ivIcon = v.findViewById(R.id.iv_item_hotel_second_classify);
        }
    }
}
