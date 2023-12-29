package com.example.clouds.ui.network;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.clouds.entry.WifiEntry;

import java.util.List;

import static com.example.clouds.constants.NetWorkConstants.REQUEST_CODE;
import static com.example.clouds.constants.NetWorkConstants.REQUEST_CODE1;
import static com.example.clouds.constants.NetWorkConstants.REQUEST_CODE_BACKGROUND_LOCATION;
import static com.example.clouds.constants.NetWorkConstants.REQUEST_CODE_FINE_LOCATION;

public class NetWorkViewModel extends ViewModel {
    private static final String TAG = "NetWorkViewModel";
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private NetworkInfo networkInfo;
    private ConnectivityManager mConnectivityManager;
    public MutableLiveData<Boolean> netWorkSwitch = new MutableLiveData<>();
    public MutableLiveData<List<WifiConfiguration>> wifiConfigList = new MutableLiveData<>();
    public MutableLiveData<String> isWifiConnected = new MutableLiveData<>();
    public MutableLiveData<Boolean> isWifiConnectSuccess = new MutableLiveData<>();
    public MutableLiveData<Boolean> isWifiDisConnected = new MutableLiveData<>();
    public MutableLiveData<Boolean> isWifiRemove = new MutableLiveData<>();
    private Context mContext;

    public void initConnect(Context context) {
        this.mContext = context;
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

    /**
     * 移除已保存wifi
     */

    public void removeConnectWifi(int value) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CHANGE_WIFI_STATE}, REQUEST_CODE1);
            boolean removeNetwork = mWifiManager.removeNetwork(value);
            mWifiManager.saveConfiguration();
            isWifiRemove.setValue(removeNetwork);
            Log.d(TAG, "移除已保存wifi: removeConnectWifi = [" + value + "]");
            Log.d(TAG, "移除已保存wifi: removeConnectWifi removeNetwork= [" + removeNetwork + "]");
        }

    }

    /**
     * 断开连接WiFi
     *
     * @param value
     */

    public void setDisConnectWifi(int value) {
        mWifiManager.disableNetwork(value);
        mWifiManager.disconnect();
        Log.d(TAG, "断开连接WiFi: setDisConnectWifi = [" + value + "]");
    }

    /**
     * 连接设备WiFi
     *
     * @param value
     */
    public void setConnectWifi(int value) {
        mWifiManager.enableNetwork(value, true);
        networkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String ssid = mWifiInfo.getSSID();
            isWifiConnected.setValue(ssid);
            Log.d(TAG, "当前连接WiFi: getConnected1 = [" + ssid + "]");
        }
        Log.d(TAG, "设置连接WiFi: setConnectWifi = [" + value + "]");
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
        getStatus();
    }

    /**
     * 获取已连接过的wifi数组
     *
     * @param context
     */
    public void getConnectionInfo(Context context) {
        // 检查权限
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            // 如果没有权限，向用户请求权限
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE},
                    REQUEST_CODE);
        }
        List<WifiConfiguration> wifiConfigurationList = mWifiManager.getConfiguredNetworks();
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
        }
        //对于 API 级别 29 或更高的设备，请求后台位置权限：
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_BACKGROUND_LOCATION);
        }
    }
}
