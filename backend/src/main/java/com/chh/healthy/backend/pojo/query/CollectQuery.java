package com.chh.healthy.backend.pojo.query;

import com.boss.xtrain.core.common.pojo.entity.BaseEntity;
import com.boss.xtrain.core.common.pojo.vo.BaseQuery;
import com.chh.healthy.backend.pojo.entity.User;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "h_collect")
@Data
public class CollectQuery extends BaseQuery {
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