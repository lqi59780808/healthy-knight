package com.chh.healthy.backend.pojo.entity;

import com.boss.xtrain.core.common.pojo.entity.BaseEntity;
import com.chh.healthy.backend.pojo.dto.ReplyDTO;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.dto.UserReplyDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Table(name = "h_reply")
@Data
public class Reply extends BaseEntity {
    @Column(name = "invitation_id")
    private Long invitationId;

    private String content;

    @Transient
    private User user;

    @Transient
    private User replyUser;

    private Long replyId;

    private Long replyUserId;

    private Integer comment;

    private Integer good;

    @Transient
    List<Reply> userReplyList;
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