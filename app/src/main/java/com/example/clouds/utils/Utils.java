package com.example.clouds.utils;

public class Utils {
    public static String checkWifiNameQuotes(String name) {
        if (name.length() > 0) {
            return name.substring(1, name.length() - 1);
        }
        return name;
    }
}
