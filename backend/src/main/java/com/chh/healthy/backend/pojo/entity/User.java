package com.chh.healthy.backend.pojo.entity;

import com.boss.xtrain.core.common.pojo.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Table(name = "h_user")
@Data
public class User extends BaseEntity {
    private String username;

    private String password;

    private String nickname;

    private String email;

    /**
     * 0男1女
     */
    private Byte sex;

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取0男1女
     *
     * @return sex - 0男1女
     */
    public Byte getSex() {
        return sex;
    }

    /**
     * 设置0男1女
     *
     * @param sex 0男1女
     */
    public void setSex(Byte sex) {
        this.sex = sex;
    }
}