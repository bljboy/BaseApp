package com.example.clouds.listener;

public interface WifiStateChangeListener {
    void onWifiStateChange(boolean enabled);

    void onWifiConnectSuccess(boolean enabled);

    void onWifiDisConnected(boolean enabled);

    void onWifiRemove(boolean enabled);
}
