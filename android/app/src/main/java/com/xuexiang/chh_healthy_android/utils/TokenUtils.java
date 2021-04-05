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

package com.xuexiang.chh_healthy_android.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.xuexiang.chh_healthy_android.activity.LoginActivity;
import com.xuexiang.chh_healthy_android.core.http.pojo.UserDTO;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.common.StringUtils;

/**
 * Token管理工具
 *
 * @author xuexiang
 * @since 2019-11-17 22:37
 */
public final class TokenUtils {

    private static String sToken;

    private static UserDTO userDTO;

    private static final String KEY_TOKEN = "com.xuexiang.templateproject.utils.KEY_TOKEN";

    private TokenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static final String KEY_PROFILE_CHANNEL = "github";

    private static final String KEY_USER_INFO = "userInfo";

    /**
     * 初始化Token信息
     */
    public static void init(Context context) {
        MMKVUtils.init(context);
        sToken = MMKVUtils.getString(KEY_TOKEN, "");
        userDTO = (UserDTO) MMKVUtils.get(KEY_USER_INFO, new UserDTO());
    }

    public static void setToken(String token) {
        sToken = token;
        MMKVUtils.put(KEY_TOKEN, token);
    }

    public static void clearToken() {
        sToken = null;
        MMKVUtils.remove(KEY_TOKEN);
        MMKVUtils.remove(KEY_USER_INFO);
    }

    public static String getToken() {
        return sToken;
    }

    public static boolean hasToken() {
        return MMKVUtils.containsKey(KEY_TOKEN);
    }

    /**
     * 处理登录成功的事件
     *
     * @param token 账户信息
     */
    public static boolean handleLoginSuccess(String token) {
        if (!StringUtils.isEmpty(token)) {
            XToastUtils.success("登录成功！");
            MobclickAgent.onProfileSignIn(KEY_PROFILE_CHANNEL, token);
            setToken(token);
            return true;
        } else {
            XToastUtils.error("登录失败！");
            return false;
        }
    }

    public static boolean putUserInfo(UserDTO userDTO) {
        if (userDTO != null) {
            MMKVUtils.put(KEY_USER_INFO,userDTO);
            return true;
        } else {
            return false;
        }
    }

    public static UserDTO getUserInfo() {
        return (UserDTO) MMKVUtils.get(KEY_USER_INFO,new UserDTO());
    }


    /**
     * 处理登出的事件
     */
    public static void handleLogoutSuccess() {
        MobclickAgent.onProfileSignOff();
        //登出时，清除账号信息
        clearToken();
        XToastUtils.success("登出成功！");
        //跳转到登录页
        ActivityUtils.startActivity(LoginActivity.class);
    }

}
