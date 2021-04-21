package com.chh.healthy.backend.dao.impl;

import com.boss.xtrain.core.annotation.stuffer.EntityFieldStuffer;
import com.boss.xtrain.core.annotation.stuffer.MethodTypeEnum;
import com.boss.xtrain.core.common.dao.impl.AbstractBaseDao;
import com.chh.healthy.backend.dao.mapper.ReplyMapper;
import com.chh.healthy.backend.dao.mapper.UserMapper;
import com.chh.healthy.backend.pojo.dto.InvitationDTO;
import com.chh.healthy.backend.pojo.entity.Reply;
import com.chh.healthy.backend.pojo.entity.User;
import com.chh.healthy.backend.pojo.query.ReplyQuery;
import com.chh.healthy.backend.pojo.query.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ReplyDAO {
    @Autowired
    ReplyMapper mapper;

    @EntityFieldStuffer(methodType = MethodTypeEnum.ADD)
    public Reply saveAndReturn(Reply entity) {
        mapper.insert(entity);
        return entity;
    }

    public List<Reply> queryByPage (ReplyQuery query) {
        return mapper.queryByPage(query);
    }

    public List<Reply> queryByPage2 (ReplyQuery query) {
        return mapper.queryByPage2(query);
    }
}
