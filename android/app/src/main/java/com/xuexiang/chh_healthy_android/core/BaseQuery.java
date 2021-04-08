/**
 * @file:  BaseQuery.java
 * @author: liang_xiaojian
 * @date:   2020/8/11 16:00
 * @copyright: 2020-2023 www.bosssoft.com.cn Inc. All rights reserved.
 */
package com.xuexiang.chh_healthy_android.core;

import java.io.Serializable;

/**
 * @class BaseQuery
 * @classdesc
 * @author liang_xiaojian
 * @date 2020/8/11  16:00
 * @version 1.0.0
 * @see
 * @since
 */
public abstract class BaseQuery implements Serializable {

    private static final long serialVersionUID = -7201448647817960504L;

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
