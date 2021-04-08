package com.chh.healthy.backend.service.impl;

import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.boss.xtrain.core.common.api.CommonResponseUtils;
import com.boss.xtrain.core.common.exception.ServiceException;
import com.boss.xtrain.core.common.service.BaseCURDService;
import com.boss.xtrain.core.context.BaseContextHolder;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class UserServiceImpl extends BaseCURDService<UserDTO, User, UserQuery, UserMapper> implements UserService {

    @Autowired
    UserDAO userDAO;

    public UserServiceImpl(@Autowired UserDAO userDAO) {
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
    public CommonResponse<UserDTO> doLogin(UserDTO userDTO) {
        try{
            if (userDTO.getUsername() != null && userDTO.getPassword() != null) {
                String md5Password = DigestUtils.md5DigestAsHex(userDTO.getPassword().getBytes());
                userDTO.setPassword(md5Password);
                User user = userDAO.queryByUsername(BeanUtil.copy(userDTO,User.class));
                if (user == null || !(user.getPassword().equals(userDTO.getPassword()))) {
                    return CommonResponseUtils.failed("登录失败，请检查用户名和密码是否正确");
                } else {
                    UserDTO response = BeanUtil.copy(user,UserDTO.class);
                    //不返回密码的哈希码
                    response.setPassword(null);
                    BaseContextHolder.set("userInfo",userDTO);
                    return CommonResponseUtils.success(response);
                }
            } else {
                throw new ServiceException(ErrorCode.LOGIN_MISS_EXCEPTION,new IllegalArgumentException());
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.LOGIN_MISS_EXCEPTION,e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResponse<Integer> doRegister(UserDTO userDTO) {
        try {
            if (Boolean.TRUE.equals(checkRegister(userDTO))) {
                User user = BeanUtil.copy(userDTO,User.class);
                //md5加密
                String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
                user.setPassword(md5Password);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResponse<UserDTO> doInit(UserDTO userDTO) {
        try {
            if (userDTO != null && 1 < userDTO.getNickname().length() && userDTO.getNickname().length() < 21) {
                userDTO.setStatus((byte)2);
                User response = userDAO.updateAndReturn(BeanUtil.copy(userDTO,User.class));
                BaseContextHolder.set("userInfo",BeanUtil.copy(response,UserDTO.class));
                return CommonResponseUtils.success(BeanUtil.copy(response,UserDTO.class));
            } else {
                return CommonResponseUtils.failed("昵称应该在2-20个字符之间");
            }
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.INIT_EXCEPTION,e);
        }
    }

    public Boolean checkRegister(UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (username ==null || password == null) {
            return false;
        }
        int usernameLen = username.length();
        int passwordLen = password.length();
        if ((usernameLen < 8 && usernameLen > 18) ||
                (passwordLen < 8 && passwordLen > 18)) {
            return false;
        }
        else {
            return true;
        }
    }
}
