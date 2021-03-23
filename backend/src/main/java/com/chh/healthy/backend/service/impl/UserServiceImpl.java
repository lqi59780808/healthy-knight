package com.chh.healthy.backend.service.impl;

import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.boss.xtrain.core.common.api.CommonResponseUtils;
import com.boss.xtrain.core.common.exception.ServiceException;
import com.boss.xtrain.core.common.service.BaseCURDService;
import com.boss.xtrain.util.BeanUtil;
import com.chh.healthy.backend.dao.impl.UserDAO;
import com.chh.healthy.backend.dao.mapper.UserMapper;
import com.chh.healthy.backend.pojo.code.ErrorCode;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.entity.User;
import com.chh.healthy.backend.pojo.query.UserQuery;
import com.chh.healthy.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends BaseCURDService<UserDTO, User, UserQuery, UserMapper> implements UserService {

    @Autowired
    UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.myDao = userDAO;
    }

    @Override
    public List<?> query(CommonRequest<UserQuery> commonRequest) {
        return null;
    }

    @Override
    protected User doObjectTransf(UserDTO userDTO) {
        return null;
    }

    @Override
    public CommonResponse<Integer> doLogin(UserDTO userDTO) {
        return null;
    }

    @Override
    public CommonResponse<Integer> doRegister(UserDTO userDTO) {
        try {
            if (Boolean.TRUE.equals(checkRegister(userDTO))) {
                User user = BeanUtil.copy(userDTO,User.class);
                int result = myDao.save(user);
                if (result == 1) {
                    return CommonResponseUtils.success(result);
                } else {
                    throw new ServiceException(ErrorCode.REGISTER_REPEAT_EXCEPTION,new IllegalArgumentException());
                }
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.REGISTER_EXCEPTION,e);
        }
        return CommonResponseUtils.success();
    }

    public Boolean checkRegister(UserDTO userDTO) {
        String username = userDTO.getUsername();
        String nickname = userDTO.getNickname();
        String password = userDTO.getPassword();
        if (username ==null || nickname == null || password == null) {
            return false;
        }
        int usernameLen = username.length();
        int nicknameLen = nickname.length();
        int passwordLen = password.length();
        if ((usernameLen < 6 && usernameLen > 15) ||
                (nicknameLen < 1 && nicknameLen > 20) ||
                (passwordLen < 8 && passwordLen > 15)) {
            return false;
        }
        else {
            return true;
        }
    }
}
