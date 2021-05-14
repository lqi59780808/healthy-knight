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

package com.xuexiang.chh_healthy_android.activity;

import android.content.Intent;
import android.view.KeyEvent;

import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipProgressLoadingCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonRequest;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.UserDTO;
import com.xuexiang.chh_healthy_android.utils.SettingUtils;
import com.xuexiang.chh_healthy_android.utils.TokenUtils;
import com.xuexiang.chh_healthy_android.utils.Utils;
import com.xuexiang.chh_healthy_android.utils.XToastUtils;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xhttp2.callback.SimpleCallBack;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.widget.activity.BaseSplashActivity;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.net.JsonUtil;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 启动页【无需适配屏幕大小】
 *
 * @author xuexiang
 * @since 2019-06-30 17:32
 */
public class SplashActivity extends BaseSplashActivity implements CancelAdapt {

    @Override
    protected long getSplashDurationMillis() {
        return 500;
    }

    /**
     * activity启动后的初始化
     */
    @Override
    protected void onCreateActivity() {
        initSplashView(R.drawable.xui_config_bg_splash);
        startSplash(false);
    }


    /**
     * 启动页结束后的动作
     */
    @Override
    protected void onSplashFinished() {
        if (SettingUtils.isAgreePrivacy()) {
            loginOrGoMainPage();
        } else {
            Utils.showPrivacyDialog(this, (dialog, which) -> {
                dialog.dismiss();
                SettingUtils.setIsAgreePrivacy(true);
                loginOrGoMainPage();
            });
        }
    }

    private void loginOrGoMainPage() {
        if (TokenUtils.hasToken()) {
            CommonRequest<Long> commonRequest = new CommonRequest<>();
            commonRequest.setBody(TokenUtils.getUserInfo().getId());
            String body = JsonUtil.toJson(commonRequest);
            XHttp.post(FinalEnum.frontUrl + "/healthy/user/id")
                    .upJson(body)
                    .syncRequest(false)
                    .onMainThread(true)
                    .timeOut(5000)
                    .execute(new CallBackProxy<CommonResponse<UserDTO>, UserDTO>(new SimpleCallBack<UserDTO>() {
                        @Override
                        public void onSuccess(UserDTO response) throws Throwable {
                            if (response.getStatus() == 3) {
                                XToastUtils.error("该账号目前处于封禁状态");
                                TokenUtils.clearToken();
                                ActivityUtils.startActivity(LoginActivity.class);
                            } else {
                                TokenUtils.putUserInfo(response);
                                ActivityUtils.startActivity(MainActivity.class);
                            }
                        }

                        @Override
                        public void onError(ApiException e) {
                            ActivityUtils.startActivity(MainActivity.class);
                        }
                    }){});
        } else {
            ActivityUtils.startActivity(LoginActivity.class);
        }
        finish();
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event);
    }
}
