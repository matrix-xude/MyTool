package jdjt.com.homepager.view.commonRecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by xxd on 2018/7/27.
 */

public abstract class CommonAdapterRecycler<T> extends RecyclerView.Adapter<CommonViewHolderRecycler> {

    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mData;
    protected LayoutInflater mInflater;
    protected float mItemHeight;


    public CommonAdapterRecycler(Context context, int layoutId, List<T> datas) {
        this(context, layoutId, datas, 0);
    }

    public CommonAdapterRecycler(Context context, int layoutId, List<T> datas, float itemHeight) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mData = datas;
        mItemHeight = itemHeight;

    }

    @Override
    public CommonViewHolderRecycler onCreateViewHolder(final ViewGroup parent, int viewType) {
        CommonViewHolderRecycler viewHolder = CommonViewHolderRecycler.get(mContext, parent, mLayoutId, mItemHeight);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommonViewHolderRecycler holder, int position) {
        convert(holder, mData.get(position), position);
    }

    public abstract void convert(CommonViewHolderRecycler holder, T t, int position);

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
