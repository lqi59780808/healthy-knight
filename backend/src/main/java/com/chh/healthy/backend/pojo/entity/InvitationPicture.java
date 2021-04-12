package com.chh.healthy.backend.pojo.entity;

import com.boss.xtrain.core.common.pojo.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Table(name = "h_invitation_picture")
@Data
public class InvitationPicture extends BaseEntity {
    @Column(name = "invitation_id")
    private Long invitationId;

    private String url;
}