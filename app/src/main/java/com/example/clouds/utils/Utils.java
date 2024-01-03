package com.example.clouds.utils;

public class Utils {
    public static String checkWifiNameQuotes(String name) {
        if (name.length() > 0) {
            return name.substring(1, name.length() - 1);
        }
        return name;
    }
    /**
     * 去除字符串首尾的引号
     */
    public static String trimQuotes(String str) {
        if (str != null) {
            str = str.replaceAll("^\"|\"$", "");
        }
        return str;
    }
}
