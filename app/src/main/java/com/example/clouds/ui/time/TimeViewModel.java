package com.example.clouds.ui.time;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeViewModel extends ViewModel {
    private static final String TAG = "TimeViewModel";
    private Calendar calendar;
    public MutableLiveData<Integer> year = new MutableLiveData<>();
    public MutableLiveData<Integer> month = new MutableLiveData<>();
    public MutableLiveData<Integer> day = new MutableLiveData<>();
    public MutableLiveData<Integer> hour = new MutableLiveData<>();
    public MutableLiveData<Integer> minute = new MutableLiveData<>();
    public MutableLiveData<Integer> second = new MutableLiveData<>();
    public MutableLiveData<Boolean> is24Hour = new MutableLiveData<>();
    public MutableLiveData<Integer> AMorPM = new MutableLiveData<>();
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    SimpleDateFormat timeFormat;

    public void initConnet(Context context) {
        calendar = Calendar.getInstance();
        mContext = context;
    }

    public void getStatus() {
        year.setValue(calendar.get(Calendar.YEAR));
        month.setValue(calendar.get(Calendar.MONTH) + 1); // 注意：月份是从0开始的
        day.setValue(calendar.get(Calendar.DAY_OF_MONTH));
        minute.setValue(calendar.get(Calendar.MINUTE));
        second.setValue(calendar.get(Calendar.SECOND));
        AMorPM.setValue(calendar.get(Calendar.AM_PM) == Calendar.AM ? 0 : 1);
        is24Hour.setValue(DateFormat.is24HourFormat(mContext));
        Log.d(TAG, "getStatus: " + "year:" + year);
        Log.d(TAG, "getStatus: " + "month:" + month);
        Log.d(TAG, "getStatus: " + "day:" + day);
        Log.d(TAG, "getStatus: " + "hour:" + hour);
        Log.d(TAG, "getStatus: " + "minute:" + minute);
        Log.d(TAG, "getStatus: " + "second:" + second);
        Log.d(TAG, "getStatus: " + "is24Hour:" + is24Hour.getValue());
        Log.d(TAG, "getStatus: " + "AMorPM:" + AMorPM);

        if (DateFormat.is24HourFormat(mContext)) {
            // 24小时制
//            timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            hour.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        } else {
            // 12小时制
//            timeFormat = new SimpleDateFormat("hh", Locale.getDefault()); // 'a' 表示 AM/PM
            int i = calendar.get(Calendar.HOUR);
            if (i == 0) {
                hour.setValue(12);
            } else {
                hour.setValue(i);
                Log.d(TAG, "getStatus: i" + i);
            }
        }

    }


    /**
     * 设置系统日期，需要有系统签名才可以
     */

    /**
     * 使用root权限设置系统日期
     *
     * @param year  年份
     * @param month 月份
     * @param day   日
     */

    public void setSystemDateTime(int year, int month, int day, int hour, int minute, boolean is24Hour) {
        Log.d(TAG, "setSystemDateTime: is24Hour..." + is24Hour);
        try {
            Process suProcess = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            //设置时间格式
            String is12_24 = is24Hour ? "settings put system time_12_24 24\n" : "settings put system time_12_24 12\n";
            os.writeBytes(is12_24);
            // 生成日期和时间设置命令
            @SuppressLint("DefaultLocale") String command = String.format("date %02d%02d%02d%02d%02d; \n",
                    month, day, hour, minute, year % 100);
            // 执行命令
            os.writeBytes(command);
            os.flush();
            // 退出su
            os.writeBytes("exit\n");
            os.flush();
            try {
                suProcess.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置系统时间，需要有系统签名才可以
     */
    public void setDate(int year, int month, int day, int hour, int minute) {
        // 检查权限
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.SET_TIME) != PackageManager.PERMISSION_GRANTED) {
            if (mContext instanceof Activity) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.SET_TIME}, 123);
            } else {
                Log.e(TAG, "Context is not an instance of Activity, cannot request permissions.");
            }
            return;
        }
        new Thread(() -> {
            try {
                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.YEAR, year);
//                calendar.set(Calendar.MONTH, month); // 注意：月份是从0开始的
//                calendar.set(Calendar.DAY_OF_MONTH, day);
//                calendar.set(Calendar.HOUR_OF_DAY, day);
                calendar.set(year, month - 1, day, hour, minute);
                long timeInMillis = calendar.getTimeInMillis();
                if (timeInMillis / 1000 < Integer.MAX_VALUE) {
                    ((AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE)).setTime(timeInMillis);
                    Log.d(TAG, "setDate: " + year + "," + month + "," + day);
                }
            } catch (SecurityException e) {
                Log.e(TAG, "Failed to set system time. Permission denied.", e);
            }
        }).start();
    }


}
