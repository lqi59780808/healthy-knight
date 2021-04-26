package com.chh.healthy.backend.dao.impl;

import com.boss.xtrain.core.annotation.stuffer.EntityFieldStuffer;
import com.boss.xtrain.core.annotation.stuffer.MethodTypeEnum;
import com.chh.healthy.backend.dao.mapper.BMIMapper;
import com.chh.healthy.backend.dao.mapper.CollectMapper;
import com.chh.healthy.backend.pojo.entity.BMI;
import com.chh.healthy.backend.pojo.entity.Collect;
import com.chh.healthy.backend.pojo.query.BMIQuery;
import com.chh.healthy.backend.pojo.query.CollectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class CollectDAO {
    @Autowired
    CollectMapper mapper;

    @EntityFieldStuffer(methodType = MethodTypeEnum.ADD)
    public Collect saveAndReturn(Collect entity) {
        mapper.insert(entity);
        return entity;
    }

    @EntityFieldStuffer(methodType = MethodTypeEnum.UPDATE)
    public Collect updateAndReturn(Collect entity) {
        mapper.updateByPrimaryKeySelective(entity);
        return entity;
    }

    public List<Collect> queryCollect(CollectQuery query) {
        return mapper.queryCollect(query);
    }

    public Collect queryCollectForInvitaiton(Long userId,Long invitationId) {
        Collect entity = new Collect();
        entity.setInvitationId(invitationId);
        entity.setCreatedBy(userId);
        return mapper.selectOne(entity);
    }

    public Integer delete(Long id) {
        return mapper.deleteByPrimaryKey(id);
    }

    public Integer count(Collect entity) {
        return mapper.selectCount(entity);
    }

}
