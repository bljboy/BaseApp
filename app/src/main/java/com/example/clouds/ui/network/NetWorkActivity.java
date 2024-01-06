package com.example.clouds.ui.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clouds.R;
import com.example.clouds.base.BaseActivity;
import com.example.clouds.databinding.ActivityNetworkBinding;
import com.example.clouds.entry.WifiConnectedEntry;
import com.example.clouds.listener.WifiStateChangeListener;
import com.example.clouds.receiver.NetWorkReceiver;
import com.example.clouds.ui.adapter.NetWorkConnectAdapter;
import com.example.clouds.ui.adapter.NetWorkSearchAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NetWorkActivity extends BaseActivity<ActivityNetworkBinding> implements View.OnClickListener, WifiStateChangeListener {

    private static final String TAG = "NetWorkActivity";
    private NetWorkReceiver netWorkReceiver;
    private NetWorkSearchAdapter mNetworkSearchAdapter;
    private NetWorkConnectAdapter mNetworkConnectAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mSearchLayoutManager;
    private NetWorkViewModel mViewModel;
    private boolean isNetworkSwitch;
    private Handler handler = new Handler(Looper.getMainLooper());
    private List<WifiConnectedEntry> mList = new ArrayList<WifiConnectedEntry>();
    private String ssid;

    @Override
    protected ActivityNetworkBinding getViewBinding() {
        return ActivityNetworkBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        //WiFi开关按钮广播WiFi状态
        netWorkReceiver = new NetWorkReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(netWorkReceiver, intentFilter);


        //扫描WiFi列表适配器
        mSearchLayoutManager = new LinearLayoutManager(this);
        mBinding.recyclerNetworkSearch.setLayoutManager(mSearchLayoutManager);
        mNetworkSearchAdapter = new NetWorkSearchAdapter(this, mViewModel);
        mBinding.recyclerNetworkSearch.setAdapter(mNetworkSearchAdapter);

        //让recyclerview不能滑动
        mLayoutManager = new LinearLayoutManager(this);
//        {
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        };
        //已保存wifi列表适配器
        mBinding.recyclerNetworkConnect.setLayoutManager(mLayoutManager);
        mNetworkConnectAdapter = new NetWorkConnectAdapter(mViewModel, this);
        mBinding.recyclerNetworkConnect.setAdapter(mNetworkConnectAdapter);
    }

    @Override
    protected void initListener() {
        mBinding.buttonBack.setOnClickListener(this);
        mBinding.networkButtonSwitch.setOnClickListener(this);

//        mViewModel.registerNetworkCallback(new ConnectivityManager.NetworkCallback() {
//            @Override
//            public void onAvailable(@NonNull @NotNull Network network) {
//                super.onAvailable(network);
//                Log.d(TAG, "onAvailable: " + network.toString());
//                runOnUiThread(() -> {
//                    if (mNetworkConnectAdapter != null) {
//                        mNetworkConnectAdapter.getWifiConnected(true);
//                    }
//                });
//            }
//
//            @Override
//            public void onLost(@NonNull @NotNull Network network) {
//                super.onLost(network);
//                Log.d(TAG, "onLost: " + network);
//                runOnUiThread(() -> {
//                    if (mNetworkConnectAdapter != null) {
//                        mNetworkConnectAdapter.getWifiConnected(false);
//                    }
//                });
//            }
//        });
    }

    @Override
    protected void initViewModel() {
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(NetWorkViewModel.class);
        mViewModel.checkPermissions(this);
        mViewModel.initConnect(this);
    }

    @Override
    protected void initData() {
        //当点击不同的wifi，操作：连接或断开连接，对适配器传入ssid，连接title做更新
        mViewModel.isWifiConnected.observe(this, values -> {
            mNetworkConnectAdapter.getWifiConnected(null);
        });

        //刷新扫描wifi
        mViewModel.scanResultList.observe(this, list -> {
            mNetworkSearchAdapter.setList(list);
        });


        //wifi总开关
        mViewModel.netWorkSwitch.observe(this, enable -> {
            isNetworkSwitch = enable;
            mBinding.networkButtonSwitch.setChecked(enable);
            showNetworkView(enable);
        });

        //已连接过的wifi配置信息列表
        mViewModel.wifiConfigList.observe(this, list -> {
            mList.clear();
            if (list != null) {
                for (WifiConfiguration wc : list) {
                    WifiConnectedEntry wifiConnectedEntry = new WifiConnectedEntry();
                    wifiConnectedEntry.setSSID(wc.SSID);
                    wifiConnectedEntry.setNetworkID(wc.networkId);
                    mList.add(wifiConnectedEntry);
                }
                mNetworkConnectAdapter.setListWifi(mList);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.setHandler();
    }

    @Override
    protected void loadData() {
        mViewModel.getStatus();
        mViewModel.getConnectionInfo();
        mViewModel.getConnected(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netWorkReceiver);
        mViewModel.removeCallbacks();
        mViewModel.unregisterNetworkCallback();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_back) {
            finish();
        } else if (v.getId() == R.id.network_button_switch) {
            showNetworkView(isNetworkSwitch);
            mViewModel.setWifiEnabled();
        }
    }

    @Override
    public void onWifiSwitchStatus(boolean enabled) {
        mViewModel.netWorkSwitch.setValue(enabled);
        Log.d(TAG, "开关状态。。。: onWifiStateChange = [" + enabled + "]");
    }

    @Override
    public void onWifiConnectSuccess(boolean enabled) {
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        // 更新状态或UI
        mNetworkConnectAdapter.getWifiConnected(ssid);
        mNetworkConnectAdapter.getWifiConnected(enabled);
        Log.d(TAG, "onReceive: ssid..." + ssid);
//        Log.d(TAG, "是否连接成功。。。: onWifiConnectSuccess = [" + enabled + "]");
    }

    @Override
    public void onWifiDisConnected(boolean enabled) {
//        Log.d(TAG, "是否断开连接。。。: onWifiDisConnected = [" + enabled + "]");
    }


    //根据wifi开关显示列表
    private void showNetworkView(boolean enabled) {
        mBinding.recyclerNetworkConnect.setVisibility(enabled ? View.VISIBLE : View.GONE);
        mBinding.recyclerNetworkSearch.setVisibility(enabled ? View.VISIBLE : View.GONE);
        mBinding.networkOtherTitle.setVisibility(enabled ? View.VISIBLE : View.GONE);
        mBinding.networkButtonRefresh.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }


}