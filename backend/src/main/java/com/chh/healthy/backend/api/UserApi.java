package com.chh.healthy.backend.api;

import com.boss.xtrain.core.common.api.CommonCRUDApi;
import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.query.UserQuery;
import com.chh.healthy.backend.pojo.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/healthy/user")
public interface UserApi extends CommonCRUDApi<UserDTO,UserQuery,UserVO> {
    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<java.lang.Integer>
     * @desc: 用户登录
     * @see
     * @since
     */
    CommonResponse<Integer> login(CommonRequest<UserDTO> request);

    @PostMapping("/register")
    @ApiOperation(value = "用户登录")
    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<java.lang.Integer>
     * @desc: 用户注册
     * @see
     * @since
     */
    CommonResponse<Integer> register(CommonRequest<UserDTO> request);
}
