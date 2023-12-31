package com.example.clouds.ui.time;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.example.clouds.R;
import com.example.clouds.base.BaseActivity;
import com.example.clouds.databinding.ActivityTimeBinding;
import com.example.clouds.utils.DateTimeUtils;

public class TimeActivity extends BaseActivity<ActivityTimeBinding> implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "TimeActivity";
    private String[] mAmPmArrays = null;
    private String[] mTime12_24Arrays = null;
    private int INDEX_DAY = 0;
    private int INDEX_MONTH = 0;
    private int INDEX_YEAR = 0;
    private int INDEX_HOUR = 0;
    private int INDEX_MINUTES = 0;
    private int INDEX_AMPM = 0;
    private boolean is24Hour;
    private TimeViewModel mViewModel;

    @Override
    protected ActivityTimeBinding getViewBinding() {
        return ActivityTimeBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        mAmPmArrays = getResources().getStringArray(R.array.half_day);
        mTime12_24Arrays = getResources().getStringArray(R.array.time_12_24);
    }

    @Override
    protected void initListener() {
        mBinding.buttonBack.setOnClickListener(this);
        mBinding.timeRg.setOnCheckedChangeListener(this);
        mBinding.timeUp.setOnClickListener(this);
        mBinding.timeDown.setOnClickListener(this);
        mBinding.timeAmPm.setOnClickListener(this);
        mBinding.time1224.setOnClickListener(this);
        mBinding.buttonOk.setOnClickListener(this);
    }

    @Override
    protected void initViewModel() {
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TimeViewModel.class);
        mViewModel.initConnet(this);
    }

    @Override
    protected void initData() {
        mViewModel.AMorPM.observe(this, value -> {
            INDEX_AMPM = value;
            Log.d(TAG, "initData: " + "AMorPM:" + value);
            mBinding.timeAmPm.post(() -> {
                mBinding.timeAmPm.setText(mAmPmArrays[value]);
            });
        });
        mViewModel.is24Hour.observe(this, value -> {
            is24Hour = value;
            Log.d(TAG, "initData: " + "is24Hour:" + is24Hour);
            mBinding.time1224.post(() -> {
                mBinding.time1224.setText(value ? mTime12_24Arrays[1] : mTime12_24Arrays[0]);
            });
        });
        mViewModel.year.observe(this, value -> {
            INDEX_YEAR = value;
            Log.d(TAG, "initData: " + "year:" + INDEX_YEAR);
            mBinding.timeYear.post(() -> {
                mBinding.timeYear.setText(DateTimeUtils.YearFormat(value));
            });
        });
        mViewModel.month.observe(this, value -> {
            INDEX_MONTH = value;
            Log.d(TAG, "initData: " + "month:" + INDEX_MONTH);
            mBinding.timeMonth.post(() -> {
                mBinding.timeMonth.setText(DateTimeUtils.DateFormat(value));
            });
        });
        mViewModel.day.observe(this, value -> {
            INDEX_DAY = value;
            Log.d(TAG, "initData: " + "day:" + INDEX_DAY);
            mBinding.timeDay.post(() -> {
                mBinding.timeDay.setText(DateTimeUtils.DateFormat(value));
            });
        });
        mViewModel.hour.observe(this, value -> {
            INDEX_HOUR = value;
            Log.d(TAG, "initData: " + "hour:" + INDEX_HOUR);
            mBinding.timeHour.post(() -> {
                mBinding.timeHour.setText(DateTimeUtils.DateFormat(value));
            });
        });
        mViewModel.minute.observe(this, value -> {
            INDEX_MINUTES = value;
            Log.d(TAG, "initData: " + "minute:" + INDEX_MINUTES);
            mBinding.timeMinutes.post(() -> {
                mBinding.timeMinutes.setText(DateTimeUtils.DateFormat(value));
            });
        });

    }

    @Override
    protected void loadData() {
        mViewModel.getStatus();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.time_day:
                mBinding.timeDay.setChecked(true);
                break;
            case R.id.time_month:
                mBinding.timeMonth.setChecked(true);
                break;
            case R.id.time_year:
                mBinding.timeYear.setChecked(true);
                break;
            case R.id.time_hour:
                mBinding.timeHour.setChecked(true);
                break;
            case R.id.time_minutes:
                mBinding.timeMinutes.setChecked(true);
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.time_up:
                setTimeUp();
                break;
            case R.id.time_down:
                setTimeDown();
                break;
            case R.id.time_am_pm:
                setTimeAmPm();
                break;
            case R.id.time_12_24:
                setmTime12_24();
                break;
            case R.id.button_ok:
                setAllOk();
                break;
        }
    }

    private void setAllOk() {
        int yearText = Integer.parseInt(mBinding.timeYear.getText().toString().trim());
        int monthText = Integer.parseInt(mBinding.timeMonth.getText().toString().trim());
        int timeDayText = Integer.parseInt(mBinding.timeDay.getText().toString().trim());
        int timeHourText = Integer.parseInt(mBinding.timeHour.getText().toString().trim());
        int timeMinutesText = Integer.parseInt(mBinding.timeMinutes.getText().toString().trim());
        String AMorPM = mBinding.timeAmPm.getText().toString().trim();
        String is12_24 = mBinding.time1224.getText().toString().trim();
        boolean flag = is12_24.equals(getString(R.string.string_24hour));
        int time12Hour = DateTimeUtils.set12Hour(is12_24, AMorPM, this, timeHourText);
        if (is12_24.equals(getString(R.string.string_12hour))) {
            timeHourText = time12Hour;
        }
        mViewModel.setSystemDateTime(yearText, monthText, timeDayText, timeHourText, timeMinutesText, flag);
//        mViewModel.setDate(yearText, monthText, timeDayText,timeHourText,timeMinutesText);

    }

    //设置12或24小时时间格式
    private void setmTime12_24() {
        is24Hour = !is24Hour;
        mBinding.time1224.setText(is24Hour ? getString(R.string.string_24hour) : getString(R.string.string_12hour));
        int timeHourText = Integer.parseInt(mBinding.timeHour.getText().toString().trim());
        String isAMorPM = mBinding.timeAmPm.getText().toString();
        int i = DateTimeUtils.updateHourDisplay(is24Hour, timeHourText, isAMorPM, this);
        INDEX_HOUR = i;
        mBinding.timeHour.setText(DateTimeUtils.DateFormat(i));
    }

    //设置上午或下午
    private void setTimeAmPm() {
        if (!is24Hour) {
            INDEX_AMPM = 1 - INDEX_AMPM;
            mBinding.timeAmPm.setText(INDEX_AMPM == 1 ? getString(R.string.string_pm) : getString(R.string.string_am));
        }
    }

    //设置日期
    private void setTimeUp() {
        if (mBinding.timeDay.isChecked()) {
            if (INDEX_DAY >= 1 && INDEX_DAY <= 31)
                mBinding.timeDay.setText(INDEX_DAY == 31 ? DateTimeUtils.DateFormat(INDEX_DAY = 1)
                        : DateTimeUtils.DateFormat(INDEX_DAY++));
        } else if (mBinding.timeMonth.isChecked()) {
            if (INDEX_MONTH >= 1 && INDEX_MONTH <= 12)
                mBinding.timeMonth.setText(INDEX_MONTH == 12 ? DateTimeUtils.DateFormat(INDEX_MONTH = 1)
                        : DateTimeUtils.DateFormat(INDEX_MONTH++));
        } else if (mBinding.timeYear.isChecked()) {
            if (INDEX_YEAR >= 2008 && INDEX_YEAR <= 2050)
                mBinding.timeYear.setText(DateTimeUtils.DateFormat(INDEX_YEAR++));
        } else if (mBinding.timeHour.isChecked()) {
            Log.d(TAG, "setTimeUp: INDEX_HOUR" + INDEX_HOUR);
            if (is24Hour) {
                if (INDEX_HOUR >= 0 && INDEX_HOUR <= 24)
                    mBinding.timeHour.setText(INDEX_HOUR == 24 ? DateTimeUtils.DateFormat(INDEX_HOUR = 0)
                            : DateTimeUtils.DateFormat(INDEX_HOUR++));
            } else {
                INDEX_HOUR = INDEX_HOUR % 12 + 1;
                mBinding.timeHour.setText(DateTimeUtils.DateFormat(INDEX_HOUR));
            }
        } else if (mBinding.timeMinutes.isChecked()) {
            if (INDEX_MINUTES >= 0 && INDEX_MINUTES <= 60)
                mBinding.timeMinutes.setText(INDEX_MINUTES == 60 ? DateTimeUtils.DateFormat(INDEX_MINUTES = 0)
                        : DateTimeUtils.DateFormat(INDEX_MINUTES++));
        }
    }

    private void setTimeDown() {
        if (mBinding.timeDay.isChecked()) {
            if (INDEX_DAY >= 1)
                mBinding.timeDay.setText(INDEX_DAY == 1 ? DateTimeUtils.DateFormat(INDEX_DAY = 31) : DateTimeUtils.DateFormat(--INDEX_DAY));
        } else if (mBinding.timeMonth.isChecked()) {
            if (INDEX_MONTH >= 1)
                mBinding.timeMonth.setText(INDEX_MONTH == 1 ? DateTimeUtils.DateFormat(INDEX_MONTH = 12)
                        : DateTimeUtils.DateFormat(--INDEX_MONTH));
        } else if (mBinding.timeYear.isChecked()) {
            if (INDEX_YEAR > 2008)
                mBinding.timeYear.setText(DateTimeUtils.DateFormat(--INDEX_YEAR));
        } else if (mBinding.timeHour.isChecked()) {
            if (is24Hour) {
                if (INDEX_HOUR >= 0)
                    mBinding.timeHour.setText(INDEX_HOUR == 0 ? DateTimeUtils.DateFormat(INDEX_HOUR = 23)
                            : DateTimeUtils.DateFormat(--INDEX_HOUR));
            } else {
                mBinding.timeHour.setText(INDEX_HOUR == 1 ? DateTimeUtils.DateFormat(INDEX_HOUR = 12)
                        : DateTimeUtils.DateFormat(--INDEX_HOUR));
            }
        } else if (mBinding.timeMinutes.isChecked()) {
            if (INDEX_MINUTES >= 0)
                mBinding.timeMinutes.setText(INDEX_MINUTES == 0 ? DateTimeUtils.DateFormat(INDEX_MINUTES = 60)
                        : DateTimeUtils.DateFormat(--INDEX_MINUTES));
        }
    }

}