package com.chh.healthy.backend.pojo.dto;

import com.boss.xtrain.core.common.pojo.dto.BaseDTO;
import com.chh.healthy.backend.pojo.entity.User;

import java.util.List;

public class ReplyDTO extends BaseDTO {
    private Long invitationId;

    private String content;

    private List<UserReplyDTO> userReplyList;

    private UserDTO user;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<UserReplyDTO> getUserReplyList() {
        return userReplyList;
    }

    public void setUserReplyList(List<UserReplyDTO> userReplyList) {
        this.userReplyList = userReplyList;
    }

    /**
     * @return invitation_id
     */
    public Long getInvitationId() {
        return invitationId;
    }

    /**
     * @param invitationId
     */
    public void setInvitationId(Long invitationId) {
        this.invitationId = invitationId;
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