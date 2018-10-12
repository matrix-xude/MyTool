package jdjt.com.homepager.http;

import jdjt.com.homepager.domain.back.BackBase;
import jdjt.com.homepager.domain.back.BackBaseList;
import jdjt.com.homepager.domain.back.BackHeadImage;
import jdjt.com.homepager.domain.back.BackHotActivity;
import jdjt.com.homepager.domain.back.BackHotRecommend;
import jdjt.com.homepager.domain.back.BackHotel;
import jdjt.com.homepager.domain.back.BackHotelType;
import jdjt.com.homepager.domain.back.BackMVMNew;
import jdjt.com.homepager.domain.back.BackNavigation;
import jdjt.com.homepager.domain.back.BackRecommendHotelType;
import jdjt.com.homepager.domain.back.BackSearchHotel;
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
     * 所有顶部轮播图（改版）
     *
     * @param id
     * @return
     */
    @GET("api/api/v1/m/banner/listDetail/{type_id}")
    Call<BackBaseList<BackHeadImage>> getHeadImage(@Path("type_id") String id);

    /**
     * 猫推荐新闻，垂直轮播
     *
     * @param id 12:猫推荐
     * @return
     */
    @GET("api/api/v1/news/{type_id}")
    Call<BackBaseList<BackMVMNew>> getMVMNew(@Path("type_id") String id);

    /**
     * 获取推荐的酒店类型
     *
     * @return
     */
    @POST("hotelbooking/reserve/search/getHotelType")
    Call<BackBaseList<BackRecommendHotelType>> getRecommendHotelType();

    /**
     * 猜你喜欢酒店列表
     *
     * @return
     */
    @POST("hotelbooking/reserve/search/getRecommendHotelList")
    Call<BackBaseList<BackHotel>> getRecommendHotel(@Body RequestBody body);

    /**
     * 获取所有酒店类型、排序
     *
     * @return
     */
    @POST("hotelbooking/reserve/serch/hotelChannelSerchIndustry")
    Call<BackBaseList<BackHotelType>> getHotelType();

    /**
     * 条件查询酒店列表
     * @return
     */
    @POST("hotelbooking/reserve/serch/hotelChannelSearch")
    Call<BackBase<BackSearchHotel>> searchHotel(@Body RequestBody body);

}
