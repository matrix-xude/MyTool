package jdjt.com.homepager.view.commonListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xxd on 2018/7/27.
 */

public class CommonViewHolderList {

    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;

    //构造函数
    private CommonViewHolderList(Context context, int resLayoutId, ViewGroup parent) {
        this.mViews = new SparseArray<>();
        this.mContext = context;
        this.mConvertView = LayoutInflater.from(context).inflate(resLayoutId, parent, false);
        this.mConvertView.setTag(this);
    }

    //获取一个ViewHolder
    public static CommonViewHolderList getHolder(Context context, int resLayoutId, View convertView, ViewGroup parent) {
        if (convertView == null) {
            return new CommonViewHolderList(context, resLayoutId, parent);
        }
        return (CommonViewHolderList) convertView.getTag();
    }

    //通过控件的id获取对应的控件，如果没有则加入mViews;记住 <T extends View> T 这种用法
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    //获得一个convertView
    public View getConvertView() {
        return mConvertView;
    }

    public void setText(int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.setText(text);
    }

    public void setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
    }

    public void setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
    }

    public void setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        View view = getView(viewId);
        view.setOnClickListener(onClickListener);
    }


}
