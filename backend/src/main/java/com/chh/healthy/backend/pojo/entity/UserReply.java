package com.chh.healthy.backend.pojo.entity;

import com.boss.xtrain.core.common.pojo.entity.BaseEntity;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import lombok.Data;

import javax.persistence.*;

@Table(name = "h_user_reply")
@Data
public class UserReply extends BaseEntity {
    @Column(name = "reply_id")
    private Long replyId;

    @Column(name = "user_id")
    private Long userId;

    private String content;

    User user;


    /**
     * @return reply_id
     */
    public Long getReplyId() {
        return replyId;
    }

    /**
     * @param replyId
     */
    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    /**
     * @return user_id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }
}