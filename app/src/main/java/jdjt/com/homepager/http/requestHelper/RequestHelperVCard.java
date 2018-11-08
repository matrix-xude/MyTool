package jdjt.com.homepager.http.requestHelper;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jdjt.com.homepager.domain.back.BackBase;
import jdjt.com.homepager.domain.back.BackVExperienceInfo;
import jdjt.com.homepager.http.CodeException;
import jdjt.com.homepager.http.RetrofitAssistant;
import jdjt.com.homepager.http.RetrofitUtil;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by xxd on 2018/9/27.
 * V客会的接口辅助类
 */

public class RequestHelperVCard {

    private static RequestHelperVCard mInstance;

    private RequestHelperVCard() {
    }

    public synchronized static RequestHelperVCard getInstance() {
        if (mInstance == null) {
            synchronized (RequestHelperVCard.class) {
                mInstance = new RequestHelperVCard();
            }
        }
        return mInstance;
    }

    /**
     * V客会体验卡预订信息
     *
     * @param memberId 会员id
     * @return
     * @throws IOException
     * @throws CodeException
     */
    public BackVExperienceInfo requestVExperienceInfo(String memberId) throws IOException, CodeException {
        Map<String, String> send = new HashMap<>();
        send.put("memberId", memberId);
        Gson gson = new Gson();
        String json = gson.toJson(send);

        Call<BackBase<BackVExperienceInfo>> call = RetrofitAssistant.getNewGsonService().getVExperiecInfo(RetrofitUtil.json2Body(json));
        Response<BackBase<BackVExperienceInfo>> response = call.execute();
        BackBase<BackVExperienceInfo> body = response.body();
        if (body == null)
            throw new CodeException("返回body为null");
        if (!RetrofitAssistant.SUCCEED_CODE.equals(body.getCode()))
            throw new CodeException(body.getMsg());
        return body.getData();
    }


}
