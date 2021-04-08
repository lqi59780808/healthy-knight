/**
 * @file: BaseVO.java
 * @author: liang_xiaojian
 * @date: 2020/8/26 15:33
 * @copyright: 2020-2023 www.bosssoft.com.cn Inc. All rights reserved.
 */
package com.xuexiang.chh_healthy_android.core;


import java.io.Serializable;
import java.util.Date;

/**
 * @class BaseVO
 * @classdesc
 * @author liang_xiaojian
 * @date 2020/8/26  15:33
 * @version 1.0.0
 * @see
 * @since
 */
public abstract class BaseVO implements Serializable {

    private static final long serialVersionUID = -2361201137582920579L;

    private Long id;

    private Long createdBy;

    private String creator;

    private Long updatedBy;

    private String updater;

    private Date createdTime;

    private Date updatedTime;

    private Byte status;

    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
