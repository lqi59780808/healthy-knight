package com.chh.healthy.backend.service;

import com.boss.xtrain.core.common.api.CommonResponse;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.dto.adminDTO;
import com.chh.healthy.backend.pojo.query.UserQuery;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    /**
     * @param: [userDTO]
     * @return: java.lang.Integer
     * @desc: 登录
     * @see
     * @since
     */
    CommonResponse<UserDTO> doLogin(UserDTO userDTO);

    /**
     * @param: [userDTO]
     * @return: java.lang.Integer
     * @desc: 注册
     * @see
     * @since
     */
    CommonResponse<Integer> doRegister(UserDTO userDTO);

    /**
     * @param: [userDTO]
     * @return: java.lang.Integer
     * @desc: 初始化
     * @see
     * @since
     */
    CommonResponse<UserDTO> doInit(UserDTO userDTO);

    /**
     * @param: [id, multipartFile]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<com.chh.healthy.backend.pojo.dto.UserDTO>
     * @desc: 上传头像
     * @see
     * @since
     */
    CommonResponse<UserDTO> doUpdateIcon(long id,long version, MultipartFile multipartFile);

    CommonResponse<UserDTO> doUpdate(UserDTO request);

    CommonResponse<adminDTO> doCount();

    CommonResponse<List<UserDTO>> doQueryUser(UserQuery query);
}
