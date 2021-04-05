/**
 * @file:  BaseDTO.java
 * @author: liang_xiaojian
 * @date:   2020/8/26 15:30
 * @copyright: 2020-2023 www.bosssoft.com.cn Inc. All rights reserved.
 */
package com.xuexiang.chh_healthy_android.core;

import java.io.Serializable;
import java.util.Date;

/**
 * @class BaseDTO
 * @classdesc DTO 从这里继承便于统一DTO接口和转型判断
 * @author liang_xiaojian
 * @date 2020/8/26  15:30
 * @version 1.0.0
 * @see
 * @since
 */
public abstract class BaseDTO implements Serializable {

    private static final long serialVersionUID = -4183652945764463929L;

    private Long id;

    private Byte status;

    private Long createdBy;

    private String createdTime;

    private Long updatedBy;

    private String updatedTime;

    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "BaseDTO{" +
                "id=" + id +
                ", status=" + status +
                ", createdBy=" + createdBy +
                ", createdTime=" + createdTime +
                ", updatedBy=" + updatedBy +
                ", updatedTime=" + updatedTime +
                ", version=" + version +
                '}';
    }
}
