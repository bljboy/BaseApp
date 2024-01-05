package com.example.clouds.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
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
import com.example.clouds.ui.network.NetWorkViewModel;
import com.example.clouds.utils.DialogNetWork;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NetWorkSearchAdapter extends RecyclerView.Adapter<NetWorkSearchAdapter.MyViewHolder> {
    private static final String TAG = "NetWorkSearchAdapter";
    private List<ScanResult> mlist;
    private Context mContext;
    private NetWorkViewModel mViewModel;

    public NetWorkSearchAdapter(Context context, NetWorkViewModel model) {
        this.mContext = context;
        this.mlist = new ArrayList<>();
        this.mViewModel = model;
    }

    public void setList(List<ScanResult> list) {
        Log.d(TAG, "获取扫描列表WiFi数量...setList = [" + list.size() + "]");
        this.mlist.clear();
        this.mlist = list;
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
        holder.recycler_network_name.setText(mlist.get(position).SSID);
        int signalLevel = WifiManager.calculateSignalLevel(mlist.get(position).level, 5);
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
        holder.recycler_network_list.setOnClickListener(v -> {
            ScanResult selectedWifi = mlist.get(position);
            // 检查是否加密
            if (isWifiEncrypted(selectedWifi)) {
                // WiFi被加密，弹出对话框要求输入密码
                showPasswordDialog(selectedWifi.SSID);
            } else {
                // WiFi未加密，直接连接
                mViewModel.connectToWifi(selectedWifi.SSID, null);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }


    // 提供对视图的引用
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recycler_network_name;
        public TextView recycler_network_icon;
        public ConstraintLayout recycler_network_list;

        public MyViewHolder(View v) {
            super(v);
            recycler_network_name = v.findViewById(R.id.recycler_network_name);
            recycler_network_icon = v.findViewById(R.id.recycler_network_icon);
            recycler_network_list = v.findViewById(R.id.recycler_network_list);
        }
    }

    private boolean isWifiEncrypted(ScanResult wifi) {
        return wifi.capabilities.contains("WEP") || wifi.capabilities.contains("PSK") || wifi.capabilities.contains("EAP");
    }

    private void showPasswordDialog(String ssid) {
        DialogNetWork.showFullScreenDialog(mContext, mViewModel, ssid);

    }
}
