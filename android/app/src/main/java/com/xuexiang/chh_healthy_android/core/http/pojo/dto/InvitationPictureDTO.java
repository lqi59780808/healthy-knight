package com.xuexiang.chh_healthy_android.core.http.pojo.dto;


import com.xuexiang.chh_healthy_android.core.BaseDTO;

public class InvitationPictureDTO extends BaseDTO {
    private Long invitationId;

    private byte[] pic;

    private byte[] smallPic;

    public Long getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(Long invitationId) {
        this.invitationId = invitationId;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public byte[] getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(byte[] smallPic) {
        this.smallPic = smallPic;
    }
}