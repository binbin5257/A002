package cn.lds.chatcore.common;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import cn.lds.chatcore.enums.DateTimeType;

/**
 * 时间类
 * <p/>
 * Created by quwei on 2016/1/18.
 */
public class DateHelper {

    /**
     * 毫秒数 转 时间
     *
     * @param lg           毫秒值
     * @param dateTimeType 时间格式
     * @return
     */
    public static String getDate(long lg, DateTimeType dateTimeType) {
        Date date = new Date(lg);
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(dateTimeType.value());
        String str = format.format(date);
        return str;
    }

    /**
     * 获取当前系统时间
     *
     * @param dateTimeType 时间格式
     * @return
     */
    public static String getSystemDate(DateTimeType dateTimeType) {
        return getDate(System.currentTimeMillis(), dateTimeType);
    }

    /**
     * 格式化聊天历史的显示时间
     */
    private static SimpleDateFormat sameDayFormat = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat sameYearFormat = new SimpleDateFormat("MM月dd日");
    private static SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String parseDate(long time) {
        if (time == 0)
            return "";

        final Calendar currentDate = new GregorianCalendar();
        final long currentTime = System.currentTimeMillis();
        currentDate.setTimeInMillis(currentTime);
        final Calendar targetDate = new GregorianCalendar();
        targetDate.setTimeInMillis(time);

        final boolean isSameDay =
                (currentDate.get(Calendar.DAY_OF_MONTH) - targetDate.get(Calendar.DAY_OF_MONTH)) == 0 ? true : false;

        final boolean isSameYear = (currentDate.get(Calendar.YEAR) - targetDate.get(Calendar.YEAR)) == 0 ? true : false;

        final Calendar yestodayDate = new GregorianCalendar();
        yestodayDate.setTimeInMillis(currentTime);
        yestodayDate.set(Calendar.DATE, currentDate.get(Calendar.DATE) - 1);

        final boolean isYestoday =
                (yestodayDate.get(Calendar.DAY_OF_MONTH) - targetDate.get(Calendar.DAY_OF_MONTH)) == 0 ? true : false;

        if (isSameDay) {
            return DateHelper.sameDayFormat.format(targetDate.getTime());
        }
        if (isYestoday) {
            return "昨天";
        }
        if (isSameYear) {
            return DateHelper.sameYearFormat.format(targetDate.getTime());
        } else {
            return DateHelper.fullDateFormat.format(targetDate.getTime());
        }

    }

}
