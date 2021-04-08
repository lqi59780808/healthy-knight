package com.chh.healthy.backend.pojo.dto;

import com.boss.xtrain.core.common.pojo.dto.BaseDTO;
import lombok.Data;

import javax.persistence.Table;

@Table(name = "t_user")
@Data
public class UserDTO extends BaseDTO {

    private String username;

    private String password;

    private String nickname;
    /**
     * 0男1女
     */
    private Byte sex;

    private String email;

    private String icon;
}