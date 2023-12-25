package com.example.clouds.ui.network;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NetWorkViewModel extends ViewModel {

    public MutableLiveData<Boolean> netWorkSwitch = new MutableLiveData<>();


    public void getStatus() {
        netWorkSwitch.setValue(true);
    }
}
