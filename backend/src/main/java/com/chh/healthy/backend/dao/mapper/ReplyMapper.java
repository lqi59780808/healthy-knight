package com.chh.healthy.backend.dao.mapper;

import com.boss.xtrain.core.common.dao.CommonMapper;
import com.chh.healthy.backend.pojo.entity.Reply;
import com.chh.healthy.backend.pojo.query.ReplyQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReplyMapper extends CommonMapper<Reply> {

    List<Reply> queryByPage (@Param("query") ReplyQuery query);

    List<Reply> queryByPage2 (@Param("query") ReplyQuery query);
}