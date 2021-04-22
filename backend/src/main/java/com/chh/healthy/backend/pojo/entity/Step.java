package com.chh.healthy.backend.pojo.entity;

import com.boss.xtrain.core.common.pojo.entity.BaseEntity;
import javax.persistence.*;

@Table(name = "h_step")
public class Step extends BaseEntity {
    private String today;

    private String step;

    /**
     * @return today
     */
    public String getToday() {
        return today;
    }

    /**
     * @param today
     */
    public void setToday(String today) {
        this.today = today;
    }

    /**
     * @return step
     */
    public String getStep() {
        return step;
    }

    /**
     * @param step
     */
    public void setStep(String step) {
        this.step = step;
    }
}