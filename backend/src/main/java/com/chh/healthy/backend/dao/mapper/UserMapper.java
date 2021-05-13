package com.chh.healthy.backend.dao.mapper;

import com.boss.xtrain.core.common.dao.CommonMapper;
import com.chh.healthy.backend.pojo.entity.User;
import com.chh.healthy.backend.pojo.query.UserQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends CommonMapper<User> {

    List<User> queryUserList(@Param("query") UserQuery query);
}