package com.example.clouds.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.clouds.R;
import com.example.clouds.ui.network.NetWorkViewModel;

public class DialogNetWork {
    private String TAG = "DialogNetWork";

    public static void showFullScreenDialog(Context context, NetWorkViewModel model, String ssid) {
        Dialog fullScreenDialog = new Dialog(context);
        fullScreenDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 去掉标题栏
        fullScreenDialog.setContentView(R.layout.dialog_network_edittext); // 设置你自己的layout

        // 设置Dialog的Window参数，全屏显示但不隐藏状态栏
        Window window = fullScreenDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 透明背景
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT); // 宽高填满
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); // 清除全屏标志
            // 调整窗口的布局设置，以保证内容全屏显示但不覆盖状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        EditText editTextPassword = fullScreenDialog.findViewById(R.id.dialog_network_password);
        Button buttonConfirm = fullScreenDialog.findViewById(R.id.dialog_network_confirm);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理确认按钮点击事件
                String password = editTextPassword.getText().toString();
                // TODO: 根据您的逻辑验证密码
                model.connectToWifi(ssid, password);
                fullScreenDialog.dismiss(); // 关闭对话框
            }
        });
        fullScreenDialog.show();
    }
}
