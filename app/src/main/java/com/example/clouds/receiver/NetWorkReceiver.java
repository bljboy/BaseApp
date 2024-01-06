package com.example.clouds.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.clouds.listener.WifiStateChangeListener;

public class NetWorkReceiver extends BroadcastReceiver {
    private String TAG = "NetWorkReceiver";
    private WifiStateChangeListener listener;

    public NetWorkReceiver(WifiStateChangeListener listener) {
        this.listener = listener;
    }

    /**
     * WiFi开关按钮广播WiFi状态
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            if (listener != null) {
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_ENABLED:
                        listener.onWifiStateChange(true);
                        break;
                    case WifiManager.WIFI_STATE_DISABLED:
                        listener.onWifiStateChange(false);
                        break;
                }
            }
        } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info != null && info.isConnected()) {
                // WiFi已连接
                listener.onWifiConnectSuccess(true);
            } else if (info != null && info.getState() == NetworkInfo.State.DISCONNECTED) {
                // WiFi已断开
            }
        } else if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            SupplicantState supplicantState = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
            if (supplicantState == SupplicantState.COMPLETED) {
                // 连接成功，检查是否真的连接到了指定的 WiFi
                Log.d(TAG, "onReceive: 密码ok...");
            } else if (supplicantState == SupplicantState.DISCONNECTED) {
                // 连接失败或断开连接
                int error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
                if (error == WifiManager.ERROR_AUTHENTICATING) {
                    // 这里可以处理密码错误的情况
                    // 可以通过回调或其他方式通知 UI 层密码错误
                    Log.d(TAG, "onReceive: 密码错误...");
                }
            }
        }
    }
}
