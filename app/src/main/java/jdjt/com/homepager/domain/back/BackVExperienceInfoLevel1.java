package jdjt.com.homepager.domain.back;

import java.util.List;

/**
 * Created by xxd on 2018/11/6.
 * V客会体验卡预约信息酒店参数
 */

public class BackVExperienceInfoLevel1 {

    private String sellerId; // 酒店ID
    private String sellerName; // 酒店名称
    private List<BackVExperienceInfoLevel2> verificationCodes; // 核销码列表

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<BackVExperienceInfoLevel2> getVerificationCodes() {
        return verificationCodes;
    }

    public void setVerificationCodes(List<BackVExperienceInfoLevel2> verificationCodes) {
        this.verificationCodes = verificationCodes;
    }
}
