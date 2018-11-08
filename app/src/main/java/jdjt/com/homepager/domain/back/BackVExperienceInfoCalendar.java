package jdjt.com.homepager.domain.back;

/**
 * Created by xxd on 2018/11/6.
 * V客会体验卡预约信息日历信息
 */

public class BackVExperienceInfoCalendar {

    private String rateDate;
    private String valid; // 有效标识（0：不可用；1：可用）

    public String getRateDate() {
        return rateDate;
    }

    public void setRateDate(String rateDate) {
        this.rateDate = rateDate;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }
}
