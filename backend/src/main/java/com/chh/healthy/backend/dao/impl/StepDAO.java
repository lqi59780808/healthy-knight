package com.chh.healthy.backend.dao.impl;

import com.boss.xtrain.core.annotation.stuffer.EntityFieldStuffer;
import com.boss.xtrain.core.annotation.stuffer.MethodTypeEnum;
import com.chh.healthy.backend.dao.mapper.ReplyMapper;
import com.chh.healthy.backend.dao.mapper.StepMapper;
import com.chh.healthy.backend.pojo.entity.Reply;
import com.chh.healthy.backend.pojo.entity.Step;
import com.chh.healthy.backend.pojo.query.ReplyQuery;
import com.chh.healthy.backend.pojo.query.StepQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class StepDAO {
    @Autowired
    StepMapper mapper;

    @EntityFieldStuffer(methodType = MethodTypeEnum.ADD)
    public Step saveAndReturn(Step entity) {
        mapper.insert(entity);
        return entity;
    }

    @EntityFieldStuffer(methodType = MethodTypeEnum.UPDATE)
    public Step updateAndReturn(Step entity) {
        mapper.updateByPrimaryKeySelective(entity);
        return entity;
    }

    public List<Step> queryStep(StepQuery query) {
        return mapper.queryStep(query);
    }

}
