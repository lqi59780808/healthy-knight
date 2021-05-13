package com.chh.healthy.backend.dao.impl;

import com.boss.xtrain.core.annotation.stuffer.EntityFieldStuffer;
import com.boss.xtrain.core.annotation.stuffer.MethodTypeEnum;
import com.boss.xtrain.core.common.dao.impl.AbstractBaseDao;
import com.chh.healthy.backend.dao.mapper.InvitationMapper;
import com.chh.healthy.backend.dao.mapper.ReplyMapper;
import com.chh.healthy.backend.dao.mapper.UserMapper;
import com.chh.healthy.backend.pojo.entity.Invitation;
import com.chh.healthy.backend.pojo.entity.Reply;
import com.chh.healthy.backend.pojo.entity.User;
import com.chh.healthy.backend.pojo.query.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserDAO extends AbstractBaseDao<User, UserMapper, UserQuery> {
    public UserDAO(@Autowired UserMapper userMapper) {
        myMapper = userMapper;
    }

    @Autowired
    InvitationMapper invitationMapper;
    @Autowired
    ReplyMapper replyMapper;

    @Override
    @EntityFieldStuffer(methodType = MethodTypeEnum.ADD)
    public int save(User entity) {
        return super.save(entity);
    }

    @EntityFieldStuffer(methodType = MethodTypeEnum.ADD)
    public User saveAndReturn(User entity) {
        myMapper.insert(entity);
        return entity;
    }

    @Override
    @EntityFieldStuffer(methodType = MethodTypeEnum.UPDATE)
    public int update(User user) {
        return super.update(user);
    }

    @EntityFieldStuffer(methodType = MethodTypeEnum.UPDATE)
    public User updateAndReturn(User user) {
        myMapper.updateByPrimaryKeySelective(user);
        return user;
    }

    public User queryByUsername(User user) {
        return myMapper.selectOne(user);
    }

    public Integer countUser() {
        User user = new User();
        user.setStatus((byte) 2);
        return myMapper.selectCount(user);
    }
    public Integer countMale() {
        User user = new User();
        user.setStatus((byte) 2);
        user.setSex((byte) 0);
        return myMapper.selectCount(user);
    }
    public Integer countFemale() {
        User user = new User();
        user.setStatus((byte) 2);
        user.setSex((byte) 1);
        return myMapper.selectCount(user);
    }
    public Integer countInvitation() {
        Invitation invitation = new Invitation();
        invitation.setStatus((byte) 1);
        return invitationMapper.selectCount(invitation);
    }
    public Integer countReply() {
        Reply reply = new Reply();
        reply.setStatus((byte) 1);
        return replyMapper.selectCount(reply);
    }
    public List<User> queryUserList(UserQuery query) {
        return myMapper.queryUserList(query);
    }
}
