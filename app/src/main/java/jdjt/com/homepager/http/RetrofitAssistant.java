package jdjt.com.homepager.http;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import jdjt.com.homepager.BuildConfig;
import jdjt.com.homepager.util.DateUtil;
import jdjt.com.homepager.util.FileUtil;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xxd on 2017/8/1.
 */

public class RetrofitAssistant {

    private static final String TAG = "RetrofitAssistant";

    private RetrofitAssistant() {

    }

    public static String SUCCEED_CODE = "200";


    /**
     * 获取支持Gson解析的serviece (新接口)
     * @return
     */
    public static ServiceGson getNewGsonService() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            File dir = Environment.getExternalStorageDirectory();
            String date = DateUtil.formatDateTime(new Date(), "yyyy-MM-dd");
            String time = DateUtil.formatDateTime(new Date(), "HH-mm-ss");
            final String path = dir.getAbsolutePath() + File.separator + "Debug_MangroveTree" + File.separator
                    + date + File.separator + time + ".txt";

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    try {
                        Log.d(TAG,"请求日志:->>"+message);
                        // 加上换行，看的清晰
                        FileUtil.writeFile(path, message + "\r\n", "utf-8", true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(httpLoggingInterceptor);
        }
        // 5秒超时链接，读写
        builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        ServiceGson service = retrofit.create(ServiceGson.class);
        return service;
    }

}
