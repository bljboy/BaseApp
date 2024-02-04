package com.example.clouds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import android.os.Bundle;

import com.example.clouds.base.BaseActivity;
import com.example.clouds.databinding.ActivityMainBinding;
import com.example.clouds.ui.adapter.MainViewPagerAdapter;
import com.example.clouds.ui.clounds.CloundsFragment;
import com.example.clouds.ui.locals.LocalsFragment;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private MainViewPagerAdapter mMainViewPagerAdapter;


    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        mMainViewPagerAdapter = new MainViewPagerAdapter(this);
        mMainViewPagerAdapter.addFragment(new CloundsFragment(), getResources().getString(R.string.title_tab_clounds));
        mMainViewPagerAdapter.addFragment(new LocalsFragment(), getResources().getString(R.string.title_tab_locals));
        mBinding.mainViewpager.setAdapter(mMainViewPagerAdapter);
        new TabLayoutMediator(mBinding.mainTabs, mBinding.mainViewpager, ((tab, position) -> {
            tab.setText(mMainViewPagerAdapter.getPageTitle(position));
        })).attach();
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