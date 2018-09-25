package jdjt.com.homepager.util;

import android.app.Instrumentation;
import android.view.KeyEvent;

/**
 * Created by xxd on 2018/9/18.
 */

public class BackUtil {

    public static void back() {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
