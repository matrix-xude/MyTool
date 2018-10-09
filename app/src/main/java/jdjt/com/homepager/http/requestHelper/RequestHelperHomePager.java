package jdjt.com.homepager.http.requestHelper;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdjt.com.homepager.domain.back.BackBaseList;
import jdjt.com.homepager.domain.back.BackHeadImage;
import jdjt.com.homepager.domain.back.BackHotActivity;
import jdjt.com.homepager.domain.back.BackHotRecommend;
import jdjt.com.homepager.domain.back.BackMVMNew;
import jdjt.com.homepager.domain.back.BackNavigation;
import jdjt.com.homepager.domain.back.BackVacation;
import jdjt.com.homepager.http.CodeException;
import jdjt.com.homepager.http.RetrofitAssistant;
import jdjt.com.homepager.http.RetrofitUtil;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by xxd on 2018/9/27.
 * 首页接口的帮助类
 */

public class RequestHelperHomePager {

    private static RequestHelperHomePager mInstance;

    private RequestHelperHomePager() {
    }

    public synchronized static RequestHelperHomePager getInstance() {
        if (mInstance == null) {
            synchronized (RequestHelperHomePager.class) {
                mInstance = new RequestHelperHomePager();
            }
        }
        return mInstance;
    }

    /**
     * 热门推荐   目的地、度假区、大家都爱去
     *
     * @return
     * @throws IOException
     * @throws CodeException
     */
    public List<BackHotRecommend> requestHotRecommend() throws IOException, CodeException {
        Call<BackBaseList<BackHotRecommend>> call = RetrofitAssistant.getNewGsonService().getHomePagerHotRecommend();
        Response<BackBaseList<BackHotRecommend>> response = call.execute();
        BackBaseList<BackHotRecommend> body = response.body();
        if (body == null)
            throw new CodeException("返回body为null");
        if (!RetrofitAssistant.SUCCEED_CODE.equals(body.getCode()))
            throw new CodeException(body.getMsg());
        return body.getData();
    }

    /**
     * 所有热门推荐   目的地、度假区、大家都爱去
     *
     * @param type 1：热门目的地 2：热门度假区 3：大家都爱去
     * @return
     * @throws IOException
     * @throws CodeException
     */
    public BackHotRecommend requestHotRecommendAll(String type) throws IOException, CodeException {
        Map<String, String> send = new HashMap<>();
        send.put("type", type);
        Gson gson = new Gson();
        String json = gson.toJson(send);

        Call<BackBaseList<BackHotRecommend>> call = RetrofitAssistant.getNewGsonService().getHotRecommendAll(RetrofitUtil.json2Body(json));
        Response<BackBaseList<BackHotRecommend>> response = call.execute();
        BackBaseList<BackHotRecommend> body = response.body();
        if (body == null || body.getData().size() == 0) // 多包了一层
            throw new CodeException("返回body为null");
        if (!RetrofitAssistant.SUCCEED_CODE.equals(body.getCode()))
            throw new CodeException(body.getMsg());
        return body.getData().get(0);
    }

    /**
     * 首页热门活动
     *
     * @param type 2:酒店首页;3:度假套餐首页;4:MVM首页;5旅游首页;6:猫超市首页;15:猫玩乐首页
     * @return
     * @throws IOException
     * @throws CodeException
     */
    public List<BackHotActivity> requestHotActivity(String type) throws IOException, CodeException {

        Call<BackBaseList<BackHotActivity>> call = RetrofitAssistant.getNewGsonService().getHomePagerHotActivity(type);
        Response<BackBaseList<BackHotActivity>> response = call.execute();
        BackBaseList<BackHotActivity> body = response.body();
        if (body == null)
            throw new CodeException("返回body为null");
        if (!RetrofitAssistant.SUCCEED_CODE.equals(body.getCode()))
            throw new CodeException(body.getMsg());
        return body.getData();
    }

    /**
     * 首页最下面的度假套餐、主题
     *
     * @return
     * @throws IOException
     * @throws CodeException
     */
    public List<BackVacation> requestVacation() throws IOException, CodeException {
        Call<BackBaseList<BackVacation>> call = RetrofitAssistant.getNewGsonService().getHomePagerVacation();
        Response<BackBaseList<BackVacation>> response = call.execute();
        BackBaseList<BackVacation> body = response.body();
        if (body == null)
            throw new CodeException("返回body为null");
        if (!RetrofitAssistant.SUCCEED_CODE.equals(body.getCode()))
            throw new CodeException(body.getMsg());
        return body.getData();
    }

    /**
     * 首页最上方的的导航  度假酒店、度假套餐、猫超市等
     * @param type 0：全部；1：度假酒店；2：度假套餐；3：猫超市；4：线路游；5:猫玩乐;6:现在生活mall
     * @return
     * @throws IOException
     * @throws CodeException
     */
    public List<BackNavigation> requestNavigation(String type) throws IOException, CodeException {

        Call<BackBaseList<BackNavigation>> call = RetrofitAssistant.getNewGsonService().getHomePagerNavigation(type);
        Response<BackBaseList<BackNavigation>> response = call.execute();
        BackBaseList<BackNavigation> body = response.body();
        if (body == null)
            throw new CodeException("返回body为null");
        if (!RetrofitAssistant.SUCCEED_CODE.equals(body.getCode()))
            throw new CodeException(body.getMsg());
        return body.getData();
    }

    /**
     * 所有首页面的顶部轮播图
     * @param type 类型id 2:酒店首页;3:度假套餐首页;4:MVM首页;5旅游首页;6:猫超市首页
     * @return
     * @throws IOException
     * @throws CodeException
     */
    public List<BackHeadImage> requestHeadImage(String type) throws IOException, CodeException {

        Call<BackBaseList<BackHeadImage>> call = RetrofitAssistant.getNewGsonService().getHeadImage(type);
        Response<BackBaseList<BackHeadImage>> response = call.execute();
        BackBaseList<BackHeadImage> body = response.body();
        if (body == null)
            throw new CodeException("返回body为null");
        if (!RetrofitAssistant.SUCCEED_CODE.equals(body.getCode()))
            throw new CodeException(body.getMsg());
        return body.getData();
    }

    /**
     *  猫推荐头条，垂直轮播的
     * @param type 7:平台首页公告;8:猫超市公告;9:生活Mall公告;11:猫超市资讯;12:猫推荐;14:目的地;15:特色;16:线路
     *             默认 12
     * @return
     * @throws IOException
     * @throws CodeException
     */
    public List<BackMVMNew> requestMVMNew(String type) throws IOException, CodeException {

        Call<BackBaseList<BackMVMNew>> call = RetrofitAssistant.getNewGsonService().getMVMNew(type);
        Response<BackBaseList<BackMVMNew>> response = call.execute();
        BackBaseList<BackMVMNew> body = response.body();
        if (body == null)
            throw new CodeException("返回body为null");
        if (!RetrofitAssistant.SUCCEED_CODE.equals(body.getCode()))
            throw new CodeException(body.getMsg());
        return body.getData();
    }
}
