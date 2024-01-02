package com.example.clouds.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clouds.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NetWorkSearchAdapter extends RecyclerView.Adapter<NetWorkSearchAdapter.MyViewHolder> {
    private static final String TAG = "NetWorkSearchAdapter";
    private List<ScanResult> mlist;
    private Context mContext;

    public NetWorkSearchAdapter(Context context) {
        this.mContext = context;
        this.mlist = new ArrayList<>();
    }

    public void setList(List<ScanResult> list) {
        this.mlist.clear();
        if (list.size() > 0) {
            this.mlist = list;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public NetWorkSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_network_search, parent, false);
        return new NetWorkSearchAdapter.MyViewHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull @NotNull NetWorkSearchAdapter.MyViewHolder holder, int position) {
        //设置wifi昵称
        String ssid = mlist.get(position).SSID;
        int level = mlist.get(position).level;
        holder.recycler_network_name.setText(ssid);
        int signalLevel = WifiManager.calculateSignalLevel(level, 5);
        Log.d(TAG, "onBindViewHolder: " + "ssid:" + ssid);
        Log.d(TAG, "onBindViewHolder: " + "level:" + level);
        switch (signalLevel) {
            case 4:
                holder.recycler_network_icon.setBackground(mContext.getResources().getDrawable(R.drawable.set_wifi_icon_4));
                break;
            case 3:
                holder.recycler_network_icon.setBackground(mContext.getResources().getDrawable(R.drawable.set_wifi_icon_3));
                break;
            case 2:
                holder.recycler_network_icon.setBackground(mContext.getResources().getDrawable(R.drawable.set_wifi_icon_2));
                break;
            default:
                holder.recycler_network_icon.setBackground(mContext.getResources().getDrawable(R.drawable.set_wifi_icon_1));
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    // 提供对视图的引用
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recycler_network_name;
        public TextView recycler_network_icon;

        public MyViewHolder(View v) {
            super(v);
            recycler_network_name = v.findViewById(R.id.recycler_network_name);
            recycler_network_icon = v.findViewById(R.id.recycler_network_icon);
        }
    }
}
