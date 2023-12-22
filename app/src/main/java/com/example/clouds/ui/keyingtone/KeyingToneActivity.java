package com.example.clouds.ui.keyingtone;

import androidx.lifecycle.ViewModelProvider;

import com.example.clouds.databinding.ActivityKeyingtoneBinding;

import com.example.clouds.base.BaseActivity;


public class KeyingToneActivity extends BaseActivity<ActivityKeyingtoneBinding> {
    private KeyingToneViewModel mViewModel;
    private KeyingToneActivity blankFragment;


    @Override
    protected ActivityKeyingtoneBinding getViewBinding() {
        return ActivityKeyingtoneBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
//        mBinding.text.setText("我爱你");
    }

    @Override
    protected void initViewModel() {
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(KeyingToneViewModel.class);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initData() {

    }


}