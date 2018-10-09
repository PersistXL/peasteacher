package com.peas.common.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 日期工具
 *
 * @author dyh
 */
public class DateUtil
{
    private static final int MAX_TIME_SPLITTER = 7;

    private static final int YEAR = 0;

    private static final int MONTH = 1;

    private static final int DAY = 2;

    private static final int HOUR = 3;

    private static final int MINUTE = 4;

    private static final int SECOND = 5;

    private static final int MILLSECOND = 6;

    private static final int YYYY_MM_DD = 3;

    private static final int YYYY_MM_DD_HH_MM = 5;

    private static final int YYYY_MM_DD_HH_MM_SS = 6;

    private static final int YYYY_MM_DD_HH_MM_SS_MS = 7;

    private static final List<Integer> BIG_MONTH = Lists.newArrayList(1, 3, 5, 7, 8, 10, 12);

    private static final long ONE_DAY = 1000L * 60 * 60 * 24;

    private DateUtil()
    {
    }

    /**
     * 将时间转换为毫秒数字
     *
     * @param date
     * @return
     */
    public static long toTime(String date)
    {
        Preconditions.checkArgument(checkDate(date), "date fomatter is not right");
        List<String> ss;
        if (!date.matches("\\d{14}"))
        {
            ss = Splitter.onPattern("[^0-9]").trimResults().omitEmptyStrings().limit(MAX_TIME_SPLITTER).splitToList(date);
        }
        else
        {
            // 20171206142325
            ss = Lists.newArrayList();
            ss.add(date.substring(0, 4));//年
            ss.add(date.substring(4, 6));//月
            ss.add(date.substring(6, 8));//日
            ss.add(date.substring(8, 10));//时
            ss.add(date.substring(10, 12));//分
            ss.add(date.substring(12, 14));//秒
        }
        int size = ss.size();
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;
        int millisecond = 0;
        if (size >= YYYY_MM_DD)
        {
            year = Integer.valueOf(ss.get(YEAR));
            month = Integer.valueOf(ss.get(MONTH));
            day = Integer.valueOf(ss.get(DAY));
        }
        if (size >= YYYY_MM_DD_HH_MM)
        {
            hour = Integer.valueOf(ss.get(HOUR));
            minute = Integer.valueOf(ss.get(MINUTE));
        }
        if (size >= YYYY_MM_DD_HH_MM_SS)
        {
            second = Integer.valueOf(ss.get(SECOND));
        }
        if (size >= YYYY_MM_DD_HH_MM_SS_MS)
        {
            millisecond = Integer.valueOf(ss.get(MILLSECOND));
        }
        return new DateTime(year, month, day, hour, minute, second, millisecond).getMillis();
    }

    /**
     * 检查日期是否正确
     *
     * @param time 经过格式化的日期 如 yyyy*MM*dd*HH*mm*ss*ms 其中*匹配[^0-9]
     * @return
     */
    public static boolean checkDate(String time)
    {
        if (time.matches("\\d{14}"))
        {
            return true;
        }
        Preconditions.checkArgument(!Strings.isNullOrEmpty(time), "arguments can not be null");
        List<String> ss = Splitter.onPattern("[^0-9]").trimResults().omitEmptyStrings().limit(MAX_TIME_SPLITTER)
                .splitToList(time);
        boolean isOk = true;
        int size = ss.size();
        if (size >= YYYY_MM_DD)
        {
            int year = Integer.valueOf(ss.get(YEAR));
            int month = Integer.valueOf(ss.get(MONTH));
            int day = Integer.valueOf(ss.get(DAY));
            isOk = (year >= 1970 && year <= 2999) && (month <= 12 && month >= 1);
            if (!isOk)
            {
                return false;
            }
            if (month == 2)
            {
                // 闰年2月
                isOk = isOk && (((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) ? (day <= 29 && day >= 1)
                        : (day <= 28 && day >= 1));
            }
            else if (BIG_MONTH.contains(month))
            {
                isOk = isOk && day <= 31 && day >= 1;
            }
            else
            {
                isOk = isOk && day <= 30 && day >= 1;
            }
        }
        if (size >= YYYY_MM_DD_HH_MM)
        {
            int hour = Integer.valueOf(ss.get(HOUR));
            int minute = Integer.valueOf(ss.get(MINUTE));
            isOk = isOk && hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59;
        }
        if (size >= YYYY_MM_DD_HH_MM_SS)
        {
            int second = Integer.valueOf(ss.get(SECOND));
            isOk = isOk && second >= 0 && second <= 59;
        }
        if (size >= YYYY_MM_DD_HH_MM_SS_MS)
        {
            int millisecond = Integer.valueOf(ss.get(MILLSECOND));
            isOk = isOk && millisecond >= 0 && millisecond <= 999;
        }
        return isOk;
    }

    public static long now()
    {
        return new DateTime().getMillis();
    }

    public static long today()
    {
        return now() - new DateTime().getMillisOfDay() + 1000;
    }

    public static long tomorrow()
    {
        return today() + ONE_DAY;
    }

    public static long tomorrowNow()
    {
        return now() + ONE_DAY;
    }

    public static long yesterday()
    {
        return today() - ONE_DAY;
    }

    public static long yesterdayNow()
    {
        return now() - ONE_DAY;
    }

    public static String now(String formatter)
    {
        return new DateTime().toString(formatter);
    }

    public static String formmat(Date date, String fommater)
    {
        SimpleDateFormat format = new SimpleDateFormat(fommater);
        return format.format(date);
    }

    public static int nowMonth()
    {
        return new DateTime().getMonthOfYear();
    }

    public static int nowDayOfWeek()
    {
        return new DateTime().getDayOfWeek();
    }

    public static int nowDayOfMonth()
    {
        return new DateTime().getDayOfMonth();
    }

    public static int nowDayOfYear()
    {
        return new DateTime().getDayOfYear();
    }

    public static int nowHourOfDay()
    {
        return new DateTime().getHourOfDay();
    }

    /**
     * 将毫秒转换成年月日时分秒
     *
     * @param mills
     * @return
     */
    public static DateGetter date(long mills)
    {
        LocalDate localDate = LocalDate.fromDateFields(new Date(mills));
        LocalTime localTime = LocalTime.fromDateFields(new Date(mills));
        return new DateGetter(localDate, localTime);
    }

    public static class DateGetter
    {
        private LocalDate localDate;

        private LocalTime localTime;

        public DateGetter(LocalDate localDate, LocalTime localTime)
        {
            this.localDate = localDate;
            this.localTime = localTime;
        }

        public int year()
        {
            return localDate.getYear();
        }

        public int month()
        {
            return localDate.getMonthOfYear();
        }

        public int weekOfYear()
        {
            return localDate.getWeekOfWeekyear();
        }

        public int dayOfWeek()
        {
            return localDate.getDayOfWeek();
        }

        public int dayOfMonth()
        {
            return localDate.getDayOfMonth();
        }

        public int hour()
        {
            return localTime.getHourOfDay();
        }

        public int minute()
        {
            return localTime.getMinuteOfHour();
        }

        public int second()
        {
            return localTime.getSecondOfMinute();
        }
    }
}
