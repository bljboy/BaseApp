package com.example.clouds.ui.network;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import android.os.Bundle;
import android.view.View;

import com.example.clouds.R;
import com.example.clouds.base.BaseActivity;
import com.example.clouds.databinding.ActivityNetworkBinding;

public class NetworkActivity extends BaseActivity<ActivityNetworkBinding> implements View.OnClickListener {


    @Override
    protected ActivityNetworkBinding getViewBinding() {
        return ActivityNetworkBinding.inflate(getLayoutInflater());
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_back) {
            finish();
        }
    }
}