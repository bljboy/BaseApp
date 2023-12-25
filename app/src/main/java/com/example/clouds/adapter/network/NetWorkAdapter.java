package com.example.clouds.adapter.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clouds.R;

import org.jetbrains.annotations.NotNull;

public class NetWorkAdapter extends RecyclerView.Adapter<NetWorkAdapter.NetWorkViewHolder> {

    private int expandedPosition = -1; // -1表示没有选中的item

    @NonNull
    @NotNull
    @Override
    public NetWorkAdapter.NetWorkViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_network_connect, parent, false);
        return new NetWorkViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NetWorkAdapter.NetWorkViewHolder holder, int position) {
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
        return 10;
    }


    // 提供对视图的引用
    public static class NetWorkViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout recycler_network_details;
        public ConstraintLayout recycler_network_list;

        public NetWorkViewHolder(View v) {
            super(v);
            recycler_network_list = v.findViewById(R.id.recycler_network_list);
            recycler_network_details = v.findViewById(R.id.recycler_network_details);

        }
    }
}
