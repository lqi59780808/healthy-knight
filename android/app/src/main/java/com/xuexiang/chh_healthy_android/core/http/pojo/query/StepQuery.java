package com.xuexiang.chh_healthy_android.core.http.pojo.query;

import com.xuexiang.chh_healthy_android.core.BaseQuery;

/**
 * Created by dylan on 2016/1/30.
 */

public class StepQuery extends BaseQuery {

    private String today;

    private String step;

    private Long createdBy;

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
