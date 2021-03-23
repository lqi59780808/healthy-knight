package com.chh.healthy.backend.pojo.query;

import com.boss.xtrain.core.common.pojo.vo.BaseQuery;
import lombok.Data;

import javax.persistence.Table;

@Table(name = "t_user")
@Data
public class UserQuery extends BaseQuery {

    private String username;

    private String password;

    private String nickname;
    /**
     * 0男1女
     */
    private Byte sex;

}