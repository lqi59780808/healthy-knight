package com.xuexiang.chh_healthy_android.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;
import com.xuexiang.chh_healthy_android.R;

import com.xuexiang.chh_healthy_android.core.BaseActivity;
import com.xuexiang.chh_healthy_android.fragment.PublishFragment;
import com.xuexiang.chh_healthy_android.fragment.step.StepCountFragment;
import com.xuexiang.chh_healthy_android.step.UpdateUiCallBack;
import com.xuexiang.chh_healthy_android.step.service.StepService;
import com.xuexiang.chh_healthy_android.utils.SharedPreferencesUtils;
import com.xuexiang.chh_healthy_android.utils.Utils;
import com.xuexiang.chh_healthy_android.widget.StepArcView;
import com.xuexiang.xutil.app.ServiceUtils;
import com.xuexiang.xutil.common.logger.Logger;

import butterknife.BindView;


/**
 * 记步主页
 */
public class StepActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openPage(StepCountFragment.class, getIntent().getExtras());
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
