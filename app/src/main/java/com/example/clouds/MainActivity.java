package com.example.clouds;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;

import com.example.clouds.base.BaseActivity;
import com.example.clouds.databinding.ActivityMainBinding;
import com.tencent.mmkv.MMKV;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private final String TAG = getClass().getName();
    private MainModel mModel;
    private MMKV mmkv;

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        mBinding.text.setText("你是谁");
    }

    @Override
    protected void initListener() {
        mBinding.mmkv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.setNum("记录");
            }
        });

    }

    @Override
    protected void initViewModel() {
        mModel = new ViewModelProvider.NewInstanceFactory().create(MainModel.class);
        mModel.initMMKV(this);
    }

    @Override
    protected void initData() {
        mModel.num.observe(this, values -> {
            mBinding.text.setText(values);
        });
    }

    @Override
    protected void loadData() {

    }
}