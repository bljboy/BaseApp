package com.example.clouds.ui.locals;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.clouds.global.Request;

import java.io.File;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.clouds.global.Request.REQUEST_OPENFILE;


public class LocalsViewModel extends ViewModel {
    private String TAG = getClass().getName();
    private ContentResolver mContentResolver;
    private Application mApplication;
    public LocalsState state = new LocalsState();
    public MutableLiveData<LocalsState> getState = new MutableLiveData<>();


    public void initConnect(Application application) {
        mContentResolver = application.getContentResolver();
        this.mApplication = application;
    }

    public void searchFiles(File dir, String extension, List<File> fileList) {
        Log.d(TAG, "searchFiles dir: " + dir);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchFiles(file, extension, fileList); // 递归搜索
                } else {
                    if (file.getName().endsWith(extension)) {
                        fileList.add(file); // 添加符合条件的文件
                    }
                }
            }
            Log.d(TAG, "searchFiles: " + fileList.size());
        }
    }

    public void openFilePicker(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*"); // 根据需要设置MIME类型
        activity.startActivityForResult(intent, REQUEST_OPENFILE);
    }

}
