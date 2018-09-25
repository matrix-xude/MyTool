package jdjt.com.homepager.view.commonListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.List;

import jdjt.com.homepager.R;

/**
 * Created by xxd on 2018/7/27.
 */


public abstract class CommonAdapterList<T> extends BaseAdapter {

    protected LayoutInflater mLayoutInflater;
    protected Context mContext;
    protected List<T> mData;
    protected int mItemLayoutId;

    public CommonAdapterList(Context mContext, List<T> mData, int mItemLayoutId) {
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mData = mData;
        this.mItemLayoutId = mItemLayoutId;
    }

    @Override
    public int getCount() {
        if (mData == null || mData.isEmpty()) // 需要一个默认的没有数据的条目
            return 1;
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mData == null || mData.isEmpty()) { // 默认实例，当前无数据条目
            return View.inflate(mContext, R.layout.item_common_text, null);
        }
        //实例化一个ViewHolder
        CommonViewHolderList holder = getViewHolder(position, convertView, parent);
        //使用对外公开的convert方法，通过ViewHolder把View找到，通过Item设置值
        convert(holder, getItem(position), position);
        return holder.getConvertView();
    }

    private CommonViewHolderList getViewHolder(int position, View convertView, ViewGroup parent) {
        return CommonViewHolderList.getHolder(mContext, mItemLayoutId, convertView, parent);
    }

    /**
     * 对外公布了一个convert方法，并且还把ViewHolder和本item对应的Bean对象给传出去
     * 现在convert方法里面需要干嘛呢？通过ViewHolder把View找到，通过Item设置值
     */
    public abstract void convert(CommonViewHolderList holder, T item, int position);


}
