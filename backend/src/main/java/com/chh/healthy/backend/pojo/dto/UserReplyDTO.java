package com.chh.healthy.backend.pojo.dto;

import com.boss.xtrain.core.common.pojo.dto.BaseDTO;
import com.chh.healthy.backend.pojo.entity.User;

public class UserReplyDTO extends BaseDTO {
    private Long replyId;

    private Long userId;

    private String content;

    private UserDTO user;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

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