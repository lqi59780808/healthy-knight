package com.xuexiang.chh_healthy_android.core.http.pojo.query;


import com.xuexiang.chh_healthy_android.core.BaseQuery;

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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}