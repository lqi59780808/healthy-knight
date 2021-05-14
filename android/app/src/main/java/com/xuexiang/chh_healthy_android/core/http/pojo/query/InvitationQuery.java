package com.xuexiang.chh_healthy_android.core.http.pojo.query;


import com.xuexiang.chh_healthy_android.core.BaseQuery;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.UserDTO;

public class InvitationQuery extends BaseQuery {
    private String title;

    /**
     * 点赞数
     */
    private Integer good;

    private Long queryId;

    /**
     * 收藏数
     */
    private Integer collect;

    private Integer click;

    private Integer type;

    private String content;

    private UserDTO user;

    private Long requestUserId;

    private Long requestInvitationId;

    private Long createdBy;

    private Long collectBy;

    public Long getQueryId() {
        return queryId;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    public Long getCollectBy() {
        return collectBy;
    }

    public void setCollectBy(Long collectBy) {
        this.collectBy = collectBy;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getRequestUserId() {
        return requestUserId;
    }

    public void setRequestUserId(Long requestUserId) {
        this.requestUserId = requestUserId;
    }

    public Long getRequestInvitationId() {
        return requestInvitationId;
    }

    public void setRequestInvitationId(Long requestInvitationId) {
        this.requestInvitationId = requestInvitationId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取点赞数
     *
     * @return good - 点赞数
     */
    public Integer getGood() {
        return good;
    }

    /**
     * 设置点赞数
     *
     * @param good 点赞数
     */
    public void setGood(Integer good) {
        this.good = good;
    }

    /**
     * 获取收藏数
     *
     * @return collect - 收藏数
     */
    public Integer getCollect() {
        return collect;
    }

    /**
     * 设置收藏数
     *
     * @param collect 收藏数
     */
    public void setCollect(Integer collect) {
        this.collect = collect;
    }

    /**
     * @return click
     */
    public Integer getClick() {
        return click;
    }

    /**
     * @param click
     */
    public void setClick(Integer click) {
        this.click = click;
    }

    /**
     * @return type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
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