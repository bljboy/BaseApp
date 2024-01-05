package com.example.clouds.entry;

public class WifiConnectedEntry {
    private String SSID;

    public int getNetworkID() {
        return NetworkID;
    }

    public void setNetworkID(int networkID) {
        NetworkID = networkID;
    }

    private int NetworkID;
    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    private boolean isConnected;
}
