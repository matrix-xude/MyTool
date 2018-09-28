package jdjt.com.homepager.http;

import jdjt.com.homepager.domain.back.BackBaseList;
import jdjt.com.homepager.domain.back.BackHeadImage;
import jdjt.com.homepager.domain.back.BackHotActivity;
import jdjt.com.homepager.domain.back.BackHotRecommend;
import jdjt.com.homepager.domain.back.BackNavigation;
import jdjt.com.homepager.domain.back.BackVacation;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ServiceGson {

    /**
     * 首页热门推荐
     *
     * @return
     */
    @GET("api/api/v2/recommend/hotrecommend")
    Call<BackBaseList<BackHotRecommend>> getHomePagerHotRecommend();

    /**
     * 所有热门推荐，目的地页面使用
     *
     * @param body
     * @return
     */
    @POST("api/api/v2/recommend/lookMore")
    Call<BackBaseList<BackHotRecommend>> getHotRecommendAll(@Body RequestBody body);

    /**
     * 首页热门活动
     *
     * @param id
     * @return
     */
    @GET("api/api/v1/m/banner/hotList/{type_id}")
    Call<BackBaseList<BackHotActivity>> getHomePagerHotActivity(@Path("type_id") String id);

    /**
     * 首页最下面的度假套餐、酒店等
     *
     * @return
     */
    @GET("api/api/v1/index/recommends")
    Call<BackBaseList<BackVacation>> getHomePagerVacation();

    /**
     * 首页最上面的导航模块
     *
     * @param id
     * @return
     */
    @GET("api/api/v1/index/navigation/{type_id}")
    Call<BackBaseList<BackNavigation>> getHomePagerNavigation(@Path("type_id") String id);

    /**
     * 所有顶部轮播图
     *
     * @param id
     * @return
     */
    @GET("api/api/v1/m/banner/list/{type_id}")
    Call<BackBaseList<BackHeadImage>> getHeadImage(@Path("type_id") String id);


}
