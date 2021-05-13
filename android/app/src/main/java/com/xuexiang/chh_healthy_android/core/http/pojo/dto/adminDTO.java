package com.xuexiang.chh_healthy_android.core.http.pojo.dto;


import com.xuexiang.chh_healthy_android.core.BaseDTO;

public class adminDTO extends BaseDTO {
    private Integer sum;

    private Integer male ;

    private Integer female;

    private Integer invitation;

    private Integer reply;

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public Integer getMale() {
        return male;
    }

    public void setMale(Integer male) {
        this.male = male;
    }

    public Integer getFemale() {
        return female;
    }

    public void setFemale(Integer female) {
        this.female = female;
    }

    public Integer getInvitation() {
        return invitation;
    }

    public void setInvitation(Integer invitation) {
        this.invitation = invitation;
    }

    public Integer getReply() {
        return reply;
    }

    public void setReply(Integer reply) {
        this.reply = reply;
    }
}