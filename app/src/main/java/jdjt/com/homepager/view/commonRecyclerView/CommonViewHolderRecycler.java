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

import com.vondear.rxtool.RxDataTool;
import com.vondear.rxtool.RxImageTool;

import jdjt.com.homepager.util.ViewUtil;

/**
 * Created by xxd on 2018/7/27.
 */

public class CommonViewHolderRecycler extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;

    public CommonViewHolderRecycler(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    public static CommonViewHolderRecycler get(Context context, ViewGroup parent, int layoutId, float height) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        if (height > 0) {
            ViewUtil.setHeight(itemView, height);
        }
        return new CommonViewHolderRecycler(context, itemView, parent);
    }


    public static CommonViewHolderRecycler get(Context context, ViewGroup parent, int layoutId) {
        return get(context, parent, layoutId, 0);
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

    public CommonViewHolderRecycler setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public CommonViewHolderRecycler setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public void setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
    }

    public CommonViewHolderRecycler setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }
}
