package com.xuexiang.chh_healthy_android.step.bean;

import com.xuexiang.chh_healthy_android.core.BaseDTO;

/**
 * Created by dylan on 2016/1/30.
 */

public class StepData extends BaseDTO {

    private String today;

    private String step;

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
