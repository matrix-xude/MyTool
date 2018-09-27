package jdjt.com.homepager.http.requestHelper;

/**
 * Created by xxd on 2018/9/27.
 * 首页接口的帮助类
 */

public class HomePagerHelper {

    private static HomePagerHelper mInstance;

    private HomePagerHelper() {
    }

    public synchronized static HomePagerHelper getInstance() {
        if (mInstance == null) {
            synchronized (HomePagerHelper.class) {
                mInstance = new HomePagerHelper();
            }
        }
        return mInstance;
    }
}
