package com.example.clouds.ui.network;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.clouds.utils.Utils;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.clouds.constants.NetWorkConstants.REQUEST_CODE;
import static com.example.clouds.constants.NetWorkConstants.REQUEST_CODE_BACKGROUND_LOCATION;
import static com.example.clouds.constants.NetWorkConstants.REQUEST_CODE_FINE_LOCATION;

public class NetWorkViewModel extends ViewModel {
    private static final String TAG = "NetWorkViewModel";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final long SCAN_INTERVAL_30S = 10000; // 10秒
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private NetworkInfo networkInfo;
    private ConnectivityManager mConnectivityManager;
    private ConnectivityManager.NetworkCallback mNetworkCallback;
    public MutableLiveData<Boolean> netWorkSwitch = new MutableLiveData<>();//WiFi总开关
    public MutableLiveData<List<WifiConfiguration>> wifiConfigList = new MutableLiveData<>();//WiFi配置
    public MutableLiveData<String> isWifiConnected = new MutableLiveData<>();//已连接wifi昵称
    public MutableLiveData<Boolean> isWifiConnectSuccess = new MutableLiveData<>();//连接成功
    public MutableLiveData<Boolean> isWifiDisConnected = new MutableLiveData<>();//断开连接
    public MutableLiveData<Boolean> isWifiRemove = new MutableLiveData<>();//移除配置
    public MutableLiveData<List<ScanResult>> scanResultList = new MutableLiveData<>();//附近热点

    public void initConnect(Context context) {
        if (mWifiManager == null) {
            mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }
        if (mWifiInfo == null) {
            mWifiInfo = mWifiManager.getConnectionInfo();
        }
        if (mConnectivityManager == null) {
            mConnectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        }
    }

    public void getStatus() {
        //获取wifi开关初始状态
        boolean wifiEnabled = mWifiManager.isWifiEnabled();
        netWorkSwitch.setValue(wifiEnabled);
    }

