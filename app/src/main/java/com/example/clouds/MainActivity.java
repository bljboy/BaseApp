package com.example.clouds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import android.os.Bundle;

import com.example.clouds.base.BaseActivity;
import com.example.clouds.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

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