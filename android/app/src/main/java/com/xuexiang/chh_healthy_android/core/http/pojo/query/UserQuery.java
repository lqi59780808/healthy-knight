package com.xuexiang.chh_healthy_android.core.http.pojo.query;


import com.xuexiang.chh_healthy_android.core.BaseQuery;

public class UserQuery extends BaseQuery {

    private String username;

    private String password;

    private String nickname;
    /**
     * 0男1女
     */
    private Byte sex;

    private String email;

    private String icon;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}