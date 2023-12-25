package com.example.clouds.ui.keyingtone;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class KeyingToneViewModel extends ViewModel {
    private String TAG = getClass().getName();
    private MutableLiveData<Boolean> keyingTone = new MutableLiveData<Boolean>();

    public void getStatus() {

    }

    public void setKeyingTone(boolean enabled) {
        Log.i(TAG, "设置按键音状态：" + enabled);
    }
}
