package cn.lds.chatcore.common;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeHelper {

    public static final String FORMAT1 = "yyyyMMdd_HHmmss";
    public static final String FORMAT2 = "HH:mm";
    public static final String FORMAT3 = "MM-dd HH:mm";
    public static final String FORMAT4 = "MM月dd日 HH:mm";
    public static final String FORMAT5 = "yyyy年MM月dd日 HH:mm";
    public static final String FORMAT6 = "yyyy-MM-dd HH:mm";
    public static final String FORMAT7 = "yyyy/MM/dd/HH:mm";
    public static final String FORMAT8 = "yyyy/MM/dd";
    public static final String FORMAT9 = "mm:ss";
    public static final String FORMAT10 = "HH:mm:ss";
    public static final String FORMAT11 = "yyyy-MM-dd";
    public static final String FORMAT12 = "yyyy/MM/dd HH:mm";

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentTimestamp(String pattern) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(new SimpleDateFormat(pattern).format(new Date()));
        return buffer.toString();
    }

    /**
     * 获得时间字符串
     *
     * @param millis
     * @return
     */
    public static String getDateStringString(long millis) {
        Date date = new Date(millis);
        String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE).format(date);
        return dateString;
    }
    /**
     * 获得时间字符串
     *
     * @param millis
     * @return
     */
    public static String getDateStringString(long millis, String format) {
        Date date = new Date(millis);
        String dateString = new SimpleDateFormat(format, Locale.CHINESE).format(date);
        return dateString;
    }

    public static String getTimeToShow(Date date) {
        if (date == null) {
            return null;
        }
        if (isToday(date)) {
            return new SimpleDateFormat(FORMAT2, Locale.CHINESE).format(date);
        } else {
            return new SimpleDateFormat(FORMAT3, Locale.CHINESE).format(date);
        }
    }

    /**
     * 格式化时间
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance(); // 今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        current.setTime(date);
        if (current.after(today)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getCurrentTime(String type) {
        SimpleDateFormat formatter = new SimpleDateFormat(type);
        Date curDate = new Date();
        return formatter.format(curDate);
    }

    /**
     * 判断时间是不是今天
     *
     * @param time
     * @return
     */
    public static boolean isToday(long time) {
        String now = getCurrentTime(FORMAT11);
        String date = getTimeFromMillis(FORMAT11, time);
        return now.equals(date);
    }

    /**
     * 判断是否是两分钟之内
     *
     * @param sendTime 消息的发送时间
     * @return
     */
    public static boolean coludRecall(long sendTime) {
        long currentTime = new Date().getTime();
        if (currentTime - sendTime <= 120000)   // 120000 = 2分钟
            return true;
        else
            return false;
    }

    /**
     * 计算时间与当前时间的差值
     *
     * @param time
     * @return
     */
    public static long countTime(long time) {
        long currentTime = System.currentTimeMillis();
        return (currentTime - time);
    }

    /**
     * 计算时间与当前时间的差值
     *
     * @param time
     * @return
     */
    public static long countTimeF(long time) {
        long currentTime = System.currentTimeMillis();
        return (time - currentTime);
    }

    public static String getTime(String type) {
        SimpleDateFormat formatter = new SimpleDateFormat(type);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 时间戳转换时间
     *
     * @param type
     * @param time
     * @return
     */
    public static String getTimeFromMillis(String type, long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(type);
        Date curDate = new Date(time);
        return formatter.format(curDate);
    }

    // 将字符串转为时间戳
    public static long getTime(String type, String time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        try {
            Date d = sdf.parse(time);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 将时间戳转变为分钟
    public static long getMinute(long time) {
        long min = 0;
        try {
            min = time / (1000 * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return min;
    }
}
