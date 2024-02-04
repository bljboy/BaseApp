package com.example.clouds.ui.clounds;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.example.clouds.base.BaseFragment;
import com.example.clouds.databinding.FragmentCloundsBinding;
import com.example.clouds.ui.adapter.CloundsAdapter;

public class CloundsFragment extends BaseFragment<FragmentCloundsBinding> {
    private CloundsAdapter mCloundsAdapter;

    @Override
    protected void initView() {
        mCloundsAdapter = new CloundsAdapter();
        mBinding.recyclerviewClounds.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerviewClounds.setAdapter(mCloundsAdapter);

    }

    @Override
    protected void initViewModel() {

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
    protected FragmentCloundsBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentCloundsBinding.inflate(inflater, container, false);
    }
}