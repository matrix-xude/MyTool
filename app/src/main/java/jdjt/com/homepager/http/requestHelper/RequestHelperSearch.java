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
import jdjt.com.homepager.domain.back.BackCitySearch;
import jdjt.com.homepager.domain.back.BackHotel;
import jdjt.com.homepager.domain.back.BackHotelType;
import jdjt.com.homepager.domain.back.BackHotelTypeLevel;
import jdjt.com.homepager.domain.back.BackRecommendHotelType;
import jdjt.com.homepager.domain.back.BackSearchHotel;
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
 * 搜索接口辅助类
 */

public class RequestHelperSearch {

    private static RequestHelperSearch mInstance;

    private RequestHelperSearch() {
    }

    public synchronized static RequestHelperSearch getInstance() {
        if (mInstance == null) {
            synchronized (RequestHelperSearch.class) {
                mInstance = new RequestHelperSearch();
            }
        }
        return mInstance;
    }

    /**
     * 城市模糊搜索
     *
     * @param searchName
     * @return
     * @throws IOException
     * @throws CodeException
     */
    public List<BackCitySearch> searchCity(String searchName) throws IOException, CodeException {
        Map<String, String> send = new HashMap<>();
        send.put("regionName", searchName);
        Gson gson = new Gson();
        String json = gson.toJson(send);
        Call<BackBaseList<BackCitySearch>> call = RetrofitAssistant.getNewGsonService().searchCity(RetrofitUtil.json2Body(json));
        Response<BackBaseList<BackCitySearch>> response = call.execute();
        BackBaseList<BackCitySearch> body = response.body();
        if (body == null)
            throw new CodeException("返回body为null");
        if (!RetrofitAssistant.SUCCEED_CODE.equals(body.getCode()))
            throw new CodeException(body.getMsg());
        return body.getData();
    }

}
