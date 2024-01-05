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
import com.example.clouds.entry.WifiConnectedEntry;
import com.example.clouds.ui.network.NetWorkViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NetWorkConnectAdapter extends RecyclerView.Adapter<NetWorkConnectAdapter.MyViewHolder> {

    private static final String TAG = "NetWorkConnectAdapter";
    private int expandedPosition = -1; // -1表示没有选中的item
    private List<WifiConnectedEntry> mWifiConnectedEntry;
    private NetWorkViewModel mViewModel;
    private String mWifiName;
    private final Context mContext;
    private boolean mConnected;
    private boolean isExpanded;

    public NetWorkConnectAdapter(NetWorkViewModel model, Context context) {
        this.mViewModel = model;
        this.mWifiConnectedEntry = new ArrayList<>();
        this.mContext = context;
    }

    public void getWifiConnected(String value) {
        this.mWifiName = value;
        this.notifyDataSetChanged();
    }

    public void getWifiConnected(boolean isConnected) {
        Log.d(TAG, "getWifiConnected: isConnected: " + isConnected);
        this.mConnected = isConnected;
        this.notifyDataSetChanged();
    }

    public void setListWifi(List<WifiConnectedEntry> list) {
        this.mWifiConnectedEntry = list;
        Log.d(TAG, "setlist: " + list.size());
        this.notifyDataSetChanged();
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
        WifiConnectedEntry item = mWifiConnectedEntry.get(position);
        //设置wifi昵称
        String ssid = item.getSSID();
        holder.recycler_network_name.setText(ssid);
        boolean isConnected = ssid.equals(mWifiName);
        Log.d(TAG, "onBindViewHolder: mConnected..." + mConnected);
        Log.d(TAG, "onBindViewHolder: isConnected..." + isConnected);
        //设置已保存的wifi，是已连接还是未连接
        if (isConnected && mConnected) {
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
            setDetailVisble(position);
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
            //隐藏wifi详情
            setDetailVisble(position);
        });

        //设置wifi详情点击展开
        holder.recycler_network_details.setVisibility(position == expandedPosition ? View.VISIBLE : View.GONE);
        holder.recycler_network_list.setOnClickListener(v -> {
            expandedPosition = position == expandedPosition ? -1 : position; // 切换展开的位置
            notifyDataSetChanged();
        });
    }

    public void setDetailVisble(int position) {
        //隐藏wifi详情
        if (expandedPosition == position) {
            expandedPosition = -1;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mWifiConnectedEntry.size();
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
