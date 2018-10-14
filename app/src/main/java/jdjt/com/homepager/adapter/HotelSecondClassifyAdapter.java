package jdjt.com.homepager.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.rxtool.RxDataTool;

import java.util.List;

import jdjt.com.homepager.R;
import jdjt.com.homepager.activity.HotelListActivity;
import jdjt.com.homepager.domain.HotelType;
import jdjt.com.homepager.domain.back.BackRecommendHotelType;
import jdjt.com.homepager.util.GlideLoadUtil;

/**
 * Created by xxd on 2018/9/5.
 */

public class HotelSecondClassifyAdapter extends RecyclerView.Adapter<HotelSecondClassifyAdapter.ClassifyViewHolder> {

    private List<BackRecommendHotelType> dataList;
    private int realShowNumber; // 实际显示的条目
    private Activity activity;

    public HotelSecondClassifyAdapter(Activity activity, List<BackRecommendHotelType> dataList, int maxShowNumber) {
        this.activity = activity;
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

        if (position == realShowNumber - 1) {
            holder.tvName.setText("全部分类");
            holder.ivIcon.setImageResource(R.drawable.icon_hotel_type_all);
            holder.rlClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, HotelListActivity.class);
                    activity.startActivity(intent);
                }
            });
        } else {
            final BackRecommendHotelType recommendHotelType = dataList.get(position);
            holder.tvName.setText(recommendHotelType.getParamName() + "");
            GlideLoadUtil.loadImage(activity, recommendHotelType.getImageUrl(), holder.ivIcon);
            holder.rlClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HotelType hotelType = new HotelType();
                    hotelType.setId(recommendHotelType.getParamCode());
                    hotelType.setName(recommendHotelType.getParamName());
                    Intent intent = new Intent(activity, HotelListActivity.class);
                    intent.putExtra("hotel_type", hotelType);
                    activity.startActivity(intent);
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
