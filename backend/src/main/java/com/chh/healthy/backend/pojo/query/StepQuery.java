package com.chh.healthy.backend.pojo.query;

import com.boss.xtrain.core.common.pojo.dto.BaseDTO;
import com.boss.xtrain.core.common.pojo.vo.BaseQuery;
import lombok.Data;

@Data
public class StepQuery extends BaseQuery {
    private String today;

    private String step;

    private Long createdBy;

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