package com.chh.healthy.backend.dao.mapper;

import com.boss.xtrain.core.common.dao.CommonMapper;
import com.chh.healthy.backend.pojo.entity.Invitation;
import com.chh.healthy.backend.pojo.query.InvitationQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InvitationMapper extends CommonMapper<Invitation> {


    /**
     * @param: [query]
     * @return: java.util.List<com.chh.healthy.backend.pojo.entity.Invitation>
     * @desc: 查询帖子
     * @see
     * @since
     */
    List<Invitation> queryInvitation(@Param("query") InvitationQuery query);

    /**
     * @param: [query]
     * @return: java.util.List<com.chh.healthy.backend.pojo.entity.Invitation>
     * @desc: 查询帖子
     * @see
     * @since
     */
    List<Invitation> queryInvitationCollect(@Param("query") InvitationQuery query);

    /**
     * @param: [query]
     * @return: java.util.List<com.chh.healthy.backend.pojo.entity.Invitation>
     * @desc: 查询帖子
     * @see
     * @since
     */
    List<Invitation> queryInvitationById(@Param("query") Long query);

}