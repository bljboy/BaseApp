package com.example.clouds.listener;

public interface WifiStateChangeListener {
    void onWifiSwitchStatus(boolean enabled);

    void onWifiConnectSuccess(boolean enabled);

    void onWifiDisConnected(boolean enabled);
//    void onWifiScanResult(boolean enabled);
}
