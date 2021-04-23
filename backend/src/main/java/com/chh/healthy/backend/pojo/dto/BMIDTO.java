package com.chh.healthy.backend.pojo.dto;

import com.boss.xtrain.core.common.pojo.dto.BaseDTO;
import lombok.Data;

@Data
public class BMIDTO extends BaseDTO {
    private String weight;

    private String height;

    private String bmi;

    private String today;

    private UserDTO user;

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