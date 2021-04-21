package com.xuexiang.chh_healthy_android.core.http.pojo.dto;


import com.xuexiang.chh_healthy_android.core.BaseDTO;

import java.util.List;

public class ReplyDTO extends BaseDTO {
    private Long invitationId;

    private Long replyId;

    private Long replyUserId;

    private String content;

    private List<ReplyDTO> userReplyList;

    private UserDTO replyUser;

    private Integer comment;

    private Integer good;

    private UserDTO user;

    public UserDTO getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(UserDTO replyUser) {
        this.replyUser = replyUser;
    }


    public Long getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(Long replyUserId) {
        this.replyUserId = replyUserId;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public Integer getGood() {
        return good;
    }

    public void setGood(Integer good) {
        this.good = good;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<ReplyDTO> getUserReplyList() {
        return userReplyList;
    }

    public void setUserReplyList(List<ReplyDTO> userReplyList) {
        this.userReplyList = userReplyList;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
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