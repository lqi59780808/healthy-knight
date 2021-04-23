package com.chh.healthy.backend.dao.impl;

import com.boss.xtrain.core.annotation.stuffer.EntityFieldStuffer;
import com.boss.xtrain.core.annotation.stuffer.MethodTypeEnum;
import com.chh.healthy.backend.dao.mapper.BMIMapper;
import com.chh.healthy.backend.dao.mapper.StepMapper;
import com.chh.healthy.backend.pojo.entity.BMI;
import com.chh.healthy.backend.pojo.entity.Step;
import com.chh.healthy.backend.pojo.query.BMIQuery;
import com.chh.healthy.backend.pojo.query.StepQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class BMIDAO {
    @Autowired
    BMIMapper mapper;

    @EntityFieldStuffer(methodType = MethodTypeEnum.ADD)
    public BMI saveAndReturn(BMI entity) {
        mapper.insert(entity);
        return entity;
    }

    @EntityFieldStuffer(methodType = MethodTypeEnum.UPDATE)
    public BMI updateAndReturn(BMI entity) {
        mapper.updateByPrimaryKeySelective(entity);
        return entity;
    }

    public List<BMI> queryBMI(BMIQuery query) {
        return mapper.queryBMI(query);
    }

}
