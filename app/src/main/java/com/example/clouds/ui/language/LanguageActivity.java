package com.example.clouds.ui.language;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import android.os.Bundle;
import android.view.View;

import com.example.clouds.R;
import com.example.clouds.base.BaseActivity;
import com.example.clouds.databinding.ActivityLanguageBinding;

public class LanguageActivity extends BaseActivity<ActivityLanguageBinding> implements View.OnClickListener {


    @Override
    protected ActivityLanguageBinding getViewBinding() {
        return ActivityLanguageBinding.inflate(getLayoutInflater());
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