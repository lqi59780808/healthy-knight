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

package com.xuexiang.chh_healthy_android.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.activity.MainActivity;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipCallBack;
import com.xuexiang.chh_healthy_android.core.http.callback.TipProgressLoadingCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonRequest;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.UserDTO;
import com.xuexiang.chh_healthy_android.utils.SettingUtils;
import com.xuexiang.chh_healthy_android.utils.TokenUtils;
import com.xuexiang.chh_healthy_android.utils.Utils;
import com.xuexiang.chh_healthy_android.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.net.JsonUtil;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


/**
 * 登录页面
 *
 * @author xuexiang
 * @since 2019-11-17 22:15
 */
@Page(anim = CoreAnim.none)
public class UserSettingFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    @BindView(R.id.et_nickname)
    MaterialEditText etNickname;
    Integer sex;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_setting;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle()
                .setImmersive(true);
        titleBar.setBackgroundColor(Color.TRANSPARENT);
        titleBar.setTitle("");
        return titleBar;
    }

    @Override
    protected void initViews() {
        sex = 2;
    }

    @SingleClick
    @OnClick({R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                CommonRequest<UserDTO> commonRequest = new CommonRequest<>();
                Bundle bundle = getArguments();
                UserDTO userDTO = JsonUtil.fromJson(bundle.getString("userInfo"),UserDTO.class);
                userDTO.setNickname(etNickname.getEditValue());
                userDTO.setSex(sex.byteValue());
                commonRequest.setBody(userDTO);
                String body = JsonUtil.toJson(commonRequest);
                XHttp.post(FinalEnum.frontUrl + "/healthy/user/init")
                        .upJson(body)
                        .syncRequest(false)
                        .onMainThread(true)
                        .execute(new CallBackProxy<CommonResponse<UserDTO>, UserDTO>(new TipProgressLoadingCallBack<UserDTO>(this) {
                            @Override
                            public void onSuccess(UserDTO response) throws Throwable {
                                XToastUtils.success("初始化成功");
                                onLoginSuccess(response);
                            }
                        }){});
                break;
            default:
                break;
        }
    }

    /**
     * 登录成功的处理
     */
    private void onLoginSuccess(UserDTO userDTO) {
        if (TokenUtils.handleLoginSuccess(userDTO.getUsername())) {
            TokenUtils.putUserInfo(userDTO);
            popToBack();
            ActivityUtils.startActivity(MainActivity.class);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        RadioButton radioButton = radioGroup.findViewById(i);
        if (radioButton.getText().equals("男")) {
            sex = 0;
        } else if (radioButton.getText().equals("女")) {
            sex = 1;
        } else {
            sex = 2;
        }
    }
}

