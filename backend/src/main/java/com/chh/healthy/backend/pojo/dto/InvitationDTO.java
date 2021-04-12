package com.chh.healthy.backend.pojo.dto;

import com.boss.xtrain.core.common.pojo.dto.BaseDTO;
import com.chh.healthy.backend.pojo.entity.User;
import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

@Data
public class InvitationDTO extends BaseDTO {
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

    private UserDTO user;

    private List<InvitationPictureDTO> pictureList;
}