package com.chh.healthy.backend.dao.mapper;

import com.boss.xtrain.core.common.dao.CommonMapper;
import com.chh.healthy.backend.pojo.entity.BMI;
import com.chh.healthy.backend.pojo.entity.Good;
import com.chh.healthy.backend.pojo.query.BMIQuery;
import com.chh.healthy.backend.pojo.query.GoodQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodMapper extends CommonMapper<Good> {
    List<Good> queryGood (@Param("query") GoodQuery query);
}