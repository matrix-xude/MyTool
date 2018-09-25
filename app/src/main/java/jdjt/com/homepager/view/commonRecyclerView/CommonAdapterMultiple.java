package jdjt.com.homepager.view.commonRecyclerView;

import android.content.Context;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by xxd on 2018/7/27.
 * 多种条目的adapter通用类
 */

public abstract class CommonAdapterMultiple<T> extends CommonAdapterRecycler<T> {

    protected MultipleTypeSupport<T> multipleTypeSupport;

    public CommonAdapterMultiple(Context context, List<T> datas, MultipleTypeSupport<T> multipleTypeSupport) {
        super(context, -1, datas);
        this.multipleTypeSupport = multipleTypeSupport;
    }

    @Override
    public int getItemViewType(int position) {
        return multipleTypeSupport.getItemViewType(position, mData.get(position));
    }

    @Override
    public CommonViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = multipleTypeSupport.getLayoutId(viewType);
        CommonViewHolderRecycler holder = CommonViewHolderRecycler.get(mContext, parent, layoutId);
        return holder;
    }
}
