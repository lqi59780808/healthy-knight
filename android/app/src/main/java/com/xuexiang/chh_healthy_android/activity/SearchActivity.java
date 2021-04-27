package com.xuexiang.chh_healthy_android.activity;

import android.os.Bundle;

import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.core.BaseActivity;
import com.xuexiang.chh_healthy_android.fragment.SearchViewFragment;
import com.xuexiang.chh_healthy_android.fragment.bmi.BmiFragment;
import com.xuexiang.chh_healthy_android.utils.Utils;



public class SearchActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openPage(SearchViewFragment.class, getIntent().getExtras());
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
