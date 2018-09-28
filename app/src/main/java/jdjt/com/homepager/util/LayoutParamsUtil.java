package jdjt.com.homepager.util;

import android.view.View;
import android.view.ViewGroup;

import com.vondear.rxtool.RxImageTool;

/**
 * Created by xxd on 2018/9/7.
 */

public class LayoutParamsUtil {

    public static void setHeight(View view, float dp) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = RxImageTool.dp2px(dp);
        view.setLayoutParams(layoutParams);
    }

    public static void setHeightPx(View view, int px) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = px;
        view.setLayoutParams(layoutParams);
    }

    public static void setHeightPx(View view, int itemHeight, int divideSpace, int count) {
        setHeightPx(view, itemHeight, divideSpace, count, 1);
    }

    public static void setHeightPx(View view, int itemHeight, int divideSpace, int count, int lineCount) {
        int row = (count + lineCount - 1) / lineCount;
        int divideCount = row - 1 > 0 ? row - 1 : 0;
        setHeightPx(view, itemHeight * row + divideSpace * divideCount);
    }

    /**
     * 设置view的margin属性
     * @param v
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
//            v.requestLayout();
            v.setLayoutParams(p);
        }
    }

}
