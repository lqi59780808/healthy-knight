package com.xuexiang.chh_healthy_android.activity;

import android.content.Intent;
import android.os.Bundle;

import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.core.BaseActivity;
import com.xuexiang.chh_healthy_android.fragment.my.MyInvitationFragment;
import com.xuexiang.chh_healthy_android.utils.Utils;


public class MyInvitationActivity extends BaseActivity {

    public Intent intent = null;
    public int code = -100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openPage(MyInvitationFragment.class, getIntent().getExtras());
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
