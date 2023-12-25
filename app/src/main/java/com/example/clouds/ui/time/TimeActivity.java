package com.example.clouds.ui.time;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RadioGroup;

import com.example.clouds.R;
import com.example.clouds.base.BaseActivity;
import com.example.clouds.databinding.ActivityTimeBinding;

public class TimeActivity extends BaseActivity<ActivityTimeBinding> implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private String[] mDayArrays = null;
    private String[] mHour12Arrays = null;
    private String[] mHour24Arrays = null;
    private String[] mMinuteArrays = null;
    private String[] mMonthArrays = null;
    private String[] mYearArrays = null;
    private String[] mAmPmArrays = null;
    private String[] mTime12_24Arrays = null;
    private int INDEX_DAY = 0;
    private int INDEX_MONTH = 0;
    private int INDEX_YEAR = 0;
    private int INDEX_HOUR = 0;
    private int INDEX_MINUTES = 0;
    private int INDEX_AMPM = 0;
    private int INDEX_12_24 = 0;

    @Override
    protected ActivityTimeBinding getViewBinding() {
        return ActivityTimeBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        mDayArrays = getResources().getStringArray(R.array.day_time);
        mHour12Arrays = getResources().getStringArray(R.array.hour_time12);
        mHour24Arrays = getResources().getStringArray(R.array.hour_time24);
        mMinuteArrays = getResources().getStringArray(R.array.minute_time);
        mMonthArrays = getResources().getStringArray(R.array.month_timev2);
        mYearArrays = getResources().getStringArray(R.array.year_time);
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
    }

    @Override
    protected void initViewModel() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void loadData() {
        mBinding.timeDay.setText(mDayArrays[0]);
        mBinding.timeMonth.setText(mMonthArrays[0]);
        mBinding.timeYear.setText(mYearArrays[0]);
        mBinding.timeHour.setText(mHour24Arrays[0]);
        mBinding.timeMinutes.setText(mMinuteArrays[0]);
        mBinding.timeAmPm.setText(mAmPmArrays[0]);
        mBinding.time1224.setText(mTime12_24Arrays[0]);
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
        }
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

    //设置12或24小时时间格式
    private void setmTime12_24() {
        INDEX_12_24 = 1 - INDEX_12_24;
        mBinding.time1224.setText(mTime12_24Arrays[INDEX_12_24]);
    }

    //设置上午或下午
    private void setTimeAmPm() {
        INDEX_AMPM = 1 - INDEX_AMPM;
        mBinding.timeAmPm.setText(mAmPmArrays[INDEX_AMPM]);
    }

    //设置日期
    private void setTimeUp() {
        if (mBinding.timeDay.isChecked()) {
            if (INDEX_DAY < mDayArrays.length - 1) {
                mBinding.timeDay.setText(mDayArrays[++INDEX_DAY]);
            }
        } else if (mBinding.timeMonth.isChecked()) {
            if (INDEX_MONTH < mMonthArrays.length - 1) {
                mBinding.timeMonth.setText(mMonthArrays[++INDEX_MONTH]);
            }
        } else if (mBinding.timeYear.isChecked()) {
            if (INDEX_YEAR < mYearArrays.length - 1) {
                mBinding.timeYear.setText(mYearArrays[++INDEX_YEAR]);
            }
        } else if (mBinding.timeHour.isChecked()) {
            if (INDEX_HOUR < mHour24Arrays.length - 1) {
                mBinding.timeHour.setText(mHour24Arrays[++INDEX_HOUR]);
            }
        } else if (mBinding.timeMinutes.isChecked()) {
            if (INDEX_MINUTES < mMinuteArrays.length - 1) {
                mBinding.timeMinutes.setText(mMinuteArrays[++INDEX_MINUTES]);
            }
        }
    }

    private void setTimeDown() {
        if (mBinding.timeDay.isChecked()) {
            if (INDEX_DAY > 0) {
                mBinding.timeDay.setText(mDayArrays[--INDEX_DAY]);
            }
        } else if (mBinding.timeMonth.isChecked()) {
            if (INDEX_MONTH > 0) {
                mBinding.timeMonth.setText(mMonthArrays[--INDEX_MONTH]);
            }
        } else if (mBinding.timeYear.isChecked()) {
            if (INDEX_YEAR > 0) {
                mBinding.timeYear.setText(mYearArrays[--INDEX_YEAR]);
            }
        } else if (mBinding.timeHour.isChecked()) {
            if (INDEX_HOUR > 0) {
                mBinding.timeHour.setText(mHour24Arrays[--INDEX_HOUR]);
            }
        } else if (mBinding.timeMinutes.isChecked()) {
            if (INDEX_MINUTES > 0) {
                mBinding.timeMinutes.setText(mMinuteArrays[--INDEX_MINUTES]);
            }
        }
    }
}