package com.chh.healthy.backend.dao.mapper;

import com.boss.xtrain.core.common.dao.CommonMapper;
import com.chh.healthy.backend.pojo.entity.Reply;
import com.chh.healthy.backend.pojo.entity.Step;
import com.chh.healthy.backend.pojo.query.ReplyQuery;
import com.chh.healthy.backend.pojo.query.StepQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StepMapper extends CommonMapper<Step> {

    List<Step> queryStep (@Param("query") StepQuery query);
}