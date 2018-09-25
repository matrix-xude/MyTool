package jdjt.com.homepager.view.commonRecyclerView;

/**
 * Created by xxd on 2018/7/27.
 * 多条目recyclerView需要传入的接口实现
 */

public interface MultipleTypeSupport<T> {
    int getLayoutId(int itemType);

    int getItemViewType(int position, T t);
}
