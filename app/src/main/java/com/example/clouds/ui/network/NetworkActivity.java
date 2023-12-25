package com.example.clouds.ui.network;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import android.os.Bundle;
import android.view.View;

import com.example.clouds.R;
import com.example.clouds.adapter.network.NetWorkAdapter;
import com.example.clouds.base.BaseActivity;
import com.example.clouds.databinding.ActivityNetworkBinding;
import com.example.clouds.ui.keyingtone.KeyingToneViewModel;

public class NetworkActivity extends BaseActivity<ActivityNetworkBinding> implements View.OnClickListener {

    private NetWorkAdapter mNetworkAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private NetWorkViewModel mViewModel;
    private boolean isNetworkSwitch = true;

    @Override
    protected ActivityNetworkBinding getViewBinding() {
        return ActivityNetworkBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        //让recyclerview不能滑动
        mLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mBinding.recyclerNetworkConnect.setLayoutManager(mLayoutManager);
        mNetworkAdapter = new NetWorkAdapter();
        mBinding.recyclerNetworkConnect.setAdapter(mNetworkAdapter);
    }

    @Override
    protected void initListener() {
        mBinding.buttonBack.setOnClickListener(this);
        mBinding.networkButtonSwitch.setOnClickListener(this);

    }

    @Override
    protected void initViewModel() {
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(NetWorkViewModel.class);
    }

    @Override
    protected void initData() {
        mViewModel.netWorkSwitch.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isNetworkSwitch = aBoolean;
                mBinding.networkButtonSwitch.setChecked(aBoolean);
                mBinding.recyclerNetworkConnect.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });

    }

    @Override
    protected void loadData() {
        mViewModel.getStatus();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_back) {
            finish();
        } else if (v.getId() == R.id.network_button_switch) {
            if (isNetworkSwitch) {
                mBinding.recyclerNetworkConnect.setVisibility(View.VISIBLE);
            } else {
                mBinding.recyclerNetworkConnect.setVisibility(View.GONE);
            }
            mViewModel.netWorkSwitch.setValue(!isNetworkSwitch);
        }
    }
}