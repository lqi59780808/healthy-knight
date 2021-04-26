package com.xuexiang.chh_healthy_android.core.http.pojo.dto;


import com.xuexiang.chh_healthy_android.core.BaseDTO;

import java.util.List;

public class PlanDTO extends BaseDTO {
    private String name;

    private String time;

    private UserDTO user;

    private List<PlanDTO> planList;

    public List<PlanDTO> getPlanList() {
        return planList;
    }

    public void setPlanList(List<PlanDTO> planList) {
        this.planList = planList;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }
}