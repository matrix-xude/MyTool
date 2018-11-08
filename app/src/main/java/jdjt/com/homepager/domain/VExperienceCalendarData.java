package jdjt.com.homepager.domain;

import java.util.List;
import java.util.Map;

import jdjt.com.homepager.domain.back.BackVExperienceInfoCalendar;
import jdjt.com.homepager.domain.back.BackVExperienceInfoLevel2;

/**
 * Created by xxd on 2018/11/7.
 * V可会体验卡日历数据，都不能为null才能构建数据
 */

public class VExperienceCalendarData {

    private String startDate;
    private String endDate;
    private String sysDate; // 系统时间
    private int roomCount; // 预订的房间数
    private List<BackVExperienceInfoCalendar> rateDates; // 预订日历
    private List<BackVExperienceInfoLevel2> verificationCodes; // 核销码列表
    private int usefulCodeSize; // 可用的核销码数量
    private Map<String, Boolean> notBookingMap; // 预定日历中，不能预定的日期

    public int getUsefulCodeSize() {
        return usefulCodeSize;
    }

    public void setUsefulCodeSize(int usefulCodeSize) {
        this.usefulCodeSize = usefulCodeSize;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

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

    public List<BackVExperienceInfoLevel2> getVerificationCodes() {
        return verificationCodes;
    }

    public void setVerificationCodes(List<BackVExperienceInfoLevel2> verificationCodes) {
        this.verificationCodes = verificationCodes;
    }

    public Map<String, Boolean> getNotBookingMap() {
        return notBookingMap;
    }

    public void setNotBookingMap(Map<String, Boolean> notBookingMap) {
        this.notBookingMap = notBookingMap;
    }
}
