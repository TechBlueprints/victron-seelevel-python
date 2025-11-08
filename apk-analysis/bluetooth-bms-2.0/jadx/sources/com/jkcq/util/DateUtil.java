package com.jkcq.util;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import com.alibaba.android.arouter.utils.Consts;
import com.blankj.utilcode.util.SizeUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes.dex */
public class DateUtil {
    public static final int CALENDAR_DAY = 5;
    public static final int CALENDAR_HOUR = 11;
    public static final int CALENDAR_MINUTE = 12;
    public static final int CALENDAR_MONTH = 2;
    public static final int CALENDAR_SECOND = 13;
    public static final int CALENDAR_YEAR = 1;
    public static final String DAY = "dd";
    public static final String DD = "dd";
    public static final String HH_MM = "HH:mm";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String HOUR = "HH";
    public static final String MINUTE = "mm";
    public static final String MM = "MM";
    public static final String MM_DD = "MM-dd";
    public static final String MM_SS = "mm:ss";
    public static final String MONTH = "MM";
    public static final int OTHER_DAY = -1;
    public static final String SECOND = "ss";
    private static final int S_DAY = 86400;
    private static final int S_HOUR = 3600;
    private static final int S_MINUTE = 60;
    private static final int S_MONTH = 2592000;
    private static final int S_YEAR = 31536000;
    public static final int TODAY = 0;
    public static final int TOMORROW = 1;
    public static final String YEAR = "yyyy";
    public static final String YYYY = "yyyy";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public static float ftoc(float f) {
        return 0.0f;
    }

    public static String getCurrentDate(String str) {
        return getDate(str, System.currentTimeMillis());
    }

