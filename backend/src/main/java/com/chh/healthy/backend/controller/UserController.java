package com.chh.healthy.backend.controller;

import com.boss.xtrain.core.common.api.CommonPage;
import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.boss.xtrain.core.common.controller.BaseCRUDController;
import com.boss.xtrain.core.context.BaseContextHolder;
import com.chh.healthy.backend.api.UserApi;
import com.chh.healthy.backend.dao.mapper.UserMapper;
import com.chh.healthy.backend.pojo.dto.InvitationDTO;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.dto.adminDTO;
import com.chh.healthy.backend.pojo.entity.User;
import com.chh.healthy.backend.pojo.query.InvitationQuery;
import com.chh.healthy.backend.pojo.query.UserQuery;
import com.chh.healthy.backend.pojo.vo.UserVO;
import com.chh.healthy.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class UserController extends BaseCRUDController<UserDTO, User, UserQuery, UserMapper, UserVO> implements UserApi {
    @Autowired
    UserService userService;

    @Override
    protected List<UserVO> doObjectTransf(List<?> list) {
        return null;
    }

    @Override
    public CommonResponse<UserDTO> login(@RequestBody CommonRequest<UserDTO> request) {
        return userService.doLogin(request.getBody());
    }

    @Override
    public CommonResponse<UserDTO> register(@RequestBody CommonRequest<UserDTO> request) {
        return userService.doRegister(request.getBody());
    }

    @Override
    public CommonResponse<UserDTO> init(@RequestBody CommonRequest<UserDTO> request) {
        BaseContextHolder.setUserId(request.getBody().getId());
        return userService.doInit(request.getBody());
    }

    @Override
    public CommonResponse<UserDTO> updateIcon(@RequestParam long id, @RequestParam long version,@RequestParam MultipartFile multipartFile) {
        BaseContextHolder.setUserId(id);
        return userService.doUpdateIcon(id,version,multipartFile);
    }

    @Override
    public CommonResponse<UserDTO> update2(@RequestBody CommonRequest<UserDTO> request) {
        BaseContextHolder.setUserId(request.getBody().getId());
        return userService.doUpdate(request.getBody());
    }

    @Override
    public CommonResponse<adminDTO> count() {
        return userService.doCount();
    }

    @Override
    public CommonResponse<List<UserDTO>> queryUser(@RequestBody CommonRequest<UserQuery> request) {
        UserQuery query = request.getBody();
        this.doBeforePagination(query.getPageNum(),query.getPageSize());
        return userService.doQueryUser(request.getBody());
    }

    @Override
    public CommonResponse<UserDTO> queryById(@RequestBody CommonRequest<Long> id) {
        return userService.doQueryById(id.getBody());
    }
}
