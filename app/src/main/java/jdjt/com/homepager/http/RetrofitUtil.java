package jdjt.com.homepager.http;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by xxd on 2017/8/9.
 */

public class RetrofitUtil {

    /**
     * 将json转为retrofit接口需要的body
     * @param json
     * @return
     */
    public static RequestBody json2Body(String json){
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return body;
    }

}
