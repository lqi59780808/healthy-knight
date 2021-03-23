package com.chh.healthy.backend.service;

import com.boss.xtrain.core.common.api.CommonResponse;
import com.chh.healthy.backend.pojo.dto.UserDTO;

public interface UserService {
    /**
     * @param: [userDTO]
     * @return: java.lang.Integer
     * @desc: 登录
     * @see
     * @since
     */
    CommonResponse<Integer> doLogin(UserDTO userDTO);

    /**
     * @param: [userDTO]
     * @return: java.lang.Integer
     * @desc: 注册
     * @see
     * @since
     */
    CommonResponse<Integer> doRegister(UserDTO userDTO);
}
