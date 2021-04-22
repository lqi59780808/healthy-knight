package com.chh.healthy.backend.pojo.dto;

import com.boss.xtrain.core.common.pojo.dto.BaseDTO;
import com.boss.xtrain.core.common.pojo.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Table;

@Data
public class StepDTO extends BaseDTO {
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