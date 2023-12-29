package com.example.clouds.entry;

public class WifiEntry {
    String bssid;
    String name;
    boolean isConnected;
    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }



    public WifiEntry(String bssid, String name,boolean isConnected) {
        this.bssid = bssid;
        this.name = name;
        this.isConnected = isConnected;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
