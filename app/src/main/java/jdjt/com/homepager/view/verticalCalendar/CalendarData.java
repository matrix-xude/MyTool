package jdjt.com.homepager.view.verticalCalendar;


import java.io.Serializable;

/**
 * Created by xxd on 2018/9/13.
 * 日历每个条目的保存的数据
 */

public class CalendarData implements Serializable {

    private static final long serialVersionUID = 141315161718191147L;

    /**
     * 年
     */
    private int year;

    /**
     * 月1-12
     */
    private int month;

    /**
     * 日1-31
     */
    private int day;

    /**
     * 是否是本月,这里对应的是月视图的本月，而非当前月份，请注意
     * 前后的空格部分都需要填充前，后一个月的数据
     * 不能在非月份视图中使用该数据
     */
    private boolean isCurrentMonth;

    /**
     * 是否是今天
     */
    private boolean isCurrentDay;

    /**
     * 是否是周末
     */
    private boolean isWeekend;

    /**
     * 星期,0-6 对应周日到周一
     */
    private int week;

    /**
     * 从左一开始，对应第几个条目，用于画pop计算
     */
    private int index;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        isCurrentMonth = currentMonth;
    }

    public boolean isWeekend() {
        return isWeekend;
    }

    public void setWeekend(boolean weekend) {
        isWeekend = weekend;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public boolean isCurrentDay() {
        return isCurrentDay;
    }

    public void setCurrentDay(boolean currentDay) {
        isCurrentDay = currentDay;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * 是否是相同月份
     *
     * @param calendarData
     * @return 是否是相同月份
     */
    public boolean isSameMonth(CalendarData calendarData) {
        return year == calendarData.getYear() && month == calendarData.getMonth();
    }

    /**
     * 比较日期
     *
     * @param calendar
     * @return -1 0 1
     */
    public int compareTo(CalendarData calendar) {
        return toString().compareTo(calendar.toString());
    }

    /**
     * 日期是否可用
     *
     * @return 日期是否可用
     */
    public boolean isAvailable() {
        return year > 0 & month > 0 & day > 0;
    }

    /**
     * 获取当前日历对应时间戳
     *
     * @return getTimeInMillis
     */
    public long getTimeInMillis() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.YEAR, year);
        calendar.set(java.util.Calendar.MONTH, month - 1);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, day);
        return calendar.getTimeInMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof CalendarData) {
            if (((CalendarData) o).getYear() == year && ((CalendarData) o).getMonth() == month && ((CalendarData) o).getDay() == day)
                return true;
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
    }
}
