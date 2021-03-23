package com.chh.healthy.backend.controller;

import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.boss.xtrain.core.common.controller.BaseCRUDController;
import com.chh.healthy.backend.api.UserApi;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.entity.User;
import com.chh.healthy.backend.pojo.query.UserQuery;
import com.chh.healthy.backend.pojo.vo.UserVO;
import com.chh.healthy.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController extends BaseCRUDController<UserDTO, User, UserQuery,UserQuery, UserVO> implements UserApi {
    @Autowired
    UserService userService;

    @Override
    protected List<UserVO> doObjectTransf(List<?> list) {
        return null;
    }

    @Override
    public CommonResponse<Integer> login(CommonRequest<UserDTO> request) {
        return null;
    }

    @Override
    public CommonResponse<Integer> register(CommonRequest<UserDTO> request) {
        return null;
    }
}
