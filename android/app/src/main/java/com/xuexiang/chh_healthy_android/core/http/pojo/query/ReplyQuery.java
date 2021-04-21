package com.xuexiang.chh_healthy_android.core.http.pojo.query;


import com.xuexiang.chh_healthy_android.core.BaseQuery;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.UserDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.UserReplyDTO;

import java.util.List;

public class ReplyQuery extends BaseQuery {
    private Long invitationId;

    private String content;

    private UserDTO user;

    private Long replyId;

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

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