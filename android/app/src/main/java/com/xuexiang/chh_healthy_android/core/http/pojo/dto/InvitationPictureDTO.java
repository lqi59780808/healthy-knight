package com.xuexiang.chh_healthy_android.core.http.pojo.dto;


import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.xuexiang.chh_healthy_android.core.BaseDTO;
import com.xuexiang.xui.widget.imageview.preview.enitity.IPreviewInfo;

public class InvitationPictureDTO extends BaseDTO implements IPreviewInfo {
    private Long invitationId;

    private String url;

    public InvitationPictureDTO(Parcel source) {
        super();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(Long invitationId) {
        this.invitationId = invitationId;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Rect getBounds() {
        return null;
    }

    @Nullable
    @Override
    public String getVideoUrl() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }


    public static final Parcelable.Creator<InvitationPictureDTO> CREATOR = new Parcelable.Creator<InvitationPictureDTO>() {
        @Override
        public InvitationPictureDTO createFromParcel(Parcel source) {
            return new InvitationPictureDTO(source);
        }

        @Override
        public InvitationPictureDTO[] newArray(int size) {
            return new InvitationPictureDTO[size];
        }
    };
}