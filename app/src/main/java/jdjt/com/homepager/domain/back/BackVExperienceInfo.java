package jdjt.com.homepager.domain.back;

import java.util.List;

/**
 * Created by xxd on 2018/11/6.
 * V客会体验卡预约信息
 */

public class BackVExperienceInfo {

    private String startDate;
    private String endDate;
    private String sysDate; // 系统时间
    private List<BackVExperienceInfoCalendar> rateDates; // 预订日历
    private List<BackVExperienceInfoLevel1> reserveInfos; // 预订参数

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSysDate() {
        return sysDate;
    }

    public void setSysDate(String sysDate) {
        this.sysDate = sysDate;
    }

    public List<BackVExperienceInfoCalendar> getRateDates() {
        return rateDates;
    }

    public void setRateDates(List<BackVExperienceInfoCalendar> rateDates) {
        this.rateDates = rateDates;
    }

    public List<BackVExperienceInfoLevel1> getReserveInfos() {
        return reserveInfos;
    }

    public void setReserveInfos(List<BackVExperienceInfoLevel1> reserveInfos) {
        this.reserveInfos = reserveInfos;
    }
}
