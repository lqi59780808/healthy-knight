package com.chh.healthy.backend.pojo.entity;

import com.boss.xtrain.core.common.pojo.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Table(name = "h_good")
@Data
public class Good extends BaseEntity {
    @Column(name = "invitation_id")
    private Long invitationId;

    @Transient
    private User user;

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
}