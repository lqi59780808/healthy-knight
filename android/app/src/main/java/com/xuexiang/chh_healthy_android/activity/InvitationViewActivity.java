package com.xuexiang.chh_healthy_android.activity;
/**
 * @file:  InvitationViewActivity.java
 * @author: chh
 * @date:   11:46 11:46
 * @copyright: 2020-2023 chh. All rights reserved.
 */
import android.os.Bundle;

import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.core.BaseActivity;
import com.xuexiang.chh_healthy_android.fragment.InvitationViewFragment;
import com.xuexiang.chh_healthy_android.fragment.PublishFragment;
import com.xuexiang.chh_healthy_android.utils.Utils;

/**
 * @class InvitationViewActivity
 * @classdesc 查看帖子详情activity
 * @author chh
 * @date 2021/4/15  11:46
 * @version 1.0.0
 * @see
 * @since
 */
public class InvitationViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openPage(InvitationViewFragment.class, getIntent().getExtras());
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
