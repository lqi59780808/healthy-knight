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

package com.chh.healthy.frontend.fragment;

import android.graphics.Color;
import android.view.View;

import com.xuexiang.frontend.R;
import com.chh.healthy.frontend.activity.MainActivity;
import com.chh.healthy.frontend.core.BaseFragment;
import com.chh.healthy.frontend.core.FinalEnum;
import com.chh.healthy.frontend.core.http.callback.TipCallBack;
import com.chh.healthy.frontend.core.http.entity.CommonRequest;
import com.chh.healthy.frontend.core.http.entity.CommonResponse;
import com.chh.healthy.frontend.core.http.pojo.UserDTO;
import com.chh.healthy.frontend.utils.RandomUtils;
import com.chh.healthy.frontend.utils.SettingUtils;
import com.chh.healthy.frontend.utils.TokenUtils;
import com.chh.healthy.frontend.utils.Utils;
import com.chh.healthy.frontend.utils.XToastUtils;
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
import butterknife.OnClick;


/**
 * 登录页面
 *
 * @author xuexiang
 * @since 2019-11-17 22:15
 */
@Page(anim = CoreAnim.none)
public class LoginFragment extends BaseFragment {

    @BindView(R.id.et_username)
    MaterialEditText etUsername;
    @BindView(R.id.et_password)
    MaterialEditText etPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle()
                .setImmersive(true);
        titleBar.setBackgroundColor(Color.TRANSPARENT);
        titleBar.setTitle("");
        titleBar.setLeftImageDrawable(ResUtils.getVectorDrawable(getContext(), R.drawable.ic_login_close));
        titleBar.setActionTextColor(ThemeUtils.resolveColor(getContext(), R.attr.colorAccent));
        titleBar.addAction(new TitleBar.TextAction(R.string.title_jump_login) {
            @Override
            public void performAction(View view) {
                onLoginSuccess();
            }
        });
        return titleBar;
    }

    @Override
    protected void initViews() {

        //隐私政策弹窗
        if (!SettingUtils.isAgreePrivacy()) {
            Utils.showPrivacyDialog(getContext(), (dialog, which) -> {
                dialog.dismiss();
                SettingUtils.setIsAgreePrivacy(true);
            });
        }
    }

    @SingleClick
    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forget_password, R.id.tv_user_protocol, R.id.tv_privacy_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (etPassword.validate() && etUsername.validate()) {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setUsername(etUsername.getEditValue());
                    userDTO.setPassword(etPassword.getEditValue());
                    CommonRequest<UserDTO> commonRequest = new CommonRequest<>();
                    commonRequest.setBody(userDTO);
                    String body = JsonUtil.toJson(commonRequest);
                    XHttp.post(FinalEnum.frontUrl + "/healthy/user/login")
                            .upJson(body)
                            .syncRequest(false)
                            .onMainThread(true)
                            .execute(new CallBackProxy<CommonResponse<UserDTO>, UserDTO>(new TipCallBack<UserDTO>() {
                                @Override
                                public void onSuccess(UserDTO response) throws Throwable {
                                    XToastUtils.success("登陆成功");
                                    onLoginSuccess();
                                }
                            }){});
                } else {
                    XToastUtils.error("请规范输入用户名和密码");
                }
                break;
            case R.id.tv_register:
                PageOption.to(RegisterFragment.class)
                        .setRequestCode(100)
                        .setAddToBackStack(true)
                        .setAnim(CoreAnim.slide)
                        .open(this);
                break;
            case R.id.tv_forget_password:
                XToastUtils.info("忘记密码");
                break;
            case R.id.tv_user_protocol:
                XToastUtils.info("用户协议");
                break;
            case R.id.tv_privacy_protocol:
                XToastUtils.info("隐私政策");
                break;
            default:
                break;
        }
    }

    /**
     * 登录成功的处理
     */
    private void onLoginSuccess() {
        String token = RandomUtils.getRandomNumbersAndLetters(16);
        if (TokenUtils.handleLoginSuccess(token)) {
            popToBack();
            ActivityUtils.startActivity(MainActivity.class);
        }
    }
}

