package com.chh.healthy.backend.dao.mapper;

import com.boss.xtrain.core.common.dao.CommonMapper;
import com.chh.healthy.backend.pojo.entity.BMI;
import com.chh.healthy.backend.pojo.entity.Step;
import com.chh.healthy.backend.pojo.query.BMIQuery;
import com.chh.healthy.backend.pojo.query.StepQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BMIMapper extends CommonMapper<BMI> {
    List<BMI> queryBMI (@Param("query") BMIQuery query);
}