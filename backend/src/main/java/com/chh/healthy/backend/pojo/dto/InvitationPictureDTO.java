package com.chh.healthy.backend.pojo.dto;

import com.boss.xtrain.core.common.pojo.dto.BaseDTO;
import com.boss.xtrain.core.common.pojo.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
public class InvitationPictureDTO extends BaseDTO {
    private Long invitationId;

    private String url;
}