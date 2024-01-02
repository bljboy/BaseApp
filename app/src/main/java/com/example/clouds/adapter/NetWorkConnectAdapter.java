package com.example.clouds.adapter;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
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

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NetWorkConnectAdapter extends RecyclerView.Adapter<NetWorkConnectAdapter.MyViewHolder> {

    private static final String TAG = "NetWorkConnectAdapter";
    private int expandedPosition = -1; // -1表示没有选中的item
    private List<WifiConfiguration> list;
    private NetWorkViewModel mViewModel;
    private Context mContext;
    private String mWifiName;

    public NetWorkConnectAdapter(NetWorkViewModel model, Context context) {
        this.mViewModel = model;
        this.mContext = context;
    }

    public void getisWifiConnected(String value) {
        this.mWifiName = value;
        Log.d(TAG, "getisWifiConnected: " + value);
    }

    public void setListWifi(List<WifiConfiguration> wifiEntityList) {
        Log.d(TAG, "setListWifi: " + wifiEntityList);
        this.list = wifiEntityList;
        notifyDataSetChanged();
        //自测代码，无需提交
//        for (WifiConfiguration configuration : wifiEntityList) {
//            ssid = configuration.SSID;
//            networkId = configuration.networkId;
//            Log.d(TAG, "setListWifi: " + "ssid:" + ssid);
//            Log.d(TAG, "setListWifi: " + "networkId:" + networkId);
//        }
    }

    @NonNull
    @NotNull
    @Override
    public NetWorkConnectAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_network_connect, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NetWorkConnectAdapter.MyViewHolder holder, int position) {
        //设置wifi昵称
        String ssid = list.get(position).SSID;
        holder.recycler_network_name.setText(ssid);
        Log.d(TAG, "onBindViewHolder: " + "ssid:" + ssid);
        boolean isConnected = ssid.equals(mWifiName);

        //设置已保存的wifi，是已连接还是未连接
        if (isConnected) {
            holder.recycler_network_title.setText(R.string.network_connect_title);
            holder.recycler_network_connect.setText(R.string.network_disconnect_details);
        } else {
            holder.recycler_network_title.setText(R.string.network_disconnect_title);
            holder.recycler_network_connect.setText(R.string.network_connect_details);
        }

        //移除已保存wifi
        holder.recycler_network_ignore.setOnClickListener(v -> {
            Log.d(TAG, "onBindViewHolder: " + "removeConnectWifi:" + list.get(position).networkId);
            mViewModel.removeConnectWifi(list.get(position).networkId,mContext);
        });

        //连接设备或断开设备按钮
        holder.recycler_network_connect.setOnClickListener(v -> {
            if (!isConnected) {
                mViewModel.setConnectWifi(list.get(position).networkId);
                mViewModel.isWifiConnected.postValue(list.get(position).SSID);
                Log.d(TAG, "onBindViewHolder: " + "setConnectWifi1:" + list.get(position).networkId);
            } else {
                mViewModel.setDisConnectWifi(list.get(position).networkId);
                mViewModel.isWifiConnected.postValue(list.get(position).SSID);
                Log.d(TAG, "onBindViewHolder: " + "setConnectWifi2:" + list.get(position).networkId);
            }
        });

        //设置wifi详情点击展开
        final boolean isExpanded = position == expandedPosition;
        holder.recycler_network_details.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.recycler_network_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedPosition = isExpanded ? -1 : position; // 切换展开的位置
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
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
