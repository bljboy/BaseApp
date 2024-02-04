package com.example.clouds.ui.locals;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clouds.R;
import com.example.clouds.base.BaseFragment;
import com.example.clouds.databinding.FragmentLocalsBinding;
import com.example.clouds.global.MusicType;
import com.example.clouds.global.Request;
import com.example.clouds.ui.adapter.LocalsAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocalsFragment extends BaseFragment<FragmentLocalsBinding> {
    private final String TAG = getClass().getName();
    private LocalsAdapter mLocalsAdapter;
    private LocalsViewModel model;
    private List<File> foundFiles = new ArrayList<>();

    @Override
    protected void initView() {
        mLocalsAdapter = new LocalsAdapter();
        mBinding.recyclerviewLocals.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerviewLocals.setAdapter(mLocalsAdapter);
    }

    @Override
    protected void initViewModel() {
        model = new ViewModelProvider(this).get(LocalsViewModel.class);
        Request.RequestRead(getActivity());
        model.initConnect(getActivity().getApplication());
    }

    @Override
    protected void initListener() {

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void loadData() {
//        model.scanMusicFiles();
        model.openFilePicker(getActivity());
        model.searchFiles(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), MusicType.FLAC, foundFiles);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected FragmentLocalsBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentLocalsBinding.inflate(inflater, container, false);
    }

}