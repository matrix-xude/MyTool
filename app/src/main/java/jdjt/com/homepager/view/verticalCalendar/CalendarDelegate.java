package jdjt.com.homepager.view.verticalCalendar;

import com.vondear.rxtool.RxImageTool;

import java.util.Date;

/**
 * Created by xxd on 2018/9/13.
 */

public class CalendarDelegate {

    /**
     * 日历卡的项高度
     */
    private int mCalendarItemHeight;

    /**
     * 周起始
     */
    private int mWeekStart; // 对应下面3个

    /**
     * 周起始：周日
     */
    static final int WEEK_START_WITH_SUN = 1;

    /**
     * 周起始：周一
     */
    static final int WEEK_START_WITH_MON = 2;

    /**
     * 周起始：周六
     */
    static final int WEEK_START_WITH_SAT = 7;

    /**
     * 选择模式
     */
    private int mSelectMode;  // 对应下面2个

    /**
     * 默认选择模式
     */
    static final int SELECT_MODE_DEFAULT = 0;

    /**
     * 单选模式
     */
    static final int SELECT_MODE_SINGLE = 1;

    /**
     * 最小年份和最大年份
     */
    private int mMinYear, mMaxYear;

    /**
     * 最小年份和最大年份对应最小月份和最大月份
     * when you want set 2015-07 to 2017-08
     */
    private int mMinYearMonth, mMaxYearMonth;

    /**
     * 最小年份和最大年份对应最小天和最大天数
     * when you want set like 2015-07-08 to 2017-08-30
     */
    private int mMinYearDay, mMaxYearDay;

    /**
     * 今天的日子
     */
    private CalendarData mCurrentDate;

    /**
     * 预先需要显示的起始数据
     */
    public CalendarData mPreStartDate, mPreEndDate;

    /**
     * 当前选择的的起始数据
     */
    public CalendarData mCurrentStartDate, mCurrentEndDate;

    public CalendarDelegate() {
        init();
    }

    private void init() {
        mWeekStart = WEEK_START_WITH_SUN;
        mSelectMode = SELECT_MODE_DEFAULT;
        mCalendarItemHeight = RxImageTool.dp2px(66); // 默认66dp的选项卡高度
        mCurrentDate = new CalendarData();
        Date d = new Date();
        mCurrentDate.setYear(CalendarUtil.getDate("yyyy", d));
        mCurrentDate.setMonth(CalendarUtil.getDate("MM", d));
        mCurrentDate.setDay(CalendarUtil.getDate("dd", d));
        mCurrentDate.setCurrentDay(true);

        int year = mCurrentDate.getYear();
        int month = mCurrentDate.getMonth();
        int day = mCurrentDate.getDay();
        int endYearDay = day;
        if (CalendarUtil.isLeapYear(year) && month == 2 && day == 29) // 默认闰年2月29下一个年为2月28
            endYearDay = day - 1;

        setRange(year, month, day, year + 1, month, endYearDay);
    }

    public void setRange(int minYear, int minYearMonth, int minYearDay,
                         int maxYear, int maxYearMonth, int maxYearDay) {
        this.mMinYear = minYear;
        this.mMinYearMonth = minYearMonth;
        this.mMinYearDay = minYearDay;
        this.mMaxYear = maxYear;
        this.mMaxYearMonth = maxYearMonth;
        this.mMaxYearDay = maxYearDay;
    }

    public int getWeekStart() {
        return mWeekStart;
    }

    public void setWeekStart(int weekStart) {
        this.mWeekStart = weekStart;
    }

    public int getSelectMode() {
        return mSelectMode;
    }

    public void setSelectMode(int selectMode) {
        this.mSelectMode = selectMode;
    }

    public int getCalendarItemHeight() {
        return mCalendarItemHeight;
    }

    public void setCalendarItemHeight(int calendarItemHeight) {
        this.mCalendarItemHeight = calendarItemHeight;
    }

    public int getMinYear() {
        return mMinYear;
    }

    public int getMaxYear() {
        return mMaxYear;
    }

    public int getMinYearMonth() {
        return mMinYearMonth;
    }

    public int getMaxYearMonth() {
        return mMaxYearMonth;
    }

    public int getMinYearDay() {
        return mMinYearDay;
    }

    public int getMaxYearDay() {
        return mMaxYearDay;
    }

    CalendarData getCurrentDay() {
        return mCurrentDate;
    }
}
