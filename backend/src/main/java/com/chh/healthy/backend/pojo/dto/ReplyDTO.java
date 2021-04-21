package com.chh.healthy.backend.pojo.dto;

import com.boss.xtrain.core.common.pojo.dto.BaseDTO;
import com.chh.healthy.backend.pojo.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class ReplyDTO extends BaseDTO {
    private Long invitationId;

    private String content;

    private UserDTO user;

    private UserDTO replyUser;

    private Long replyId;

    private Long replyUserId;

    List<ReplyDTO> userReplyList;

    private Integer comment;

    private Integer good;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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