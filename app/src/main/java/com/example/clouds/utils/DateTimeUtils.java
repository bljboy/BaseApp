package com.example.clouds.utils;

public class DateTimeUtils {

    public static String YearFormat(int year) {
        return String.format("%04d", year);
    }

    public static String MonthFormat(int month) {
        return String.format("%02d", month);
    }

    public static String DayFormat(int day) {
        return String.format("%02d", day);
    }

    public static String HourFormat(int hour) {
        return String.format("%02d", hour);
    }

    public static String MinuteFormat(int minute) {
        return String.format("%02d", minute);
    }
}
