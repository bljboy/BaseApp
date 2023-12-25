package com.example.clouds.ui.version;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.example.clouds.R;
import com.example.clouds.base.BaseActivity;
import com.example.clouds.databinding.ActivityVersionBinding;

public class VersionActivity extends BaseActivity<ActivityVersionBinding> implements View.OnClickListener {


    @Override
    protected ActivityVersionBinding getViewBinding() {
        return ActivityVersionBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        mBinding.buttonBack.setOnClickListener(this);
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_back) {
            finish();
        }
    }
}