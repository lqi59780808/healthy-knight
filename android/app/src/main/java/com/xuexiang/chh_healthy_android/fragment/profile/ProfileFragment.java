/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.chh_healthy_android.fragment.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.activity.MainActivity;
import com.xuexiang.chh_healthy_android.activity.SettingActivity;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipProgressLoadingCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.InvitationDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.UserDTO;
import com.xuexiang.chh_healthy_android.fragment.AboutFragment;
import com.xuexiang.chh_healthy_android.fragment.PublishFragment;
import com.xuexiang.chh_healthy_android.fragment.SettingsFragment;
import com.xuexiang.chh_healthy_android.fragment.UserInfoFragment;
import com.xuexiang.chh_healthy_android.fragment.UserSettingFragment;
import com.xuexiang.chh_healthy_android.utils.TokenUtils;
import com.xuexiang.chh_healthy_android.utils.Utils;
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
public class ProfileFragment extends BaseFragment implements SuperTextView.OnSuperTextViewClickListener {
    @BindView(R.id.riv_head_pic)
    RadiusImageView rivHeadPic;
    @BindView(R.id.menu_settings)
    SuperTextView menuSettings;
    @BindView(R.id.menu_about)
    SuperTextView menuAbout;
    @BindView(R.id.st_account)
    SuperTextView stAccount;
    @BindView(R.id.icon_set)
    SuperTextView iconSet;

    private List<LocalMedia> mSelectList = new ArrayList<>();
    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        if (TokenUtils.getUserInfo().getIcon() != null) {
            Glide.with(ProfileFragment.this).load(FinalEnum.frontUrl + TokenUtils.getUserInfo().getIcon()).into(rivHeadPic);
        }
    }

    @Override
    protected void initListeners() {
        menuSettings.setOnSuperTextViewClickListener(this);
        menuAbout.setOnSuperTextViewClickListener(this);
        stAccount.setOnSuperTextViewClickListener(this);
        iconSet.setOnSuperTextViewClickListener(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    mSelectList = PictureSelector.obtainMultipleResult(data);
                    //请求post
                    XHttp.post(FinalEnum.frontUrl + "/healthy/user/icon")
                            .params("id", TokenUtils.getUserInfo().getId())
                            .params("version",TokenUtils.getUserInfo().getVersion())
                            .uploadFile("multipartFile",FileUtils.getFileByPath(mSelectList.get(0).getCutPath()), new IProgressResponseCallBack() {
                                @Override
                                public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {
                                }
                            })
                            .syncRequest(false)
                            .onMainThread(true)
                            .execute(new CallBackProxy<CommonResponse<UserDTO>, UserDTO>(new TipProgressLoadingCallBack<UserDTO>(ProfileFragment.this) {
                                @Override
                                public void onSuccess(UserDTO response) throws Throwable {
                                    UserDTO userDTO = TokenUtils.getUserInfo();
                                    userDTO.setVersion(userDTO.getVersion() + 1);
                                    userDTO.setIcon(response.getIcon());
                                    TokenUtils.putUserInfo(userDTO);
                                    Glide.with(ProfileFragment.this).load(FinalEnum.frontUrl + response.getIcon()).into(rivHeadPic);
                                    NavigationView nav = getActivity().findViewById(R.id.nav_view);
                                    RadiusImageView avatar = nav.getHeaderView(0).findViewById(R.id.iv_avatar);
                                    Glide.with(ProfileFragment.this).load(FinalEnum.frontUrl + response.getIcon()).into(avatar);
                                    RadiusImageView riv = getActivity().findViewById(R.id.toolbar_avatar);
                                    Glide.with(ProfileFragment.this).load(FinalEnum.frontUrl + response.getIcon()).into(riv);
                                    XToastUtils.success("头像更改成功");
                                }
                            }){});
                    break;
                default:
                    break;
            }
        } else if (resultCode == 100) {
            Bundle bundle = data.getExtras();
            String nickname = bundle.getString("nickname");
            int sex = bundle.getInt("sex");
            if (nickname != null) {
                NavigationView nav = getActivity().findViewById(R.id.nav_view);
                TextView tv = nav.getHeaderView(0).findViewById(R.id.tv_avatar);
                tv.setText(bundle.getString("nickname"));
            }
        }
    }

    @SingleClick
    @Override
    public void onClick(SuperTextView view) {
        switch(view.getId()) {
            case R.id.menu_settings:
                ActivityUtils.startActivityWithBundle(SettingActivity.class,"type","SettingsFragment");
                break;
            case R.id.menu_about:
                openNewPage(AboutFragment.class);
                break;
            case R.id.st_account:
                ActivityUtils.startActivityForResultWithBundle(getActivity(),SettingActivity.class,100,"type","UserInfoFragment");
                break;
            case R.id.icon_set:
                PictureSelector.create(ProfileFragment.this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)
                        .enableCrop(true)
                        .freeStyleCropEnabled(true)
                        .isDragFrame(true)
                        .withAspectRatio(1,1)
                        .previewImage(true)
                        .selectionMedia(mSelectList)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            default:
                break;
        }
    }
}
