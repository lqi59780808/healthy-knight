package com.chh.healthy.backend.pojo.query;

import com.boss.xtrain.core.common.pojo.vo.BaseQuery;
import lombok.Data;

@Data
public class BMIQuery extends BaseQuery {
    private String weight;

    private String height;

    private String bmi;

    private String today;

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

}