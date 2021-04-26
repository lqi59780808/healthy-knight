package com.chh.healthy.backend.dao.mapper;

import com.boss.xtrain.core.common.dao.CommonMapper;
import com.chh.healthy.backend.pojo.entity.BMI;
import com.chh.healthy.backend.pojo.entity.Plan;
import com.chh.healthy.backend.pojo.query.BMIQuery;
import com.chh.healthy.backend.pojo.query.PlanQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlanMapper extends CommonMapper<Plan> {
    List<Plan> queryPlan (@Param("query") PlanQuery query);
}