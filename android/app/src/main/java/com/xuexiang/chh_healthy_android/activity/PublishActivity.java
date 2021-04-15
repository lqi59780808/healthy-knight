package com.xuexiang.chh_healthy_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.core.BaseActivity;
import com.xuexiang.chh_healthy_android.fragment.LoginFragment;
import com.xuexiang.chh_healthy_android.fragment.PublishFragment;
import com.xuexiang.chh_healthy_android.utils.Utils;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.display.Colors;


public class PublishActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openPage(PublishFragment.class, getIntent().getExtras());
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
