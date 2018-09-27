package jdjt.com.homepager.http;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by xxd on 2017/8/7.
 * <p>
 * 此类中的接口必须是按照文档定义的接口模式，在head中添加必要的参数
 * 因为返回需要根据head中的mymhotel-status字段接收不同的bean，所以需要用到RxJava的异常处理机制
 */

public interface ServiceGson {


    /**
     * 入住客人根据任务号获取聊天信息
     *
     * @param body
     * @return
     */
    @POST("ghost-service/customer/getMessageByTaskCode.json")
    Call<String> getChatMessageByCode(@Body RequestBody body);

}
