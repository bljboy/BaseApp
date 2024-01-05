package com.example.clouds.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clouds.R;
import com.example.clouds.entry.WifiConnectedList;
import com.example.clouds.ui.network.NetWorkViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NetWorkConnectAdapter extends RecyclerView.Adapter<NetWorkConnectAdapter.MyViewHolder> {

    private static final String TAG = "NetWorkConnectAdapter";
    private int expandedPosition = -1; // -1表示没有选中的item
    private List<WifiConnectedList> mWifiConnectedList;
    private NetWorkViewModel mViewModel;
    private String mWifiName;

    public NetWorkConnectAdapter(NetWorkViewModel model, Context context) {
        this.mViewModel = model;
        this.mWifiConnectedList = new ArrayList<>();
    }

    public void getWifiConnected(String value) {
        this.mWifiName = value;
    }


    public void updateWifiConnectionStatus(String SSID, boolean isConnected) {
        if (mWifiConnectedList.size() > 0) {
            for (int i = 0; i < mWifiConnectedList.size(); i++) {
                WifiConnectedList item = mWifiConnectedList.get(i);
                if (item.getSSID().equals(SSID)) {
                    item.setConnected(isConnected);
                    notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    public void setListWifi(List<WifiConnectedList> list) {
        this.mWifiConnectedList = list;
        Log.d(TAG, "setlist: " + list.size());
    }

//    public void setListWifi(List<WifiConfiguration> wifiEntityList) {
//        Log.d(TAG, "setListWifi: " + wifiEntityList);
//        this.mlist = wifiEntityList;
//        notifyDataSetChanged();
//    }

    @NonNull
    @NotNull
    @Override
    public NetWorkConnectAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_network_connect, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NetWorkConnectAdapter.MyViewHolder holder, int position) {
        WifiConnectedList item = mWifiConnectedList.get(position);
        //设置wifi昵称
        String ssid = item.getSSID();
        holder.recycler_network_name.setText(ssid);
        Log.d(TAG, "onBindViewHolder: " + "ssid:" + ssid);
        boolean isConnected = ssid.equals(mWifiName);

        //设置已保存的wifi，是已连接还是未连接
        if (item.isConnected()) {
            holder.recycler_network_title.setText(R.string.network_connect_title);
            holder.recycler_network_connect.setText(R.string.network_disconnect_details);
        } else {
            holder.recycler_network_title.setText(R.string.network_disconnect_title);
            holder.recycler_network_connect.setText(R.string.network_connect_details);
        }

        //移除已保存wifi
        holder.recycler_network_ignore.setOnClickListener(v -> {
            Log.d(TAG, "onBindViewHolder: " + "removeConnectWifi:" + item.getNetworkID());
            mViewModel.removeConnectWifi(item.getNetworkID());
        });

        //连接设备或断开设备按钮
        holder.recycler_network_connect.setOnClickListener(v -> {
            if (!isConnected) {
                mViewModel.setConnectWifi(item.getNetworkID(), item.getSSID());
                Log.d(TAG, "onBindViewHolder: " + "setConnectWifi1:" + item.getNetworkID());
            } else {
                mViewModel.setDisConnectWifi(item.getNetworkID());
                Log.d(TAG, "onBindViewHolder: " + "setConnectWifi2:" + item.getNetworkID());
            }
            if (item.isConnected()) {
                holder.recycler_network_title.setText(R.string.network_connect_title);
                holder.recycler_network_connect.setText(R.string.network_disconnect_details);
            } else {
                holder.recycler_network_title.setText(R.string.network_disconnect_title);
                holder.recycler_network_connect.setText(R.string.network_connect_details);
            }
        });

        //设置wifi详情点击展开
        final boolean isExpanded = position == expandedPosition;
        holder.recycler_network_details.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.recycler_network_list.setOnClickListener(v -> {
            expandedPosition = isExpanded ? -1 : position; // 切换展开的位置
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mWifiConnectedList.size();
    }


    // 提供对视图的引用
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout recycler_network_details;
        public ConstraintLayout recycler_network_list;
        public TextView recycler_network_name;
        public TextView recycler_network_title;
        public TextView recycler_network_connect;
        public TextView recycler_network_ignore;

        public MyViewHolder(View v) {
            super(v);
            recycler_network_list = v.findViewById(R.id.recycler_network_list);
            recycler_network_details = v.findViewById(R.id.recycler_network_details);
            recycler_network_name = v.findViewById(R.id.recycler_network_name);
            recycler_network_title = v.findViewById(R.id.recycler_network_title);
            recycler_network_connect = v.findViewById(R.id.recycler_network_connect);
            recycler_network_ignore = v.findViewById(R.id.recycler_network_ignore);

        }
    }

}
