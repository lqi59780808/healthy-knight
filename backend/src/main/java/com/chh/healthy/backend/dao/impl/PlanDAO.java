package com.chh.healthy.backend.dao.impl;

import com.boss.xtrain.core.annotation.stuffer.EntityFieldStuffer;
import com.boss.xtrain.core.annotation.stuffer.MethodTypeEnum;
import com.chh.healthy.backend.dao.mapper.BMIMapper;
import com.chh.healthy.backend.dao.mapper.PlanMapper;
import com.chh.healthy.backend.pojo.entity.BMI;
import com.chh.healthy.backend.pojo.entity.Plan;
import com.chh.healthy.backend.pojo.query.BMIQuery;
import com.chh.healthy.backend.pojo.query.PlanQuery;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class PlanDAO {
    @Autowired
    PlanMapper mapper;

    @EntityFieldStuffer(methodType = MethodTypeEnum.ADD)
    public Plan saveAndReturn(Plan entity) {
        mapper.insert(entity);
        return entity;
    }

    @EntityFieldStuffer(methodType = MethodTypeEnum.UPDATE)
    public Plan updateAndReturn(Plan entity) {
        mapper.updateByPrimaryKeySelective(entity);
        return entity;
    }

    public List<Plan> queryPlan(PlanQuery query) {
        return mapper.queryPlan(query);
    }

    public Integer deletePlan(Long id) {
        Plan plan = new Plan();
        plan.setCreatedBy(id);
        return mapper.delete(plan);
    }

}
