/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.chh_healthy_android.fragment.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.activity.MyInvitationActivity;
import com.xuexiang.chh_healthy_android.activity.SettingActivity;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipCallBack;
import com.xuexiang.chh_healthy_android.core.http.callback.TipProgressLoadingCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.UserDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.adminDTO;
import com.xuexiang.chh_healthy_android.fragment.bmi.BmiFragment;
import com.xuexiang.chh_healthy_android.fragment.bmi.HistoryBmiFragment;
import com.xuexiang.chh_healthy_android.utils.TokenUtils;
import com.xuexiang.chh_healthy_android.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xhttp2.callback.impl.IProgressResponseCallBack;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.file.FileUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019-10-30 00:18
 */
@Page(anim = CoreAnim.none)
public class AdminFragment extends BaseFragment implements SuperTextView.OnSuperTextViewClickListener {
    @BindView(R.id.st_user)
    SuperTextView stUser;
    @BindView(R.id.st_invitation)
    SuperTextView stInvitation;
    @BindView(R.id.tv_sum)
    TextView tvSum;
    @BindView(R.id.tv_male)
    TextView tvMale;
    @BindView(R.id.tv_female)
    TextView tvFemale;
    @BindView(R.id.tv_invitation)
    TextView tvInvitation;
    @BindView(R.id.tv_reply)
    TextView tvReply;
    private List<LocalMedia> mSelectList = new ArrayList<>();
    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorTitleBar));
        titleBar.setTitle("管理员管理");
        titleBar.setLeftImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        titleBar.setActionTextColor(getResources().getColor(R.color.white));
        return titleBar;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_admin;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        initRequest();
    }

    @Override
    protected void initListeners() {
        stUser.setOnSuperTextViewClickListener(this);
        stInvitation.setOnSuperTextViewClickListener(this);
    }
    @SingleClick
    @Override
    public void onClick(SuperTextView view) {
        switch(view.getId()) {
            case R.id.st_user:
                PageOption.to(AdminUserFragment.class)
                        .setRequestCode(100)
                        .setAddToBackStack(true)
                        .setAnim(CoreAnim.slide)
                        .open(this);
                break;
            case R.id.st_invitation:
                PageOption.to(AdminInvitationFragment.class)
                        .setRequestCode(100)
                        .setAddToBackStack(true)
                        .setAnim(CoreAnim.slide)
                        .open(this);
                break;
            default:
                break;
        }
    }

    private void initRequest() {
        XHttp.get(FinalEnum.frontUrl + "/healthy/user/count")
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<adminDTO>, adminDTO>(new TipCallBack<adminDTO>() {
                    @Override
                    public void onSuccess(adminDTO response) throws Throwable {
                        tvSum.setText(tvSum.getText().toString() + response.getSum());
                        tvMale.setText(tvMale.getText().toString() + response.getMale());
                        tvFemale.setText(tvFemale.getText().toString() + response.getFemale());
                        tvInvitation.setText(tvInvitation.getText().toString() + response.getInvitation());
                        tvReply.setText(tvReply.getText().toString() + response.getReply());
                    }
                }){});
    }
}
