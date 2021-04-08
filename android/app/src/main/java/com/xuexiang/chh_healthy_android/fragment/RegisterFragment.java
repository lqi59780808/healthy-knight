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
import android.view.KeyEvent;
import android.view.View;

import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipProgressLoadingCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonRequest;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.UserDTO;
import com.xuexiang.chh_healthy_android.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
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
                            .execute(new CallBackProxy<CommonResponse<Integer>, Integer>(new TipProgressLoadingCallBack<Integer>(this) {
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            popToBack();
        }
        return true;
    }
}

