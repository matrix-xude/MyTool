package jdjt.com.homepager.util;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/4/004.
 */

public class DateUtil {

    // 格式化日期为字符串 "yyyy-MM-dd   hh:mm"
    public static String formatDateTime(Date basicDate, String strFormat) {
        SimpleDateFormat df = new SimpleDateFormat(strFormat);
        return df.format(basicDate);
    }


    /**
     * 根据传入日期 和 转换格式转换时间  例： 2014-06-06 | yyyy-MM-dd
     *
     * @param basicDate
     * @param strFormat
     * @return 2014-8-11下午9:49:47
     * @author wangmingyu
     */
    public static String formatStringDate(String basicDate, String strFormat) {
        Date date = DateUtil.convertStringToDate(basicDate);
        SimpleDateFormat df = new SimpleDateFormat(strFormat);
        return df.format(date);
    }


    /**
     * 根据传入日期 和 转换格式转换时间  例： 2014-06-06 20:03:08 | yyyy-MM-dd HH:mm:ss
     *
     * @param basicDateTime
     * @param
     * @return
     */
    public static String formatStringDateTime(String basicDateTime, String strFormat) {
        Date date = DateUtil.convertStringToDateTime(basicDateTime);
        SimpleDateFormat df = new SimpleDateFormat(strFormat);
        return df.format(date);
    }

    // 格式化日期为字符串 "yyyy-MM-dd   hh:mm"
    public static String formatDateTime(String basicDate, String strFormat) {
        SimpleDateFormat df = new SimpleDateFormat(strFormat);
        Date tmpDate = null;
        try {
            tmpDate = df.parse(basicDate);
        } catch (Exception e) {
            // 日期型字符串格式错误
        }
        return df.format(tmpDate);
    }

