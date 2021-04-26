package com.chh.healthy.backend.pojo.query;

import com.boss.xtrain.core.common.pojo.entity.BaseEntity;
import com.boss.xtrain.core.common.pojo.vo.BaseQuery;
import com.chh.healthy.backend.pojo.entity.User;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "h_plan")
@Data
public class PlanQuery extends BaseQuery {
    private String name;

    private String time;

    private Long createdBy;

    @Transient
    private User user;

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }
}