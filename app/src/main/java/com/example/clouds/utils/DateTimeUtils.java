package com.example.clouds.utils;

import android.content.Context;
import android.util.Log;

import com.example.clouds.R;

public class DateTimeUtils {
    private static final String TAG = "DateTimeUtils";

    public static String YearFormat(int year) {
        return String.format("%04d", year);
    }

    public static String DateFormat(int month) {
        return String.format("%02d", month);
    }

    /**
     * 12 24 时间格式切换
     *
     * @param is24Hour
     * @param hour24
     * @param isAMorPM
     * @return
     */
    public static int updateHourDisplay(boolean is24Hour, int hour24, String isAMorPM, Context context) {
        Log.d(TAG, "updateHourDisplay:  isAMorPM..." + isAMorPM);
        Log.d(TAG, "updateHourDisplay:  is24Hour..." + is24Hour);

        int displayHour = hour24;
        if (!is24Hour) {
            if (hour24 == 0) {
                displayHour = 12; // 午夜12点
            } else if (hour24 > 12) {
                displayHour = hour24 - 12; // 转换为12小时制
            }
            Log.d(TAG, "updateHourDisplay: displayHour1..." + displayHour);

        } else {
            if (isAMorPM.equals(context.getString(R.string.string_am))) {
                if (hour24 == 12) {
                    displayHour = 0; // 12小时制中的12 AM 是 24小时制中的 0
                    Log.d(TAG, "updateHourDisplay:  am ..." + displayHour);
                }
            } else {
                displayHour = hour24 + 12;
                Log.d(TAG, "updateHourDisplay:  pm ..." + displayHour);
            }
            Log.d(TAG, "updateHourDisplay:  ..." + displayHour);
        }
        return displayHour;
    }

    /**
     * 12小时时间格式，设置系统am，pm
     */
    public static int set12Hour(String is24Hour, String isAMorPM, Context context, int hour24) {
        int displayHour = hour24;
        if (!is24Hour.equals(context.getString(R.string.string_24hour))) {
            if (isAMorPM.equals(context.getString(R.string.string_am))) {
                if (hour24 == 12) {
                    displayHour = 0; // 12小时制中的12 AM 是 24小时制中的 0
                    Log.d(TAG, "set12Hour:  am ..." + displayHour);
                }
            } else {
                displayHour = hour24 + 12;
                Log.d(TAG, "set12Hour:  pm ..." + displayHour);
            }
            Log.d(TAG, "set12Hour:  ..." + displayHour);
        }
        return displayHour;
    }
}
