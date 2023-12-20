package com.example.clouds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.clouds.base.BaseActivity;
import com.example.clouds.databinding.ActivityMainBinding;
import com.example.clouds.ui.chat.ChatFragment;
import com.example.clouds.ui.home.HomeFragment;
import com.example.clouds.ui.person.PersonFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private final String TAG = "MainActivity";
    private List<Fragment> mFragmentList = new ArrayList<>();
    private ChatFragment chatFragment = new ChatFragment();
    private HomeFragment homeFragment = new HomeFragment();
    private PersonFragment personFragment = new PersonFragment();
    private Fragment fragment = null;

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        mFragmentList.add(homeFragment);
        mFragmentList.add(chatFragment);
        mFragmentList.add(personFragment);
    }

    @Override
    protected void initListener() {
        mBinding.bottomAppBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switchFragment(item.getItemId());
                return true;
            }
        });
    }

    @Override
    protected void initViewModel() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void loadData() {

    }

    private void switchFragment(int id) {
        switch (id) {
            case R.id.nav_home:
                fragment = mFragmentList.get(0);
                Log.d(TAG, "onNavigationItemSelected: " + "home");
                break;
            case R.id.nav_chat:
                fragment = mFragmentList.get(1);
                Log.d(TAG, "onNavigationItemSelected: " + "chat");
                break;
            case R.id.nav_person:
                fragment = mFragmentList.get(2);
                Log.d(TAG, "onNavigationItemSelected: " + "person");
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_current, fragment).commit();
        }
    }
}