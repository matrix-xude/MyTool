package jdjt.com.homepager.view.commonRecyclerView;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by xxd on 2018/7/27.
 * 多种条目的adapter通用类,因为每个条目不通用，所以不进行builder设置
 */

public abstract class AdapterMultipleRecycler<T> extends AdapterRecycler<T> {

    private MultipleTypeSupport<T> multipleTypeSupport;

    public AdapterMultipleRecycler(List<T> dataList, MultipleTypeSupport<T> multipleTypeSupport) {
        super(-1, dataList);
        this.multipleTypeSupport = multipleTypeSupport;
    }

    @Override
    public int getItemViewType(int position) {
        return multipleTypeSupport.getItemViewType(position, dataList.get(position));
    }

    @NonNull
    @Override
    public ViewHolderRecycler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = multipleTypeSupport.getLayoutId(viewType);
        return ViewHolderRecycler.get(parent.getContext(), parent, layoutId, null);
    }

}
