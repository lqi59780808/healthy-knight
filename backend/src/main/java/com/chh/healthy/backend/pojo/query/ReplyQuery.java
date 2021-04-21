package com.chh.healthy.backend.pojo.query;

import com.boss.xtrain.core.common.pojo.vo.BaseQuery;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.dto.UserReplyDTO;
import lombok.Data;

import java.util.List;

@Data
public class ReplyQuery extends BaseQuery {
    private Long invitationId;

    private String content;

    private UserDTO user;

    private Long replyId;

    private Integer comment;

    private Integer good;

    public Long getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(Long invitationId) {
        this.invitationId = invitationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}