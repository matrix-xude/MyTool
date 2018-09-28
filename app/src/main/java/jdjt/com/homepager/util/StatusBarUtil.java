package jdjt.com.homepager.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


public class StatusBarUtil {

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 填充状态栏
     *
     * @param activity
     */
    public static void fitSystemWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  // 5.0之上
            setTitleColor21(activity);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTitleColor19(activity);
        }
    }

    @TargetApi(21)
    private static void setTitleColor21(Activity activity) {

        Window window = activity.getWindow();
        //如果为半透明模式，添加设置Window半透明的Flag
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //如果为全透明模式，取消设置Window半透明的Flag
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); // 半透
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //设置状态栏为透明
        window.setStatusBarColor(Color.TRANSPARENT);

        //设置系统状态栏处于可见状态，时间条目前面那个遮挡
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //设置window的状态栏不可见，时间条目前面那个遮挡
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        // 全屏模式设置，不预留位置
        ViewGroup mContentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 不预留出系统 View 的空间.
//            ViewCompat.setFitsSystemWindows(mChildView, true);
            mChildView.setFitsSystemWindows(true);
        }
    }

    private static void setTitleColor19(Activity activity) {
        Window window = activity.getWindow();

        // 设置状态栏透明
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // 全屏模式设置，不预留位置
        ViewGroup mContentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 不预留出系统 View 的空间.
//            ViewCompat.setFitsSystemWindows(mChildView, false);
            mChildView.setFitsSystemWindows(true);
        }

    }
}
