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
import com.example.clouds.utils.DialogNetWork;

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
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(netWorkReceiver, intentFilter);

        //扫描WiFi列表适配器
        mSearchLayoutManager = new LinearLayoutManager(this);
        mBinding.recyclerNetworkSearch.setLayoutManager(mSearchLayoutManager);
        mNetworkSearchAdapter = new NetWorkSearchAdapter(this, mViewModel);
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
        mViewModel.isWifiDisConnected.observe(this, values -> {
            mBinding.recyclerNetworkConnect.post(() -> {
                mNetworkConnectAdapter.notifyDataSetChanged();
            });
        });

        //刷新扫描wifi
        mViewModel.scanResultList.observe(this, list -> {
            mNetworkSearchAdapter.setList(list);
        });

        //正在连接中
        mViewModel.isWifiConnectSuccess.observe(this, values -> {
            if (values) {
                mNetworkConnectAdapter.notifyDataSetChanged();
            }
        });
        //当前连接WiFi
        mViewModel.isWifiConnected.observe(this, values -> {
            mNetworkConnectAdapter.getWifiConnected(values);
        });

        //wifi总开关
        mViewModel.netWorkSwitch.observe(this, enable -> {
            isNetworkSwitch = enable;
            mBinding.networkButtonSwitch.setChecked(enable);
            showNetworkView(enable);
        });

        //已连接过的wifi配置信息列表
        mViewModel.wifiConfigList.observe(this, list -> {
            if (list != null) {
                mNetworkConnectAdapter.setListWifi(list);
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
        Log.d(TAG, "开关状态。。。: onWifiStateChange = [" + enabled + "]");
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