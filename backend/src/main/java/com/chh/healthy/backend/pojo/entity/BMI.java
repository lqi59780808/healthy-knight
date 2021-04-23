package com.chh.healthy.backend.pojo.entity;

import com.boss.xtrain.core.common.pojo.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Table(name = "h_bmi")
@Data
public class BMI extends BaseEntity {
    private String weight;

    private String height;

    private String bmi;

    private String today;

    @Transient
    private User user;

    /**
     * @return weight
     */
    public String getWeight() {
        return weight;
    }

    /**
     * @param weight
     */
    public void setWeight(String weight) {
        this.weight = weight;
    }

    /**
     * @return height
     */
    public String getHeight() {
        return height;
    }

    /**
     * @param height
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * @return bmi
     */
    public String getBmi() {
        return bmi;
    }

    /**
     * @param bmi
     */
    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

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