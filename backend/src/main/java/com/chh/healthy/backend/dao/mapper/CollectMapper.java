package com.chh.healthy.backend.dao.mapper;

import com.boss.xtrain.core.common.dao.CommonMapper;
import com.chh.healthy.backend.pojo.entity.BMI;
import com.chh.healthy.backend.pojo.entity.Collect;
import com.chh.healthy.backend.pojo.query.BMIQuery;
import com.chh.healthy.backend.pojo.query.CollectQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CollectMapper extends CommonMapper<Collect> {
    List<Collect> queryCollect (@Param("query") CollectQuery query);
}