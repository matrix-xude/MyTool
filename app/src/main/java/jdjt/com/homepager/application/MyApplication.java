package jdjt.com.homepager.application;

import android.app.Application;

import com.vondear.rxtool.RxTool;

/**
 * Created by xxd on 2018/9/3.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RxTool.init(this);
    }
}
