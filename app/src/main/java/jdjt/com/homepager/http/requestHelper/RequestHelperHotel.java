package jdjt.com.homepager.http.requestHelper;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdjt.com.homepager.domain.HotelDestination;
import jdjt.com.homepager.domain.back.BackBase;
import jdjt.com.homepager.domain.back.BackBaseList;
import jdjt.com.homepager.domain.back.BackHeadImage;
import jdjt.com.homepager.domain.back.BackHotActivity;
import jdjt.com.homepager.domain.back.BackHotRecommend;
import jdjt.com.homepager.domain.back.BackHotel;
import jdjt.com.homepager.domain.back.BackHotelType;
import jdjt.com.homepager.domain.back.BackHotelTypeLevel;
import jdjt.com.homepager.domain.back.BackNavigation;
import jdjt.com.homepager.domain.back.BackRecommendHotelType;
import jdjt.com.homepager.domain.back.BackSearchHotel;
import jdjt.com.homepager.domain.back.BackVacation;
import jdjt.com.homepager.domain.send.SendHotelSearch;
import jdjt.com.homepager.domain.send.SendHotelSearchLevel1;
import jdjt.com.homepager.domain.send.SendHotelSearchLevel2;
import jdjt.com.homepager.domain.send.SendHotelSearchLevel3;
import jdjt.com.homepager.http.CodeException;
import jdjt.com.homepager.http.RetrofitAssistant;
import jdjt.com.homepager.http.RetrofitUtil;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by xxd on 2018/9/27.
 * 酒店页面的接口辅助类
 */

public class RequestHelperHotel {

    private static RequestHelperHotel mInstance;

    private RequestHelperHotel() {
    }

    public synchronized static RequestHelperHotel getInstance() {
        if (mInstance == null) {
            synchronized (RequestHelperHotel.class) {
                mInstance = new RequestHelperHotel();
            }
        }
        return mInstance;
    }

    /**
     * 请求推荐的酒店类型，7个，酒店频道页面
     *
     * @return
     * @throws IOException
     * @throws CodeException
     */
    public List<BackRecommendHotelType> requestRecommendHotelType() throws IOException, CodeException {
        Call<BackBaseList<BackRecommendHotelType>> call = RetrofitAssistant.getNewGsonService().getRecommendHotelType();
        Response<BackBaseList<BackRecommendHotelType>> response = call.execute();
        BackBaseList<BackRecommendHotelType> body = response.body();
        if (body == null)
            throw new CodeException("返回body为null");
        if (!RetrofitAssistant.SUCCEED_CODE.equals(body.getCode()))
            throw new CodeException(body.getMsg());
        return body.getData();
    }

    /**
     * 获取猜你喜欢酒店列表
     *
     * @param startDate   入住时间，可为null
     * @param endDate     离店时间，可为null
     * @param destination 目的地，可为null
     * @return
     * @throws IOException
     * @throws CodeException
     */
    public List<BackHotel> requestRecommendHotel(String startDate, String endDate, String destination) throws IOException, CodeException {
        Map<String, String> send = new HashMap<>();
        send.put("startDate", startDate);
        send.put("endDate", endDate);
        send.put("serchKey", destination);
        Gson gson = new Gson();
        String json = gson.toJson(send);

        Call<BackBaseList<BackHotel>> call = RetrofitAssistant.getNewGsonService().getRecommendHotel(RetrofitUtil.json2Body(json));
        Response<BackBaseList<BackHotel>> response = call.execute();
        BackBaseList<BackHotel> body = response.body();
        if (body == null)
            throw new CodeException("返回body为null");
        if (!RetrofitAssistant.SUCCEED_CODE.equals(body.getCode()))
            throw new CodeException(body.getMsg());
        return body.getData();
    }

