package com.chh.healthy.backend.dao.impl;

import com.boss.xtrain.core.annotation.stuffer.EntityFieldStuffer;
import com.boss.xtrain.core.annotation.stuffer.MethodTypeEnum;
import com.boss.xtrain.core.common.dao.impl.AbstractBaseDao;
import com.chh.healthy.backend.dao.mapper.InvitationMapper;
import com.chh.healthy.backend.dao.mapper.UserMapper;
import com.chh.healthy.backend.pojo.entity.Invitation;
import com.chh.healthy.backend.pojo.entity.User;
import com.chh.healthy.backend.pojo.query.InvitationQuery;
import com.chh.healthy.backend.pojo.query.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class InvitationDAO extends AbstractBaseDao<Invitation, InvitationMapper, InvitationQuery> {
    public InvitationDAO(@Autowired InvitationMapper mapper) {
        myMapper = mapper;
    }

    @Override
    @EntityFieldStuffer(methodType = MethodTypeEnum.ADD)
    public int save(Invitation entity) {
        return super.save(entity);
    }

    @EntityFieldStuffer(methodType = MethodTypeEnum.ADD)
    public Invitation saveAndReturn(Invitation entity) {
        myMapper.insert(entity);
        return entity;
    }

    @Override
    @EntityFieldStuffer(methodType = MethodTypeEnum.UPDATE)
    public int update(Invitation entity) {
        return super.update(entity);
    }

    @EntityFieldStuffer(methodType = MethodTypeEnum.UPDATE)
    public Invitation updateAndReturn(Invitation entity) {
        myMapper.updateByPrimaryKeySelective(entity);
        return entity;
    }

    public List<Invitation> queryInvitation(InvitationQuery query) {
        return myMapper.queryInvitation(query);
    }

    public List<Invitation> queryInvitationById(Long id) {
        return myMapper.queryInvitationById(id);
    }
}