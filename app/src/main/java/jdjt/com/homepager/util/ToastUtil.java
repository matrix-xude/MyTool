package jdjt.com.homepager.util;

import android.content.Context;
import android.widget.Toast;

import jdjt.com.homepager.http.CodeException;
import jdjt.com.homepager.http.ERRException;


/**
 * Created by xxd on 2017/8/9.
 */

public class ToastUtil {

    /**
     * 之前显示的内容
     */
    private static String oldMsg;
    /**
     * Toast对象
     */
    private static Toast toast = null;
    /**
     * 第一次时间
     */
    private static long oneTime = 0;
    /**
     * 第二次时间
     */
    private static long twoTime = 0;

    /**
     * 显示Toast
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        if (message == null)
            return;

        if(context == null)
            return;

        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (message.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = message;
                toast.setText(message);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    public static void showToast(Context context, Throwable throwable) {
        if (throwable instanceof CodeException) {
            showToast(context, throwable.getMessage());
        } else if (throwable instanceof ERRException) {
            showToast(context, throwable.getMessage());
        } else {
            showToast(context, throwable.getMessage());
//            showToast(context, "获取数据失败，请检测当前网络状况");
        }
    }
}