    // 当前日期加减n天后的日期，返回String (yyyy-mm-dd)
    public static String tomorrowDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.DAY_OF_MONTH, +1);
        return df.format(rightNow.getTime());
    }

    // 当前日期加减n天后的日期，返回String (yyyy-mm-dd)
    public static String nDaysAftertoday(int n) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar rightNow = Calendar.getInstance();
        // rightNow.add(Calendar.DAY_OF_MONTH,-1);
        rightNow.add(Calendar.DAY_OF_MONTH, +n);
        return df.format(rightNow.getTime());
    }

    // 当前日期加减n天后的日期，返回String (yyyy-mm-dd)
    public static Date nDaysAfterNowDate(int n) {
        Calendar rightNow = Calendar.getInstance();
        // rightNow.add(Calendar.DAY_OF_MONTH,-1);
        rightNow.add(Calendar.DAY_OF_MONTH, +n);
        return rightNow.getTime();
    }

    // 给定一个日期型字符串，返回加减n天后的日期型字符串
    public static String nDaysAfterOneDateString(String basicDate, int n) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date tmpDate = null;
        try {
            tmpDate = df.parse(basicDate);
        } catch (Exception e) {
            // 日期型字符串格式错误
        }
        long nDay = (tmpDate.getTime() / (24 * 60 * 60 * 1000) + 1 + n)
                * (24 * 60 * 60 * 1000);
        tmpDate.setTime(nDay);

        return df.format(tmpDate);
    }

    // 给定一个日期，返回加减n天后的日期
    public static Date nDaysAfterOneDate(Date basicDate, int n) {
        long nDay = (basicDate.getTime() / (24 * 60 * 60 * 1000) + 1 + n)
                * (24 * 60 * 60 * 1000);
        basicDate.setTime(nDay);

        return basicDate;
    }

    // 计算两个日期相隔的天数
    public static int nDaysBetweenTwoDate(Date firstDate, Date secondDate) {
        int nDay = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
        return nDay;
    }

    // 计算两个日期相隔的天数
    public static int nDaysBetweenTwoDate(String firstString,
                                          String secondString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = df.parse(firstString);
            secondDate = df.parse(secondString);
        } catch (Exception e) {
            // 日期型字符串格式错误
        }

        int nDay = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
        return nDay;
    }

    public static Date convertStringToDate(String date_s, String parten) {
        SimpleDateFormat df = new SimpleDateFormat(parten);
        try {
            return df.parse(date_s);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date convertStringToDate(String date_s) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return df.parse(date_s);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Date convertStringToDateTime(String date_s) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return df.parse(date_s);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* 将字符串转换为java.sql.Date类型 */
    public static Timestamp dateFormatsqlutil(String s) {
        java.text.DateFormat format1 = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date timeDate = null;
        try {
            timeDate = format1.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(timeDate);
        Timestamp ts = Timestamp.valueOf(time);
        return ts;
    }

    /* 将字符串转换为Date类型 */
    public static Timestamp dateFormatsqlutil(Date timeDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(timeDate);
        Timestamp ts = Timestamp.valueOf(time);
        return ts;
    }

    /**
     * 取得时间段内时间list 注：list不包含 outdate这一天
     *
     * @param indate  2012-06-12
     * @param outdate 2012-06-22
     * @return 2012-06-12 2012-06-13.. 集合
     */
    public static List<String> getDateStringList(String indate, String outdate) {
        List<String> list = new ArrayList<String>();
        // 取时间段的天数
        int length = nDaysBetweenTwoDate(indate, outdate);
        list.add(indate);
        for (int i = 0; i < length; i++) {
            String nextDate = DateUtil.nDaysAfterOneDateString(indate, 1);
            String _a = formatDateTime(convertStringToDate(nextDate),
                    "yyyy-MM-dd");
            String _b = formatDateTime(convertStringToDate(outdate),
                    "yyyy-MM-dd");
            if (!_a.equals(_b)) {
                list.add(nextDate);
                indate = nextDate;
            } else
                break;
        }
        return list;
    }

    /**
     * @param @param  now
     * @param @return 设定文件
     * @return boolean 如果在这之间 返回 true ，否则 返回false
     * @throws
     * @Title: nowDateCheck
     * @Description: TODO(判断输入的日期 是否在 4-1 ~4-15 之间 包括 1号 及 15号)
     * @author 王明雨
     * @date 2014-1-9 上午10:06:08
     */
    public static boolean nowDateCheck(Date now) {
        Date start = DateUtil.convertStringToDate("04-08", "MM-dd");
        Date end = DateUtil.convertStringToDate("04-15", "MM-dd");
        now = DateUtil.convertStringToDate(
                DateUtil.formatDateTime(now, "MM-dd"), "MM-dd");
        if (now.after(start) && now.before(end) || now.equals(start)
                || now.equals(end)) {
            return true;
        }
        return false;
    }

    /**
     * 验证判断 一个时间是否在 startDate 很 enddate 之间，
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param checkDate 验证时间
     * @return 2014-7-4下午1:55:15
     * @author wangmingyu
     */
    public static boolean checkedDateBetwen(String startDate, String endDate,
                                            String checkDate) {
        Date start = DateUtil.convertStringToDate(startDate, "yyyy-MM-dd");
        Date end = DateUtil.convertStringToDate(endDate, "yyyy-MM-dd");
        Date check = DateUtil.convertStringToDate(checkDate, "yyyy-MM-dd");
        if (check.after(start) && check.before(end) || check.equals(start)
                || check.equals(end)) {
            return true;
        }
        return false;
    }

    /**
     * @param @param  now
     * @param @param  end
     * @param @return 设定文件
     * @return boolean 大于60天 返回true ，否则返回false
     * @throws
     * @Title: isDateBetweenToDay
     * @Description: TODO(判断2个日期 是否大于60天)
     * @author 王明雨
     * @date 2014-1-15 下午01:41:22
     */
    public static boolean isDateBetweenToDay(Date now, Date end) {
        int days = nDaysBetweenTwoDate(now, end);
        if (days > 60) {
            return true;
        }
        return false;
    }

    public static Date formatDate(String d, String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr);
        Date nowd = null;
        try {
            nowd = df.parse(d);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return nowd;
    }

    public static Calendar formatStringDate(String date){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            calendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static Date formatDate(String d) throws ParseException {
        Calendar fromCalendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        fromCalendar.setTime(sdf.parse(d));

        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);
        return fromCalendar.getTime();
    }

    private static long getTimer(long t) {
        long time = t;
        return time < 0 ? time = 0 : (time = time);
    }

    private static String getTimerString(String t) {
        String str = "";
        return t.toString().length() < 2 ? str = "0" + t.toString() : (str = t);
    }

    /**
     * 传入参数单位 为秒
     *
     * @param time
     * @return
     */
    public static String timer(long time) {
        long h, m, s, hstr;
        String mstr, sstr, timestr;
        long etime = time; //总秒数s
        h = Long.valueOf(etime / 3600); //时
        m = Long.valueOf(etime / 60) % 60; //分
        s = Long.valueOf(etime % 60); //秒
        h = getTimer(h);
        m = getTimer(m);
        s = getTimer(s);


        timestr = getTimerString(h + "") + ":" + getTimerString(m + "") + ":" + getTimerString(s + "");

        //etime = etime - 1;
        return timestr;
    }

    public static String timerHM(long time) {
        long h, m, s, hstr;
        String mstr, sstr, timestr;
        long etime = time; //总秒数s
        h = Long.valueOf(etime / 3600); //时
        m = Long.valueOf(etime / 60) % 60; //分
        s = Long.valueOf(etime % 60); //秒
        h = getTimer(h);
        m = getTimer(m);
        s = getTimer(s);


        timestr = getTimerString(h + "") + ":" + getTimerString(m + "");

        //etime = etime - 1;
        return timestr;
    }

    public static long getStartAndEndToTimes(String start, String eend) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long between = 0;
        try {
            Date begin = dfs.parse(start);
            Date end = dfs.parse(eend);
            between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return between;
    }

    public static long getStartAndEndToTimes(Date start, Date end) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long between = 0;
        try {
            between = (end.getTime() - start.getTime());// 得到两者的毫秒数
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return between;
    }

    public static boolean checkStartAndEnd(String start, String eend) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        long between = 0;
        try {
            Date begin = dfs.parse("2009-07-10 10:22:21.214");
            Date end = dfs.parse("2009-07-20 11:24:49.145");
            between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between / (60 * 60 * 1000) - day * 24);
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                - min * 60 * 1000 - s * 1000);
        System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒" + ms
                + "毫秒");
        return false;
    }

    public static boolean checkStartAndEnd(Date start, Date end) {
        long between = 0;
        try {
            between = (end.getTime() - start.getTime());// 得到两者的毫秒数
        } catch (Exception ex) {
            return true;
        }
        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between / (60 * 60 * 1000) - day * 24);
        return hour > 1;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * @param strDate 1493887605000l
     * @return
     */
    public static String longToDateStr(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date(Long.valueOf(strDate)));
    }

    public static void main(String[] args) {
//        System.out.print(new Date(1493887605000l*1000));
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(1493887605000l));
        System.out.print(date);
    }


    public static Date strsToDate(String style, String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String dateToStr(String style, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        return formatter.format(date);
    }

    public static String clanderTodatetime(Calendar calendar, String style) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        return formatter.format(calendar.getTime());
    }

    public static String DateTotime(long date, String style) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        return formatter.format(date);
    }

    // 将字符串转为时间戳
    public static String getStringTime(String user_time) {
        String re_time = null;
        String str = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            str = String.valueOf(l);
            re_time = str.substring(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re_time;
    }

    /**
     * 获取2个日期总共占用的月份
     *
     * @param olderDate
     * @param newerDate
     * @return
     * @throws Exception
     */
    public static int getMonthCount(Date olderDate, Date newerDate) throws Exception {
        int years = 0;
        int months = 0;

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(olderDate);
        end.setTime(newerDate);

        if (olderDate.getTime() > newerDate.getTime()) {
            throw new Exception();
        } else {
            years = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
            if (years == 0) {
                months = end.get(Calendar.MONTH) - start.get(Calendar.MONTH) + 1;
            } else if (years == 1) {
                months = (12 - start.get(Calendar.MONTH)) + (end.get(Calendar.MONTH) + 1);
            } else {
                months = (12 - start.get(Calendar.MONTH)) + (end.get(Calendar.MONTH) + 1) + 12 * (years - 1);
            }

        }
        return months;
    }

    /**
     * 返回一个二维数组，单位分别是月和日，代表两个Date之差。 <br>
     * 本方法忽略小时和分钟。 <br>
     * <br>
     * 例： <br>
     * 1，2012年6月1日到2012年6月3日，返回值是[0,2] （2天） <br>
     * 2，2012年6月15日到2012年8月4日，返回值是[1,20] （1个月零20天） <br>
     * 3，2011年11月3日到2013年1月14日，返回值是[14,11] （14个月零11天）
     *
     * @param olderDate 较早的日期
     * @param newerDate 较晚的日期
     */

    public static int[] getDateDifferenceInMonthAndDay(Date olderDate, Date newerDate)
            throws Exception {
        int[] differenceInMonthAndDay = new int[2];
        int years = 0;
        int months = 0;
        int days = 0;

        Calendar older = Calendar.getInstance();
        Calendar newer = Calendar.getInstance();
        older.setTime(olderDate);
        newer.setTime(newerDate);

        if (olderDate.getTime() > newerDate.getTime()) {
            throw new Exception();
        } else {
            years = newer.get(Calendar.YEAR) - older.get(Calendar.YEAR);
            if (years >= 0) {

                months = newer.get(Calendar.MONTH) - older.get(Calendar.MONTH) + 12 * years;
                older.add(Calendar.MONTH, months);
                days = newer.get(Calendar.DATE) - older.get(Calendar.DATE);

                if (days < 0) {
                    months = months - 1;
                    older.add(Calendar.MONTH, -1);
                }

                days = daysBetween(newer.getTime(), older.getTime());
                differenceInMonthAndDay[0] = months;
                differenceInMonthAndDay[1] = days;
            }

        }
        return differenceInMonthAndDay;
    }

    /**
     * 返回一个二维数组，单位分别是月和日，代表两个Date之差。 <br>
     * 本方法忽略小时和分钟。 <br>
     * <br>
     * 例： <br>
     * 1，2012年6月1日到2012年6月3日，返回值是[0,2] （2天） <br>
     * 2，2012年6月15日到2012年8月4日，返回值是[1,20] （1个月零20天） <br>
     * 3，2011年11月3日到2013年1月14日，返回值是[14,11] （14个月零11天）
     *
     * @param olderDate 较早的日期
     * @param newerDate 较晚的日期
     */

    public static int[] getDateDifferenceInMonthAndDay(String olderDate, String newerDate)
            throws Exception {
        int[] differenceInMonthAndDay = new int[2];
        int years = 0;
        int months = 0;
        int days = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(sdf.parse(olderDate));

        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(sdf.parse(newerDate));
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        if (fromCalendar.getTime().getTime() > toCalendar.getTime().getTime()) {
            throw new Exception();
        } else {
            years = fromCalendar.get(Calendar.YEAR) - toCalendar.get(Calendar.YEAR);
            if (years >= 0) {

                months = fromCalendar.get(Calendar.MONTH) - toCalendar.get(Calendar.MONTH) + 12 * years;
                toCalendar.add(Calendar.MONTH, months);
                days = fromCalendar.get(Calendar.DATE) - toCalendar.get(Calendar.DATE);

                if (days < 0) {
                    months = months - 1;
                    toCalendar.add(Calendar.MONTH, -1);
                }

                days = daysBetween(fromCalendar.getTime(), toCalendar.getTime());
                differenceInMonthAndDay[0] = months;
                differenceInMonthAndDay[1] = days;
            }

        }
        return differenceInMonthAndDay;
    }

    /**
     * 取两个Date之间的天数差<br>
     * <br>例：newerDate是6月3日，olderDate是5月31日，则应返回3
     * <br>一个更极端的列子：newerDate是6月3日 00:01，olderDate是6月2日 23:59，则应返回1，说明相差一天，即便实际上只差2分钟
     * <br>此代码来自网上
     * <br>http://blog.csdn.net/rmartin/article/details/1452867
     *
     * @param newerDate
     * @param olderDate
     * @return
     */
    public static int daysBetween(Date newerDate, Date olderDate) {
        Calendar cNow = Calendar.getInstance();
        Calendar cReturnDate = Calendar.getInstance();
        cNow.setTime(newerDate);
        cReturnDate.setTime(olderDate);
        setTimeToMidnight(cNow);
        setTimeToMidnight(cReturnDate);
        long todayMs = cNow.getTimeInMillis();
        long returnMs = cReturnDate.getTimeInMillis();
        long intervalMs = todayMs - returnMs;
        return millisecondsToDays(intervalMs);
    }

    private static int millisecondsToDays(long intervalMs) {
        return (int) (intervalMs / (1000 * 86400));
    }

    private static void setTimeToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }
}
