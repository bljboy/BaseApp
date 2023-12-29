package com.example.clouds.ui.network;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.example.clouds.R;
import com.example.clouds.adapter.NetWorkConnectAdapter;
import com.example.clouds.adapter.NetWorkSearchAdapter;
import com.example.clouds.base.BaseActivity;
import com.example.clouds.receiver.NetWorkReceiver;
import com.example.clouds.databinding.ActivityNetworkBinding;
import com.example.clouds.listener.WifiStateChangeListener;

public class NetWorkActivity extends BaseActivity<ActivityNetworkBinding> implements View.OnClickListener, WifiStateChangeListener {

    private static final String TAG = "NetWorkActivity";
    private NetWorkReceiver netWorkReceiver;
    private NetWorkSearchAdapter mNetworkSearchAdapter;
    private NetWorkConnectAdapter mNetworkConnectAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mSearchLayoutManager;
    private NetWorkViewModel mViewModel;
    private boolean isNetworkSwitch = true;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected ActivityNetworkBinding getViewBinding() {
        return ActivityNetworkBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        //WiFi开关按钮广播WiFi状态
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(netWorkReceiver, intentFilter);

        //扫描WiFi列表适配器
        mSearchLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mBinding.recyclerNetworkSearch.setLayoutManager(mSearchLayoutManager);
        mNetworkSearchAdapter = new NetWorkSearchAdapter();
        mBinding.recyclerNetworkSearch.setAdapter(mNetworkSearchAdapter);
        //让recyclerview不能滑动
        mLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        //已保存wifi列表适配器
        mBinding.recyclerNetworkConnect.setLayoutManager(mLayoutManager);
        mNetworkConnectAdapter = new NetWorkConnectAdapter(mViewModel, this);
        mBinding.recyclerNetworkConnect.setAdapter(mNetworkConnectAdapter);
    }

    @Override
    protected void initListener() {
        mBinding.buttonBack.setOnClickListener(this);
        mBinding.networkButtonSwitch.setOnClickListener(this);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(NetWorkViewModel.class);
        mViewModel.checkPermissions(this);
        mViewModel.initConnect(this);
        netWorkReceiver = new NetWorkReceiver(this);
    }

    @Override
    protected void initData() {
        mViewModel.isWifiRemove.observe(this, values -> {
            if (values) {
                Log.d(TAG, "断开连接。。。: initData...isWifiDisConnected = [" + values + "]");
                mNetworkConnectAdapter.notifyDataSetChanged();
            }
        });
        mViewModel.isWifiDisConnected.observe(this, values -> {
            if (values) {
                Log.d(TAG, "断开连接。。。: initData...isWifiDisConnected = [" + values + "]");
//                mNetworkConnectAdapter.notifyDataSetChanged();
            }
        });
        mViewModel.isWifiConnectSuccess.observe(this, values -> {
            if (values) {
                Log.d(TAG, "正在连接中。。。: initData...isWifiConnecting = [" + values + "]");
                mNetworkConnectAdapter.notifyDataSetChanged();
            }
        });
        mViewModel.isWifiConnected.observe(this, values -> {
            if (values != null) {
                mNetworkConnectAdapter.getisWifiConnected(values);
                Log.d(TAG, "当前连接WiFi: isWifiConnected = [" + values + "]");
            }
        });
        mViewModel.netWorkSwitch.observe(this, enable -> {
            isNetworkSwitch = enable;
            mBinding.networkButtonSwitch.setChecked(enable);
            showNetworkView(enable);
        });
        mViewModel.wifiConfigList.observe(this, enable -> {
            Log.d(TAG, "initData: " + enable);
            if (enable != null) {
                mNetworkConnectAdapter.setListWifi(enable);
            }
        });
    }

    @Override
    protected void loadData() {
        mViewModel.getStatus();
        mViewModel.getConnectionInfo(this);
        mViewModel.getConnected(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netWorkReceiver);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_back) {
            finish();
        } else if (v.getId() == R.id.network_button_switch) {
            //wifi开关按钮
            showNetworkView(isNetworkSwitch);
            mViewModel.setWifiEnabled(!isNetworkSwitch);
        }
    }

    @Override
    public void onWifiStateChange(boolean enabled) {
        mViewModel.netWorkSwitch.setValue(enabled);
        Log.d(TAG, "onWifiStateChange = [" + enabled + "]");
    }

    @Override
    public void onWifiConnectSuccess(boolean enabled) {
        mViewModel.isWifiConnectSuccess.setValue(enabled);
        Log.d(TAG, "正在连接中。。。: onWifiConnecting = [" + enabled + "]");
    }

    @Override
    public void onWifiDisConnected(boolean enabled) {
        mViewModel.isWifiDisConnected.setValue(enabled);
        Log.d(TAG, "断开连接。。。: onWifiDisConnected = [" + enabled + "]");
    }

    //根据wifi开关显示列表
    private void showNetworkView(boolean enabled) {
        mBinding.recyclerNetworkConnect.setVisibility(enabled ? View.VISIBLE : View.GONE);
        mBinding.recyclerNetworkSearch.setVisibility(enabled ? View.VISIBLE : View.GONE);
        mBinding.networkOtherTitle.setVisibility(enabled ? View.VISIBLE : View.GONE);
        mBinding.networkButtonRefresh.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }

}