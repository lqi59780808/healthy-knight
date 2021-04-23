package com.xuexiang.chh_healthy_android.activity;

import android.os.Bundle;

import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.core.BaseActivity;
import com.xuexiang.chh_healthy_android.fragment.bmi.BmiFragment;
import com.xuexiang.chh_healthy_android.fragment.step.StepCountFragment;
import com.xuexiang.chh_healthy_android.utils.SharedPreferencesUtils;
import com.xuexiang.chh_healthy_android.utils.Utils;


/**
 * 记步主页
 */
public class BmiActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openPage(BmiFragment.class, getIntent().getExtras());
    }

    @Override
    protected boolean isSupportSlideBack() {
        return false;
    }

    @Override
    protected void initStatusBarStyle() {
        Utils.changeStatusBar(getResources().getColor(R.color.colorTitleBar),this);
    }
}