    private final Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {
            if (mWifiManager != null && mWifiManager.isWifiEnabled()) {
                startScanWifi();
                getScanResult();
            }
            handler.postDelayed(this, SCAN_INTERVAL_30S);
        }
    };

    //10s定时扫描一次wifi
    public void setHandler() {
        handler.post(scanRunnable);
    }

    public void removeCallbacks() {
        handler.removeCallbacks(scanRunnable);
    }

    /**
     * 输入密码。连接附近wifi热点
     */
    public void connectToWifi(String ssid, String password) {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", ssid);

        if (password != null && !password.equals("")) {
            // WPA/WPA2 PSK WiFi
            wifiConfig.preSharedKey = String.format("\"%s\"", password);
        } else {
            // Open WiFi (无需密码)
            isWifiConnected.setValue(null);
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        // 添加新的WiFi配置
        int netId = mWifiManager.addNetwork(wifiConfig);
        mWifiManager.disconnect();
        mWifiManager.enableNetwork(netId, true);
        mWifiManager.reconnect();
        isWifiConnected.setValue(null);
        refreshWifiList();
    }

    /**
     * 获取附近WiFi热点
     */
    @SuppressLint("MissingPermission")
    public void getScanResult() {
        if (mWifiManager != null) {
            List<ScanResult> originalScanResults = mWifiManager.getScanResults();
            Set<String> configuredSSIDs = new HashSet<>();

            // 获取已保存网络的SSID，并存储在HashSet以快速查找
            List<WifiConfiguration> configuredNetworks = mWifiManager.getConfiguredNetworks();
            if (configuredNetworks != null) {
                for (WifiConfiguration config : configuredNetworks) {
                    configuredSSIDs.add(Utils.trimQuotes(config.SSID));
                }
            }
            // 使用HashSet来存储唯一的SSID
            Set<String> uniqueSSIDs = new HashSet<>();
            List<ScanResult> uniqueScanResults = new ArrayList<>();
            for (ScanResult result : originalScanResults) {
                String ssid = result.SSID;
                if (!uniqueSSIDs.contains(ssid) && !configuredSSIDs.contains(ssid)) {
                    uniqueSSIDs.add(ssid);
                    uniqueScanResults.add(result);
                }
            }
            // 更新LiveData或者进行其他UI操作
            scanResultList.setValue(uniqueScanResults);
        }
    }


    /**
     * 开始扫描 WIFI.
     */
    public void startScanWifi() {
        Log.d(TAG, "开始扫描 WIFI.。。。...startScanWifi = 调用");
        if (mWifiManager != null) {
            mWifiManager.startScan();
        }
    }

    /**
     * 移除已保存wifi
     */
    public void removeConnectWifi(int networkId) {
        boolean removeNetwork = mWifiManager.removeNetwork(networkId);
        boolean saveConfiguration = mWifiManager.saveConfiguration();
        isWifiRemove.setValue(removeNetwork);
        Log.d(TAG, "移除已保存wifi: removeConnectWifi = [" + removeNetwork + "]");
        if (removeNetwork && saveConfiguration) {
            refreshWifiList();
        }

    }

    private void refreshWifiList() {
        // 更新Wi-Fi列表显示
        @SuppressLint("MissingPermission") List<WifiConfiguration> wifiConfigurationList = mWifiManager.getConfiguredNetworks();
        wifiConfigList.setValue(wifiConfigurationList);
    }

    /**
     * 断开连接WiFi
     *
     * @param value
     */

    public void setDisConnectWifi(int value) {
        isWifiConnected.setValue(null);
        mWifiManager.disableNetwork(value);
        mWifiManager.disconnect();
        Log.d(TAG, "断开连接WiFi: setDisConnectWifi = [" + value + "]");
    }

    /**
     *
     */

    /**
     * 连接设备WiFi
     *
     * @param value
     */
    public void setConnectWifi(int value, String ssid) {
        isWifiConnected.setValue(null);
        mWifiManager.enableNetwork(value, true);
        Log.d(TAG, "setConnectWifi: " + "name" + ssid);
    }

    /**
     * 当前连接WiFi
     *
     * @return
     */
    public void getConnected(Context context) {
        // 在有权限时重新获取
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mWifiInfo = mWifiManager.getConnectionInfo();
            networkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                String ssid = mWifiInfo.getSSID();
                isWifiConnected.setValue(ssid);
                Log.d(TAG, "当前连接WiFi: getConnected1 = [" + ssid + "]");
            }
        }
    }

    /**
     * 设置wifi开关总状态
     *
     * @param enabled
     */
    public void setWifiEnabled(boolean enabled) {
        Log.d(TAG, "设置wifi开关总状态: enable = [" + enabled + "]");
        mWifiManager.setWifiEnabled(enabled);
        isWifiConnected.setValue(null);
        getStatus();
    }

    /**
     * 获取已连接过的wifi数组
     *
     * @param
     */
    public void getConnectionInfo() {
        // 检查权限
        @SuppressLint("MissingPermission") List<WifiConfiguration> wifiConfigurationList = mWifiManager.getConfiguredNetworks();
        wifiConfigList.setValue(wifiConfigurationList);
        Log.d(TAG, "获取已连接过的wifi数组: wifiConfigurationList = [" + wifiConfigurationList + "]");
    }

    /**
     * 动态请求权限
     *
     * @param context
     */
    public void checkPermissions(Context context) {
        //动态请求位置权限：
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_FINE_LOCATION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //对于 API 级别 29 或更高的设备，请求后台位置权限：
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_BACKGROUND_LOCATION);
        } else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            // 如果没有权限，向用户请求权限
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE},
                    REQUEST_CODE);
        }
    }

    public void registerNetworkCallback(ConnectivityManager.NetworkCallback callback) {
        this.mNetworkCallback = callback;
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        mConnectivityManager.registerNetworkCallback(builder.build(), mNetworkCallback);

    }

    public void unregisterNetworkCallback() {
        if (mNetworkCallback == null) return;
        mConnectivityManager.unregisterNetworkCallback(mNetworkCallback);
        mNetworkCallback = null;
    }
}
