package jdjt.com.homepager.view.commonRecyclerView;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xxd on 2018/9/26.
 * RecyclerView专用Util,只能同包名下使用
 */

class RecyclerUtil {

    static void setHeightPx(View view, int px) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = px;
        view.setLayoutParams(layoutParams);
    }

    static void setWidthPx(View view, int px) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = px;
        view.setLayoutParams(layoutParams);
    }
}
