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

import com.chh.healthy.frontend.activity.MainActivity;
import com.chh.healthy.frontend.core.BaseFragment;
import com.chh.healthy.frontend.core.FinalEnum;
import com.chh.healthy.frontend.utils.RandomUtils;
import com.chh.healthy.frontend.utils.TokenUtils;
import com.chh.healthy.frontend.utils.XToastUtils;
import com.xuexiang.frontend.R;
import com.chh.healthy.frontend.core.http.callback.TipCallBack;
import com.chh.healthy.frontend.core.http.entity.CommonRequest;
import com.chh.healthy.frontend.core.http.entity.CommonResponse;
import com.chh.healthy.frontend.core.http.pojo.UserDTO;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xpage.annotation.Page;
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
 * @class RegisterFragment
 * @classdesc
 * @author chh
 * @date 2021/3/30  21:01
 * @version 1.0.0
 * @see
 * @since
 */
@Page(anim = CoreAnim.none)
public class RegisterFragment extends BaseFragment {

    @BindView(R.id.et_username)
    MaterialEditText etUsername;
    @BindView(R.id.et_password)
    MaterialEditText etPassword;
    @BindView(R.id.et_password2)
    MaterialEditText etPassword2;
    @BindView(R.id.et_email)
    MaterialEditText etEmail;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle()
                .setImmersive(true);
        titleBar.setBackgroundColor(Color.TRANSPARENT);
        titleBar.setTitle("");
        titleBar.setLeftImageDrawable(ResUtils.getVectorDrawable(getContext(), R.drawable.ic_login_close));
        titleBar.setActionTextColor(ThemeUtils.resolveColor(getContext(), R.attr.colorAccent));
        return titleBar;
    }

    @SingleClick
    @OnClick({R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                if (!etPassword.getEditValue().equals(etPassword2.getEditValue())) {
                    XToastUtils.error("两次输入的密码不一致！");
                    break;
                }
                if (etUsername.validate() && etPassword.validate() && etEmail.validate()) {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setUsername(etUsername.getEditValue());
                    userDTO.setPassword(etPassword.getEditValue());
                    userDTO.setEmail(etEmail.getEditValue());
                    CommonRequest<UserDTO> commonRequest = new CommonRequest<>();
                    commonRequest.setBody(userDTO);
                    String body = JsonUtil.toJson(commonRequest);
                    XHttp.post(FinalEnum.frontUrl + "/healthy/user/register")
                            .upJson(body)
                            .syncRequest(false)
                            .onMainThread(true)
                            .execute(new CallBackProxy<CommonResponse<Integer>, Integer>(new TipCallBack<Integer>() {
                                @Override
                                public void onSuccess(Integer response) throws Throwable {
                                    if (response == 1) {
                                        XToastUtils.success("注册成功");
                                        popToBack();
                                    }
                                }
                            }){});
                } else {
                    XToastUtils.error("请规范填写注册信息");
                }
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

