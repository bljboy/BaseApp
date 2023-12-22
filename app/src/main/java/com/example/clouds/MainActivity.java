package com.example.clouds;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;

import com.example.clouds.base.BaseActivity;
import com.example.clouds.databinding.ActivityMainBinding;
import com.example.clouds.ui.keyingtone.KeyingToneActivity;
import com.example.clouds.ui.language.LanguageActivity;
import com.example.clouds.ui.network.NetworkActivity;
import com.example.clouds.ui.time.TimeActivity;
import com.example.clouds.ui.version.VersionActivity;
import com.tencent.mmkv.MMKV;


public class MainActivity extends BaseActivity<ActivityMainBinding> implements View.OnClickListener {

    private final String TAG = getClass().getName();
    private MainModel mModel;
    private MMKV mmkv;

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initListener() {
        mBinding.buttonKeyingtone.setOnClickListener(this);
        mBinding.buttonLanguage.setOnClickListener(this);
        mBinding.buttonNetwork.setOnClickListener(this);
        mBinding.buttonTime.setOnClickListener(this);
        mBinding.buttonVersion.setOnClickListener(this);
        mBinding.buttonBack.setOnClickListener(this);
    }

    @Override
    protected void initViewModel() {
        mModel = new ViewModelProvider.NewInstanceFactory().create(MainModel.class);
        mModel.initMMKV(this);
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
        switch (v.getId()) {
            case R.id.button_back:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.button_keyingtone:
                startActivity(new Intent(MainActivity.this, KeyingToneActivity.class));
                break;
            case R.id.button_language:
                startActivity(new Intent(MainActivity.this, LanguageActivity.class));
                break;
            case R.id.button_time:
                startActivity(new Intent(MainActivity.this, TimeActivity.class));
                break;
            case R.id.button_version:
                startActivity(new Intent(MainActivity.this, VersionActivity.class));
                break;
            case R.id.button_network:
                startActivity(new Intent(MainActivity.this, NetworkActivity.class));
                break;
        }
    }
}