package jdjt.com.homepager.view.commonRecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by xxd on 2018/7/27.
 */

public abstract class AdapterRecycler<T> extends RecyclerView.Adapter<ViewHolderRecycler> {

    private int layoutId;
    protected List<T> dataList;
    private Builder builder;

    public AdapterRecycler(int layoutId, List<T> dataList) {
        this(layoutId, dataList, null);
    }

    public AdapterRecycler(int layoutId, List<T> dataList, Builder builder) {
        this.layoutId = layoutId;
        this.dataList = dataList;
        this.builder = builder;
    }

    @NonNull
    @Override
    public ViewHolderRecycler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolderRecycler.get(parent.getContext(), parent, layoutId, builder);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderRecycler holder, int position) {
        convert(holder, dataList.get(position), position);
    }

    public abstract void convert(ViewHolderRecycler holder, T t, int position);

    @Override
    public int getItemCount() {
        int count = 0;
        if (dataList == null)
            return count;
        count = dataList.size();
        if (builder != null) {
            int maxShowCount = builder.getMaxShowCount();
            if (maxShowCount > 0) {
                count = maxShowCount < dataList.size() ? maxShowCount : dataList.size();
            }
        }
        return count;
    }

    /**
     * 一些其它设置，可以添加参数，用于特殊item
     */
    public static class Builder {
        private int itemHeight; // 固定holder的高度，大于0有效
        private int itemWidth;  // 固定holder的宽度，大于0有效
        private int maxShowCount; // 最大显示个数，大于0有效

        public int getItemHeight() {
            return itemHeight;
        }

        public Builder setItemHeight(int itemHeight) {
            this.itemHeight = itemHeight;
            return this;
        }

        public int getItemWidth() {
            return itemWidth;
        }

        public Builder setItemWidth(int itemWidth) {
            this.itemWidth = itemWidth;
            return this;
        }

        public int getMaxShowCount() {
            return maxShowCount;
        }

        public Builder setMaxShowCount(int maxShowCount) {
            this.maxShowCount = maxShowCount;
            return this;
        }
    }
}
