package com.chh.healthy.backend.dao.impl;

import com.boss.xtrain.core.annotation.stuffer.EntityFieldStuffer;
import com.boss.xtrain.core.annotation.stuffer.MethodTypeEnum;
import com.boss.xtrain.core.common.dao.impl.AbstractBaseDao;
import com.chh.healthy.backend.dao.mapper.UserMapper;
import com.chh.healthy.backend.pojo.entity.User;
import com.chh.healthy.backend.pojo.query.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO extends AbstractBaseDao<User, UserMapper, UserQuery> {
    public UserDAO(@Autowired UserMapper userMapper) {
        myMapper = userMapper;
    }

    @Override
    @EntityFieldStuffer(methodType = MethodTypeEnum.ADD)
    public int save(User entity) {
        return super.save(entity);
    }
}
