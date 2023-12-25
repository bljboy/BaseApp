package com.example.clouds.ui.keyingtone;

import android.view.View;
import android.widget.RadioGroup;

import androidx.lifecycle.ViewModelProvider;

import com.example.clouds.R;
import com.example.clouds.databinding.ActivityKeyingtoneBinding;

import com.example.clouds.base.BaseActivity;


public class KeyingToneActivity extends BaseActivity<ActivityKeyingtoneBinding> implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private KeyingToneViewModel mViewModel;
    private KeyingToneActivity blankFragment;
    private boolean keyingTone_Status = false;

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
        mBinding.buttonBack.setOnClickListener(this);
        mBinding.keyingtoneRg.setOnCheckedChangeListener(this);
        mBinding.buttonOk.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
        mViewModel.getStatus();
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_back) {
            //返回按钮
            finish();
        } else if (v.getId() == R.id.button_ok) {
            //按键音开关
            mViewModel.setKeyingTone(keyingTone_Status);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.keyingtone_open) {
            keyingTone_Status = true;
        } else if (checkedId == R.id.keyingtone_close) {
            keyingTone_Status = false;
        }
    }
}