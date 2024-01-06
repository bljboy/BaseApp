package com.example.clouds.ui.time;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;

public class TimeViewModel extends ViewModel {
    private static final String TAG = "TimeViewModel";
    private Calendar calendar;
    public MutableLiveData<Integer> year = new MutableLiveData<>();
    public MutableLiveData<Integer> month = new MutableLiveData<>();
    public MutableLiveData<Integer> day = new MutableLiveData<>();
    public MutableLiveData<Integer> hour = new MutableLiveData<>();
    public MutableLiveData<Integer> minute = new MutableLiveData<>();
    public MutableLiveData<Integer> second = new MutableLiveData<>();

    public void initConnet() {
        calendar = Calendar.getInstance();
    }

    public void getStatus() {
        year.setValue(calendar.get(Calendar.YEAR));
        month.setValue(calendar.get(Calendar.MONTH) + 1); // 注意：月份是从0开始的
        day.setValue(calendar.get(Calendar.DAY_OF_MONTH));
        hour.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        minute.setValue(calendar.get(Calendar.MINUTE));
        second.setValue(calendar.get(Calendar.SECOND));
        Log.d(TAG, "getStatus: " + "year:" + year);
        Log.d(TAG, "getStatus: " + "month:" + month);
        Log.d(TAG, "getStatus: " + "day:" + day);
        Log.d(TAG, "getStatus: " + "hour:" + hour);
        Log.d(TAG, "getStatus: " + "minute:" + minute);
        Log.d(TAG, "getStatus: " + "second:" + second);
    }

}
