package com.chh.healthy.backend.dao.impl;

import com.boss.xtrain.core.annotation.stuffer.EntityFieldStuffer;
import com.boss.xtrain.core.annotation.stuffer.MethodTypeEnum;
import com.boss.xtrain.core.common.dao.impl.AbstractBaseDao;
import com.chh.healthy.backend.dao.mapper.InvitationMapper;
import com.chh.healthy.backend.dao.mapper.InvitationPictureMapper;
import com.chh.healthy.backend.pojo.entity.Invitation;
import com.chh.healthy.backend.pojo.entity.InvitationPicture;
import com.chh.healthy.backend.pojo.query.InvitationQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class InvitationPictureDAO {
    @Autowired
    InvitationPictureMapper myMapper;

    @EntityFieldStuffer(methodType = MethodTypeEnum.ADD)
    public InvitationPicture saveAndReturn(InvitationPicture entity) {
        myMapper.insert(entity);
        return entity;
    }


    @EntityFieldStuffer(methodType = MethodTypeEnum.UPDATE)
    public InvitationPicture updateAndReturn(InvitationPicture entity) {
        myMapper.updateByPrimaryKeySelective(entity);
        return entity;
    }
}