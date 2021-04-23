package com.xuexiang.chh_healthy_android.core.http.pojo.dto;


import com.xuexiang.chh_healthy_android.core.BaseDTO;

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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}