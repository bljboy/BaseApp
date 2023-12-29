package com.example.clouds.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.example.clouds.listener.WifiStateChangeListener;

public class NetWorkReceiver extends BroadcastReceiver {
    private WifiStateChangeListener listener;

    public NetWorkReceiver(WifiStateChangeListener listener) {
        this.listener = listener;
    }

    /**
     * WiFi开关按钮广播WiFi状态
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
        }
    }
}
