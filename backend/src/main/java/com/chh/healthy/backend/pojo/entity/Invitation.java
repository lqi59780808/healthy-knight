package com.chh.healthy.backend.pojo.entity;

import com.boss.xtrain.core.common.pojo.entity.BaseEntity;
import com.chh.healthy.backend.pojo.dto.InvitationPictureDTO;
import com.chh.healthy.backend.pojo.dto.ReplyDTO;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Table(name = "h_invitation")
@Data
public class Invitation extends BaseEntity {
    private String title;

    /**
     * 点赞数
     */
    private Integer good;

    /**
     * 收藏数
     */
    private Integer collect;

    private Integer click;

    private Integer type;

    private String content;

    private Integer comment;

    @Transient
    private List<InvitationPicture> pictureList;

    @Transient
    private List<Reply> replyList;

    @Transient
    private User user;

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
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