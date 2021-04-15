package com.xuexiang.chh_healthy_android.activity;

import android.content.Intent;
import android.os.Bundle;

import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.core.BaseActivity;
import com.xuexiang.chh_healthy_android.fragment.PublishFragment;
import com.xuexiang.chh_healthy_android.fragment.SettingsFragment;
import com.xuexiang.chh_healthy_android.fragment.UserInfoFragment;
import com.xuexiang.chh_healthy_android.fragment.user_info.UserInfoNicknameFragment;
import com.xuexiang.chh_healthy_android.utils.Utils;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xpage.enums.CoreAnim;


public class SettingActivity extends BaseActivity {

    public Intent intent = null;
    public int code = -100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String type = bundle.getString("type");
            if ("SettingsFragment".equals(type)) {
                openPage(SettingsFragment.class, getIntent().getExtras());
            } else if ("UserInfoFragment".equals(type)) {
                openPage(UserInfoFragment.class, getIntent().getExtras());
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            setResult(100,data);
        }
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
