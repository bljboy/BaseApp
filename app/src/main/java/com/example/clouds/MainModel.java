package com.example.clouds;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tencent.mmkv.MMKV;

public class MainModel extends ViewModel {
    private MMKV mmkv;
    public MutableLiveData<String> num = new MutableLiveData<>();

    public void setNum(String value) {
        num.setValue(value);
    }

    public void initMMKV(Context context) {
        MMKV.initialize(context);
        mmkv = MMKV.defaultMMKV();
    }
}
