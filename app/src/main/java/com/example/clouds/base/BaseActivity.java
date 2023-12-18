package com.example.clouds.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {
    protected VB mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = getViewBinding();
        setContentView(mBinding.getRoot());
        initViewModel();
        initView();
        loadData();
        initData();
        initListener();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract VB getViewBinding();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initViewModel();

    protected abstract void initData();

    protected abstract void loadData();
}
