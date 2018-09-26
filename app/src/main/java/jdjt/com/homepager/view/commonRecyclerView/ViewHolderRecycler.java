package jdjt.com.homepager.view.commonRecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xxd on 2018/7/27.
 */

public class ViewHolderRecycler extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;
    private View mConvertView;

    private ViewHolderRecycler(View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    public static ViewHolderRecycler get(Context context, ViewGroup parent, int layoutId, AdapterRecycler.Builder builder) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        if (builder != null) {
            if (builder.getItemWidth() > 0)
                RecyclerUtil.setWidthPx(itemView, builder.getItemWidth());
            if (builder.getItemHeight() > 0)
                RecyclerUtil.setHeightPx(itemView, builder.getItemHeight());
        }
        return new ViewHolderRecycler(itemView);
    }


    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public ViewHolderRecycler setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public ViewHolderRecycler setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public void setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
    }

    public ViewHolderRecycler setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }
}
