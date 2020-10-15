package com.weiqing.noctorrosmssend.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Rodney Cheung
 * @date 10/15/2020 10:07 AM
 */
public class DateUtil {
    /**
     * lock
     */
    private static final Object LOCK_OBJ = new Object();

    /**
     * SimpleDateFormat map
     */
    private static final Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<>();


    /**
     * return ThreadLocal SimpleDateFormat
     *
     * @param pattern pattern
     * @return SimpleDateFormat
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (LOCK_OBJ) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern));
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }

    /**
     * 使用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
     * 如果新的线程中没有SimpleDateFormat，才会new一个
     *
     * @param date    date
     * @param pattern pattern
     * @return formatted date
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return getSdf(pattern).format(date);
    }

    public static Date parse(String dateStr, String pattern) throws ParseException {
        return getSdf(pattern).parse(dateStr);
    }

    public static List<String> findDaysStr(String startTime, String endTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startTime);
        Date endDate = sdf.parse(endTime);
        List<String> daysStrList = new ArrayList<>();
        daysStrList.add(sdf.format(startDate));
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(startDate);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);
        while (endDate.after(calBegin.getTime())) {
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            String dayStr = sdf.format(calBegin.getTime());
            daysStrList.add(dayStr);
        }
        return daysStrList;
    }

    public static String getCurrentTimeStr() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static Date getCurrentTimestamp() {
        return Calendar.getInstance().getTime();
    }


    public static String getMidnightDateTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
    }

    public static boolean isValidDate(String dataTime, String dateFormat) {
        if (StringUtils.isBlank(dataTime)) {
            return false;
        }
        boolean convertSuccess = true;
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        try {
            format.setLenient(false);
            format.parse(dataTime);
        } catch (ParseException e) {
            convertSuccess = false;
        }
        return convertSuccess;
    }

    /**
     * get start time and end time str,overflow is not considered
     *
     * @param endTimeInterval   time from now
     * @param endTimeUnit       end time interval unit
     * @param startTimeInterval time from end time
     * @param startTimeUnit     start time interval unit
     * @param timeFmt           result time format
     * @return String[0] is startTime String[0] is endTime
     */
    public static String[] getStartTimeAndEndTime(long endTimeInterval,
                                                  TimeUnit endTimeUnit,
                                                  long startTimeInterval,
                                                  TimeUnit startTimeUnit,
                                                  String timeFmt) {
        if (endTimeUnit != TimeUnit.SECONDS) {
            endTimeInterval = endTimeUnit.toSeconds(endTimeInterval);
        }
        if (startTimeUnit != TimeUnit.SECONDS) {
            startTimeInterval = startTimeUnit.toSeconds(startTimeInterval);
        }
        String[] startAndEndTime = new String[2];
        Calendar calendar = Calendar.getInstance();
        int endTimeIntervalInteger = (int) endTimeInterval;
        calendar.add(Calendar.SECOND, -endTimeIntervalInteger);
        startAndEndTime[1] = format(calendar.getTime(), timeFmt);

        int startTimeIntervalInteger = (int) startTimeInterval;
        calendar.add(Calendar.SECOND, -startTimeIntervalInteger);
        startAndEndTime[0] = format(calendar.getTime(), timeFmt);
        return startAndEndTime;
    }
}