    /**
     * 获取所有的酒店排序、类型
     *
     * @return
     * @throws IOException
     * @throws CodeException
     */
    public List<BackHotelType> requestHotelType() throws IOException, CodeException {
        Call<BackBaseList<BackHotelType>> call = RetrofitAssistant.getNewGsonService().getHotelType();
        Response<BackBaseList<BackHotelType>> response = call.execute();
        BackBaseList<BackHotelType> body = response.body();
        if (body == null)
            throw new CodeException("返回body为null");
        if (!RetrofitAssistant.SUCCEED_CODE.equals(body.getCode()))
            throw new CodeException(body.getMsg());
        return body.getData();
    }

    public BackSearchHotel searchHotel(String pageNo, String pageCount, String keyword, String startDate
            , String endDate, HotelDestination destination, BackHotelTypeLevel sort
            , List<BackHotelTypeLevel> typeList) throws IOException, CodeException {

        SendHotelSearch send = new SendHotelSearch();
        send.setPageNo(pageNo);
        send.setPageCount(pageCount);
        send.setSerchKey(keyword);
        send.setStartDate(startDate);
        send.setEndDate(endDate);
        List<SendHotelSearchLevel1> list = new ArrayList<>();
        send.setParamTypeList(list);
        if (destination != null) { // 目的地
            SendHotelSearchLevel1 level1 = new SendHotelSearchLevel1(); // 第一级数据
            list.add(level1);
            level1.setParamType("2");
            List<SendHotelSearchLevel2> list2 = new ArrayList<>();
            level1.setParamContent(list2);

            SendHotelSearchLevel2 level2 = new SendHotelSearchLevel2(); // 第二级数据
            list2.add(level2);
            level2.setTitle(destination.getTitle());
            List<SendHotelSearchLevel3> list3 = new ArrayList<>();
            level2.setContent(list3);

            SendHotelSearchLevel3 level3 = new SendHotelSearchLevel3(); // 第三级数据
            list3.add(level3);
            level3.setParamCode(destination.getId());
            level3.setParamName(destination.getName());
        }
        if (sort != null) {  // 排序
            SendHotelSearchLevel1 level1 = new SendHotelSearchLevel1(); // 第一级数据
            list.add(level1);
            level1.setParamType("4");
            List<SendHotelSearchLevel2> list2 = new ArrayList<>();
            level1.setParamContent(list2);

            SendHotelSearchLevel2 level2 = new SendHotelSearchLevel2(); // 第二级数据
            list2.add(level2);
            List<SendHotelSearchLevel3> list3 = new ArrayList<>();
            level2.setContent(list3);

            SendHotelSearchLevel3 level3 = new SendHotelSearchLevel3(); // 第三级数据
            list3.add(level3);
            level3.setParamCode(sort.getParamCode());
            level3.setParamName(sort.getParamName());
        }
        if (typeList != null && typeList.size() != 0) {  // 类型
            SendHotelSearchLevel1 level1 = new SendHotelSearchLevel1(); // 第一级数据
            list.add(level1);
            level1.setParamType("3");
            List<SendHotelSearchLevel2> list2 = new ArrayList<>();
            level1.setParamContent(list2);

            SendHotelSearchLevel2 level2 = new SendHotelSearchLevel2(); // 第二级数据
            list2.add(level2);
            level2.setTitle("酒店类型");
            List<SendHotelSearchLevel3> list3 = new ArrayList<>();
            level2.setContent(list3);

            SendHotelSearchLevel3 level3 = new SendHotelSearchLevel3(); // 第三级数据
            list3.add(level3);
            for (BackHotelTypeLevel level : typeList) {
                level3.setParamCode(level.getParamCode());
                level3.setParamName(level.getParamName());
            }
        }
        Gson gson = new Gson();
        String json = gson.toJson(send);

        Call<BackBase<BackSearchHotel>> call = RetrofitAssistant.getNewGsonService().searchHotel(RetrofitUtil.json2Body(json));
        Response<BackBase<BackSearchHotel>> response = call.execute();
        BackBase<BackSearchHotel> body = response.body();
        if (body == null)
            throw new CodeException("返回body为null");
        if (!RetrofitAssistant.SUCCEED_CODE.equals(body.getCode()))
            throw new CodeException(body.getMsg());
        return body.getData();
    }


}