    public static long getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    public static String getWhichDate(String str, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, i);
        return getDate(str, calendar);
    }

    public static Calendar copy(Calendar calendar) {
        Calendar currentCalendar = getCurrentCalendar();
        currentCalendar.setTimeInMillis(calendar.getTimeInMillis());
        return currentCalendar;
    }

    public static Calendar getCurrentCalendar() {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar;
    }

    public static Calendar getCurrentCalendarBegin() {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.set(11, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(14, 0);
        return calendar;
    }

    public static Calendar getCurrentCalendarEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 23);
        calendar.set(13, 59);
        calendar.set(12, 59);
        calendar.set(14, 0);
        return calendar;
    }

    public static boolean isCurrentAM() {
        return getCurrentCalendar().get(9) == 0;
    }

    public static boolean isCurrentPM() {
        return getCurrentCalendar().get(9) == 1;
    }

    public static boolean isTimeUnit24(Context context) {
        try {
            return "24".equals(Settings.System.getString(context.getContentResolver(), "time_12_24"));
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static int getYear(Calendar calendar) {
        return calendar.get(1);
    }

    public static int getYear(Date date) {
        return getYear(convertDateToCalendar(date));
    }

    public static int getYear(long j) {
        return getYear(convertLongToCalendar(j));
    }

    public static int getMonth(Calendar calendar) {
        return calendar.get(2) + 1;
    }

    public static int getMonth(Date date) {
        return getMonth(convertDateToCalendar(date));
    }

    public static int getMonth(long j) {
        return getMonth(convertLongToCalendar(j));
    }

    public static int getDay(Calendar calendar) {
        return calendar.get(5);
    }

    public static int getDay(Date date) {
        return getDay(convertDateToCalendar(date));
    }

    public static int getDay(long j) {
        return getDay(convertLongToCalendar(j));
    }

    public static int getHour(String str) throws ParseException {
        return getHour(convertStringToCalendar(YYYY_MM_DD_HH_MM_SS, str));
    }

    public static int getHour(Calendar calendar) {
        return calendar.get(11);
    }

    public static int getHour(Date date) {
        return getHour(convertDateToCalendar(date));
    }

    public static int getHour(long j) {
        return getHour(convertLongToCalendar(j));
    }

    public static int getMinute(String str) throws ParseException {
        return getMinute(convertStringToCalendar(YYYY_MM_DD_HH_MM_SS, str));
    }

    public static int getMinute(Calendar calendar) {
        return calendar.get(12);
    }

    public static int getMinute(Date date) {
        return getMinute(convertDateToCalendar(date));
    }

    public static int getMinute(long j) {
        return getMinute(convertLongToCalendar(j));
    }

    public static int getSecond(String str) throws ParseException {
        return getSecond(convertStringToCalendar(YYYY_MM_DD_HH_MM_SS, str));
    }

    public static int getSecond(Calendar calendar) {
        return calendar.get(13);
    }

    public static int getSecond(Date date) {
        return getSecond(convertDateToCalendar(date));
    }

    public static int getSecond(long j) {
        return getSecond(convertLongToCalendar(j));
    }

    public static String getDate(String str, long j) {
        return new SimpleDateFormat(str, Locale.ENGLISH).format(Long.valueOf(j));
    }

    public static String getDate(String str, Date date) {
        return new SimpleDateFormat(str, Locale.ENGLISH).format(date);
    }

    public static String getDate(String str, String str2, String str3) {
        try {
            return convertStringToNewString(str, str2, str3);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDate(String str, Calendar calendar) {
        return getDate(str, calendar.getTime());
    }

    public static Calendar getYearLastDate(Calendar calendar) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(calendar.getTimeInMillis());
        calendar2.set(2, calendar2.getActualMaximum(2));
        calendar2.set(11, 0);
        calendar2.set(13, 0);
        calendar2.set(12, 0);
        calendar2.set(14, 0);
        return calendar2;
    }

    public static Calendar getYearFirstDate(Calendar calendar) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(calendar.getTimeInMillis());
        calendar2.set(2, 0);
        calendar2.set(5, 1);
        calendar2.set(11, 0);
        calendar2.set(13, 0);
        calendar2.set(12, 0);
        calendar2.set(14, 0);
        return calendar2;
    }

    public static Calendar getMonthLastDate(Calendar calendar) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(calendar.getTimeInMillis());
        calendar2.set(5, calendar2.getActualMaximum(5));
        return calendar2;
    }

    public static Calendar getMonthFirstDate(Calendar calendar) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(calendar.getTimeInMillis());
        calendar2.set(5, 1);
        return calendar2;
    }

    public static Calendar getQuarterLastDate(Calendar calendar) {
        return getMonthLastDate(calendar);
    }

    public static Calendar getQuarterFirstDate(Calendar calendar) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(calendar.getTimeInMillis());
        calendar2.add(2, -2);
        calendar2.set(5, 1);
        return calendar2;
    }

    public static Calendar getWeekFirstDate(Calendar calendar) {
        Calendar calendar2 = Calendar.getInstance(Locale.CHINA);
        calendar2.setTimeInMillis(calendar.getTimeInMillis());
        calendar2.set(7, 1);
        return calendar2;
    }

    public static Calendar getWeekLastDate(Calendar calendar) {
        Calendar calendar2 = Calendar.getInstance(Locale.CHINA);
        calendar2.setTimeInMillis(calendar.getTimeInMillis());
        calendar2.set(7, 7);
        return calendar2;
    }

    public static int getDateOffset(Calendar calendar, Calendar calendar2) {
        return Math.round(((calendar.getTimeInMillis() - calendar2.getTimeInMillis()) * 1.0f) / 8.604E7f);
    }

    public static int getDateOffset(String str, String str2) {
        try {
            return getDateOffset(convertStringToCalendar(YYYY_MM_DD, str), convertStringToCalendar(YYYY_MM_DD, str2));
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getWeekString(int i, int i2, int i3) {
        return weekDays[getWeekIndex(i, i2, i3)];
    }

    public static String getWeekString(Calendar calendar) {
        return weekDays[getWeekIndex(calendar)];
    }

    public static int getWeekIndex(int i, int i2, int i3) {
        Calendar.getInstance().set(i, i2 - 1, i3);
        return r0.get(7) - 1;
    }

    public static int getWeekIndex(Calendar calendar) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(calendar.get(1), calendar.get(2), calendar.get(5));
        return calendar2.get(7) - 1;
    }

    public static int getMonthIndex(int i, int i2, int i3) {
        Calendar.getInstance().set(i, i2 - 1, i3);
        return r0.get(5) - 1;
    }

    public static int getMonthIndex(Calendar calendar) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(calendar.get(1), calendar.get(2), calendar.get(5));
        return calendar2.get(5) - 1;
    }

    public static Date convertCalendarToDate(Calendar calendar) {
        return convertLongToDate(convertCalendarToLong(calendar));
    }

    public static Date convertLongToDate(long j) {
        return new Date(j);
    }

    public static Date convertStringToDate(String str, String str2) throws ParseException {
        return convertLongToDate(convertStringToLong(str, str2));
    }

    public static long convertDateToLong(Date date) throws ParseException {
        return date.getTime();
    }

    public static long convertCalendarToLong(Calendar calendar) {
        return calendar.getTimeInMillis();
    }

    public static long convertStringToLong(String str, String str2) throws ParseException {
        return new SimpleDateFormat(str, Locale.ENGLISH).parse(str2).getTime();
    }

    public static Calendar convertDateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Calendar convertLongToCalendar(long j) {
        return convertDateToCalendar(convertLongToDate(j));
    }

    public static long convertLongToCurTimeZoneLong(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(14, -calendar.get(15));
        return j + (((System.currentTimeMillis() - calendar.getTimeInMillis()) / 3600000) * 60 * 60 * 1000);
    }

    public static Calendar convertStringToCalendar(String str, String str2) throws ParseException {
        return convertDateToCalendar(convertStringToDate(str, str2));
    }

    public static String convertCalendarToString(String str, Calendar calendar) {
        return getDate(str, convertCalendarToDate(calendar));
    }

    public static String convertStringToNewString(String str, String str2, String str3) throws ParseException {
        return getDate(str2, convertStringToDate(str, str3));
    }

    public static int convertTimeToIndex(Calendar calendar, int i) {
        return convertTimeToIndex(calendar.get(11), calendar.get(12), i);
    }

    public static int convertTimeToIndex(int i, int i2, int i3) {
        return (i * (60 / i3)) + (i2 / i3);
    }

    public static HMS convertIndexToTime(int i, int i2) {
        int i3 = 60 / i2;
        int i4 = i % i3;
        int i5 = i / i3;
        if (i2 <= 1 || i4 <= 0) {
            i2 = i4;
        }
        HMS hms = new HMS();
        hms.setHour(i5);
        hms.setMinute(i2);
        hms.setSecond(0);
        return hms;
    }

    public static HMS getHMSFromMillis(long j) {
        return getHMSFromMinutes((int) (j / 1000));
    }

    public static HMS getHMSFromMinutes(int i) {
        return new HMS(i / 3600, (i / 60) % 60, i % 60);
    }

    public static HMS getHMSFromString(String str, String str2) throws ParseException {
        Calendar calendarConvertStringToCalendar = convertStringToCalendar(str, str2);
        return new HMS(calendarConvertStringToCalendar.get(11), calendarConvertStringToCalendar.get(12), calendarConvertStringToCalendar.get(13));
    }

    public static HMS getHMSFromSeconds(int i) {
        return getHMSFromMillis(i * 1000);
    }

    public static boolean equalsDate(String str, String str2, String str3) {
        return equalsDate(str, str2, str, str3);
    }

    public static boolean equalsDate(String str, String str2, String str3, String str4) {
        try {
            return convertStringToNewString(str, YYYY_MM_DD, str2).equalsIgnoreCase(convertStringToNewString(str3, YYYY_MM_DD, str4));
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
    }

    public static boolean equalsDate(String str, String str2) {
        return equalsDate(YYYY_MM_DD, str, YYYY_MM_DD, str2);
    }

    public static boolean equalsDate(Calendar calendar, Calendar calendar2) {
        return equalsDate(YYYY_MM_DD, getDate(YYYY_MM_DD, calendar), YYYY_MM_DD, getDate(YYYY_MM_DD, calendar2));
    }

    public static boolean equalsDate(String str, Calendar calendar, Calendar calendar2) {
        return equalsDate(str, getDate(str, calendar), str, getDate(str, calendar2));
    }

    public static boolean equalsToday(String str) {
        return equalsDate(str, getCurrentDate(YYYY_MM_DD));
    }

    public static boolean equalsToday(long j) {
        return equalsDate(getDate(YYYY_MM_DD_HH_MM_SS, j), getCurrentDate(YYYY_MM_DD));
    }

    public static String getHHmmss(long j) {
        return getDate(HH_MM_SS, j);
    }

    public static String getYYMMDDHHmmss(long j) {
        return getDate(YYYY_MM_DD_HH_MM_SS, j);
    }

    public static String getYyyyMm(long j) {
        return getDate(YYYY_MM_DD, j);
    }

    public static boolean equalsToday(Calendar calendar) {
        return equalsDate(getDate(YYYY_MM_DD, calendar), getCurrentDate(YYYY_MM_DD));
    }

    public static int whichDay(String str) throws ParseException {
        boolean zEqualsDate = equalsDate(YYYY_MM_DD_HH_MM, str, getCurrentDate(YYYY_MM_DD_HH_MM));
        boolean zEqualsDate2 = equalsDate(YYYY_MM_DD_HH_MM, str, getWhichDate(YYYY_MM_DD_HH_MM, 1));
        if (zEqualsDate) {
            return 0;
        }
        return zEqualsDate2 ? 1 : -1;
    }

    public static class HMS {
        int hour;
        int minute;
        int second;

        public int getHour() {
            return this.hour;
        }

        public void setHour(int i) {
            this.hour = i;
        }

        public int getMinute() {
            return this.minute;
        }

        public void setMinute(int i) {
            this.minute = i;
        }

        public int getSecond() {
            return this.second;
        }

        public void setSecond(int i) {
            this.second = i;
        }

        public HMS() {
        }

        public HMS(int i, int i2, int i3) {
            this.hour = i;
            this.minute = i2;
            this.second = i3;
        }
    }

    public static String formatOnePoint(float f) {
        return String.format("%.1f", Float.valueOf(Math.round(f * 100.0f) / 100.0f));
    }

    public static String formatfloorOnePoint(float f) {
        String str = String.format("%.1f", Double.valueOf(Math.floor((f * 100.0f) / 10.0f) / 10.0d));
        Log.e("formatfloorOnePoint", str);
        return str;
    }

    public static double formatOnePointDouble(float f) {
        return Double.parseDouble(String.format("%.1f", Float.valueOf(f)));
    }

    public static String formatTwoStr(int i) {
        return String.format("%02d", Integer.valueOf(i));
    }

    public static String formatTwoPoint(double d) {
        return String.format("%.2f", Double.valueOf(d / 1000.0d)).replace(",", Consts.DOT);
    }

    public static String formatThreeoint(double d) {
        return String.format("%.2f", Double.valueOf(d / 1000.0d)).replace(",", Consts.DOT);
    }

    public static String formatThreeo(double d) {
        return String.format("%.2f", Double.valueOf(d)).replace(",", Consts.DOT);
    }

    public static float formatFloor(float f, boolean z) {
        return z ? ((float) (Math.floor(f / 10.0f) * 10.0d)) / 1000.0f : f / 1000.0f;
    }

    public static float ctof(float f) {
        return (float) (Math.floor((((f * 1.8f) + 32.0f) * 100.0f) / 10.0f) / 10.0d);
    }

    public static String formatTwoPoint(float f) {
        return String.format("%.2f", Float.valueOf(Math.round(f * 1000.0f) / 1000.0f)).replace(",", Consts.DOT);
    }

    public static String formatTwoPointThree(double d) {
        return String.format("%.2f", Float.valueOf(Math.round(d * 1000.0d) / 1000.0f));
    }

    public static String formatInterger(double d) {
        return Math.round(d) + "";
    }

    public static int dip2px(float f) {
        return SizeUtils.dp2px(f);
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    public static String dataToString(Date date, String str) {
        try {
            return new SimpleDateFormat(str).format(date);
        } catch (Exception unused) {
            return System.currentTimeMillis() + "";
        }
    }

    public static int getMonthOfDay(int i, int i2) {
        int i3 = ((i % 4 != 0 || i % 100 == 0) && i % 400 != 0) ? 28 : 29;
        switch (i2) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                return i3;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 0;
        }
    }

    public static String getModel() {
        String str = Build.MODEL;
        return str != null ? str.trim().replaceAll("\\s*", "") : "";
    }
}
