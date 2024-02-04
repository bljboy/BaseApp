package com.example.clouds.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.example.clouds.base.BaseFragment;
import com.example.clouds.databinding.FragmentBlankBinding;


public class BlankFragment extends BaseFragment<FragmentBlankBinding> {
    private BlankViewModel mViewModel;
    private BlankFragment blankFragment;

    @Override
    protected void initView() {
//        mBinding.text.setText("我爱你");
    }

    @Override
    protected void initViewModel() {
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(BlankViewModel.class);
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

    @Override
    protected FragmentBlankBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentBlankBinding.inflate(inflater, container, false);
    }

}