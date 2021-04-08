package com.chh.healthy.backend.pojo.vo;

import com.boss.xtrain.core.common.pojo.vo.BaseVO;
import lombok.Data;

import javax.persistence.Table;

@Table(name = "t_user")
@Data
public class UserVO extends BaseVO {

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