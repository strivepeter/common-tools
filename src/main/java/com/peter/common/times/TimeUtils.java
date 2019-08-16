package com.peter.common.times;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName TimeUtils
 * @Description 关于时间相关的方法
 * @Author peter
 * @Date 2019/4/18 17:25
 * @Version 1.0
 */
public class TimeUtils {
    private static SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取当前的时间
     *
     * @return Date 时间
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获的当前时间 格式为（ yyyy-MM-dd ）
     *
     * @return String 字符串
     */
    public static String getCurrentTimeByDay() {
        String time = date.format(new Date(System.currentTimeMillis()));
        return time;
    }

    /**
     * 获的当前时间 格式为（yyyy-MM-dd HH:mm:ss）
     *
     * @return String 字符串
     */
    public static String getCurrentTimeBySecond() {
        String time = datetime.format(new Date(System.currentTimeMillis()));
        return time;
    }


    /**
     * 获得给定格式的当前时间字符串
     *
     * @param give 给定的时间格式
     * @return string的字符串
     */
    public static String getCurrentTime(String give) {
        SimpleDateFormat temp = new SimpleDateFormat(give);
        return temp.format(new Date(System.currentTimeMillis()));
    }


    /**
     * 将String转化成date
     *
     * @param str  时间字符串
     * @param sfgs 转化时间格式
     * @return
     * @throws ParseException
     */
    public static Date pStringToDate(String str, String sfgs)
            throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(sfgs);
        return sf.parse(str);
    }


    /**
     * 将String转化成date 格式为（yyyy-MM-dd hh:mm:ss）
     *
     * @param str 时间字符串
     * @return
     * @throws ParseException
     */
    public static Date pStringToDate(String str) throws ParseException {
        return datetime.parse(str);
    }


    /**
     * 转换成日期格式的字符串 格式为(yyyy-MM-dd)
     *
     * @param dateData
     * @return String的字符串
     */
    public static String dateFormat(Date dateData) {
        if (date == null) {
            return "";
        }
        return date.format(dateData);
    }


    /**
     * 转换成时间格式的字符串 格式为（yyyy-MM-dd hh:mm:ss）
     *
     * @param dateData
     * @return
     */
    public static String dateTimeFormat(Date dateData) {
        if (dateData == null) {
            return "";
        }
        return datetime.format(dateData);
    }


    /**
     * 转换成给定时间格式的字符串
     *
     * @param dateData 时间
     * @param format   时间格式
     * @return String 的字符串
     */
    public static String getDateFormat(Date dateData, String format) {
        return new SimpleDateFormat(format).format(dateData);
    }


    /**
     * 日期格式化(yyyy年MM月dd日)
     *
     * @param dateData 时间
     * @return String 字符串
     */
    public static String fDateCNYR(Date dateData) {
        return getDateFormat(dateData, "yyyy年MM月dd日");
    }


    /**
     * 日期格式化(yyyy年MM月dd日 HH:mm)
     *
     * @param dateData
     * @return String 字符串
     */
    public static String fDateCNYRS(Date dateData) {
        return getDateFormat(dateData, "yyyy年MM月dd日 HH点");
    }


    /**
     * 日期格式化(yyyy年MM月dd日 HH:mm)
     *
     * @param dateData 时间
     * @return String 字符串
     */
    public static String fDateCNYRSF(Date dateData) {
        return getDateFormat(dateData, "yyyy年MM月dd日 HH:mm");
    }


    /**
     * 日期格式化(yyyy年MM月dd日 HH:mm:ss)
     *
     * @param dateData 时间
     * @return String 字符串
     */
    public static String fDateCNYRSFM(Date dateData) {
        return getDateFormat(dateData, "yyyy年MM月dd日 HH:mm:ss");
    }


    /**
     * 根据给定的时间格式字符串截取给定格式的字符串
     *
     * @param dateData String 给定时间格式为yyyy-MM-dd HH:mm:ss
     * @param format   String 给定的格式
     * @return String 字符串
     * @throws ParseException
     */
    public static String getDateFormat(String dateData, String format) throws ParseException {
        Date date = datetime.parse(dateData);
        return getDateFormat(date, format);
    }


    /**
     * 通过字符串获得long型时间
     *
     * @param dateData 时间字符串
     * @return
     */
    public static long getDateFromStr(String dateData) {
        long temp = 0L;
        Date date;
        try {
            date = datetime.parse(dateData);
        } catch (Exception e) {
            e.printStackTrace();
            return temp;
        }
        temp = date.getTime();
        return temp;
    }


    /**
     * 日期格式化（2014-03-04）
     *
     * @param dateData
     * @return 时间格式Date
     * @throws ParseException
     */
    public static Date fDate(Date dateData) throws ParseException {
        String dateStr = date.format(dateData);
        return date.parse(dateStr);
    }


    /**
     * 通过开始时间和间隔获得结束时间
     *
     * @param start 开始的时间
     * @param span  隔得的数字
     * @return
     */
    public static String getEndTime(String start, int span) {
        if (isNullOrNone(start) || span == 0) {
            return null;
        }
        long temp = getDateFromStr(start);
        temp += span * 60L * 1000L;
        return datetime.format(new Date(temp));
    }


    /**
     * 格式化字符串，将2013-10-20 00:00:00.000000 简化为2013-10-20 00:00:00
     *
     * @param str 时间字符串
     * @return String字符串
     * @throws ParseException
     */
    public static String getFormatStringDay(String str) throws ParseException {
        Date date = datetime.parse(str);
        return datetime.format(date);
    }


    /**
     * 判断时间字符串是否为空
     *
     * @param src 时间自符串
     * @return Boolean 的值 true  获 false
     */
    public static boolean isNullOrNone(String src) {
        if (null == src || "".equals(src)) {
            return true;
        }
        return false;
    }


    /**
     * 如果字符串长度大于25则截取前25个字符串后续改成省略号
     *
     * @param strData String 字符串
     * @return String的自符串
     */
    public static String showCount(String strData) {
        if (strData != null) {
            if (strData.length() > 25) {
                strData = strData.substring(0, 25);
                strData = strData + "...";
            }
        } else {
            strData = "";
        }
        return strData;
    }


    /**
     * 是否符合日期格式yyyy-MM-dd
     *
     * @param day 时间的字符串
     * @return boolean 值 true 或 false
     */
    public static boolean isFormatDay(String day) {
        return day.matches("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
    }


    /**
     * 是否符合时间格式HH:mm:ss
     *
     * @param time 时间字符串
     * @return boolean 值 返回的true 或 false
     */
    public static boolean isFormatTime(String time) {
        return time.matches("(0[1-9]|1[0-9]|2[0-4]):(0[1-9]|[1-5][0-9]):(0[1-9]|[1-5][0-9])(\\.000000)?");
    }


    /**
     * 是否符合时间格式yyyy-MM-dd HH:mm:ss
     *
     * @param time 时间字符串
     * @return boolean 值 true 或 false
     */
    public static boolean isFormat(String time) {
        String[] temp = time.split(" ");
        return isFormatDay(temp[0]) && isFormatTime(temp[1]);
    }


    /**
     * 通过给定的年、月、周获得该周内的每一天日期
     *
     * @param year  给定的年
     * @param month 给定的月
     * @param week  给定的周
     * @return List<Date>的集合
     */
    public static List<Date> getDayByWeek(int year, int month, int week) {
        List<Date> list = new ArrayList<Date>();
        // 先滚动到该年.
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        // 滚动到月:
        c.set(Calendar.MONTH, month - 1);
        // 滚动到周:
        c.set(Calendar.WEEK_OF_MONTH, week);
        // 得到该周第一天:
        for (int i = 0; i < 6; i++) {
            c.set(Calendar.DAY_OF_WEEK, i + 2);
            list.add(c.getTime());
        }
        // 最后一天:
        c.set(Calendar.WEEK_OF_MONTH, week + 1);
        c.set(Calendar.DAY_OF_WEEK, 1);
        list.add(c.getTime());
        return list;
    }


    /**
     * 获得当前日期是本月的第几周
     *
     * @return int的数字
     */
    public static int getCurWeekNoOfMonth() {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }


    /**
     * 获得当前日期是星期几
     *
     * @param dat 给入的时间字符串
     * @return int 数字
     */
    public static int getCurWeekNo(String dat) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(dat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }


    /**
     * 获得当前的年份
     *
     * @return 返回int的数字
     */
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获得当前的月份
     *
     * @return 返回Int的数字
     */
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得当前的日期天
     *
     * @return 返回int的数字
     */
    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }


    /**
     * 获取当月最后一天
     *
     * @param date   时间
     * @param format 时间的格式
     * @return String的字符串
     */
    public static String lastDayOfMoth(Date date, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        date = cal.getTime();
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }


    /**
     * 获取当月最后一天
     *
     * @param date   时间
     * @param format 时间的格式
     * @return String的字符串
     */
    public static String firstDayOfMoth(Date date, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 0);
        date = cal.getTime();
        ;
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }


    /**
     * 获取当前日期前一天
     *
     * @param date 给的参数
     * @return 返回Date的数据
     */
    public static Date getSpecifiedDayBefore(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
        date = c.getTime();
        return date;
    }


    /**
     * 比较两个日期的大小
     *
     * @param firstDate  第一个时间参数
     * @param secondDate 第二个时间参数
     * @return boolean 值 true，false
     */
    public boolean compareDate(Date firstDate, Date secondDate) {
        if (firstDate.getTime() > secondDate.getTime()) {
            return true;
        }
        return false;
    }


    /**
     * 获取当前日期的后几日
     *
     * @return Date 日期
     */
    public static String getSpecifiedDayAfter(Date date, int number) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + number);
        date = c.getTime();
        return datetime.format(date);
    }


    /**
     * 比较两个日期的大小
     *
     * @param date 穿入的日期
     * @return boolean 值
     */
    public static boolean compareTwoDate(String date) {
        Date date1;
        try {
            date1 = pStringToDate(date);
            if (date1.getTime() < System.currentTimeMillis()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 比较三个日期的大小
     *
     * @param date 穿入的日期
     * @return boolean 值
     */
    public static boolean compareThreeDate(String date, String date2) {
        Date date1;
        Date date3;
        try {
            date1 = pStringToDate(date);
            date3 = pStringToDate(date2);
            if (date1.getTime() < System.currentTimeMillis()) {
                if (date3.getTime() > System.currentTimeMillis()) {
                    return true;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * ---------------测试---------------
     *
     * @param args
     */
    public static void main(String[] args) {
//
//        System.out.println(getCurrentTimeBySecond());
//        System.out.println(getSpecifiedDayAfter(new Date(),2));

        System.out.println(getCurrentTimeBySecond());
        System.out.println(compareTwoDate("2019-08-13 17:41:16"));

        System.out.println(compareThreeDate("2019-08-13 16:41:16", "2019-08-14 17:41:16"));

    }
}
