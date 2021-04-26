package com.chh.healthy.backend.dao.impl;

import com.boss.xtrain.core.annotation.stuffer.EntityFieldStuffer;
import com.boss.xtrain.core.annotation.stuffer.MethodTypeEnum;
import com.chh.healthy.backend.dao.mapper.BMIMapper;
import com.chh.healthy.backend.dao.mapper.GoodMapper;
import com.chh.healthy.backend.pojo.entity.BMI;
import com.chh.healthy.backend.pojo.entity.Collect;
import com.chh.healthy.backend.pojo.entity.Good;
import com.chh.healthy.backend.pojo.query.BMIQuery;
import com.chh.healthy.backend.pojo.query.GoodQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class GoodDAO {
    @Autowired
    GoodMapper mapper;

    @EntityFieldStuffer(methodType = MethodTypeEnum.ADD)
    public Good saveAndReturn(Good entity) {
        mapper.insert(entity);
        return entity;
    }

    @EntityFieldStuffer(methodType = MethodTypeEnum.UPDATE)
    public Good updateAndReturn(Good entity) {
        mapper.updateByPrimaryKeySelective(entity);
        return entity;
    }

    public List<Good> queryGood(GoodQuery query) {
        return mapper.queryGood(query);
    }

    public Good queryCollectForInvitaiton(Long userId, Long invitationId) {
        Good entity = new Good();
        entity.setInvitationId(invitationId);
        entity.setCreatedBy(userId);
        return mapper.selectOne(entity);
    }

    public Integer delete(Long id) {
        return mapper.deleteByPrimaryKey(id);
    }

    public Integer count(Good entity) {
        return mapper.selectCount(entity);
    }
}
