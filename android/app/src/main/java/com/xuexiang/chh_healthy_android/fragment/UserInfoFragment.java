package com.xuexiang.chh_healthy_android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.xuexiang.chh_healthy_android.MyApp;
import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.activity.SettingActivity;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.UserDTO;
import com.xuexiang.chh_healthy_android.fragment.user_info.UserInfoNicknameFragment;
import com.xuexiang.chh_healthy_android.fragment.user_info.UserInfoSexFragment;
import com.xuexiang.chh_healthy_android.utils.TokenUtils;
import com.xuexiang.chh_healthy_android.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.app.ActivityUtils;

import butterknife.BindView;

@Page(anim = CoreAnim.none)
public class UserInfoFragment extends BaseFragment implements SuperTextView.OnSuperTextViewClickListener {

    @BindView(R.id.menu_nickname)
    SuperTextView menuNickname;
    @BindView(R.id.menu_sex)
    SuperTextView menuSex;

    private final int RESULT_NICKNAME = 100;
    private final int RESULT_SEX = 101;

    private UserDTO userInfo;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void initViews() {
        userInfo = TokenUtils.getUserInfo();
        menuNickname.setOnSuperTextViewClickListener(this);
        menuSex.setOnSuperTextViewClickListener(this);
        menuNickname.setRightBottomString(userInfo.getNickname());
        String[] sexIndex = {"男","女","保密"};
        menuSex.setRightBottomString(sexIndex[userInfo.getSex()]);
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorTitleBar));
        titleBar.setTitle("用户信息");
        titleBar.setLeftImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        return titleBar;
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_NICKNAME) {
            Bundle bundle = data.getExtras();
            menuNickname.setRightBottomString(bundle.getString("nickname"));
            MyApp myApp = (MyApp) getActivity().getApplication();
            myApp.setUserInfoFlag(true);
        } else if (requestCode == RESULT_SEX) {
            Bundle bundle = data.getExtras();
            String[] sexIndex = {"男","女","保密"};
            int sex = bundle.getInt("sex");
            menuSex.setRightBottomString(sexIndex[sex]);
            MyApp myApp = (MyApp) getActivity().getApplication();
            myApp.setUserInfoFlag(true);
        }
    }

    @SingleClick
    @Override
    public void onClick(SuperTextView superTextView) {
        switch (superTextView.getId()) {
            case R.id.menu_nickname:
                PageOption.to(UserInfoNicknameFragment.class)
                        .setRequestCode(RESULT_NICKNAME)
                        .setAddToBackStack(true)
                        .setAnim(CoreAnim.slide)
                        .open(this);
                break;
            case R.id.menu_sex:
                PageOption.to(UserInfoSexFragment.class)
                        .setRequestCode(RESULT_SEX)
                        .setAddToBackStack(true)
                        .setAnim(CoreAnim.slide)
                        .open(this);
                break;
            default:
                break;
        }
    }
}
