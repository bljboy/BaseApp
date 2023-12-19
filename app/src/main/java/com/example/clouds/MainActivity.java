package com.example.clouds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.clouds.base.BaseActivity;
import com.example.clouds.databinding.ActivityMainBinding;
import com.tencent.mmkv.MMKV;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private final String TAG = getClass().getName();

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        mBinding.text.setText("你是谁");
        MMKV.initialize(this);
        MMKV mmkv = MMKV.defaultMMKV();
        mmkv.encode("name", 123);
        mmkv.apply();
        mBinding.mmkv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int name = mmkv.decodeInt("name");
                Log.d(TAG, "onClick: " + name);
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initViewModel() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void loadData() {

    }
}