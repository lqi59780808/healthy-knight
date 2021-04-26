package com.chh.healthy.backend.pojo.dto;

import com.boss.xtrain.core.common.pojo.dto.BaseDTO;
import com.boss.xtrain.core.common.pojo.entity.BaseEntity;
import com.chh.healthy.backend.pojo.entity.User;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Table(name = "h_plan")
@Data
public class PlanDTO extends BaseDTO {
    private String name;

    private String time;

    private List<PlanDTO> planList;

    @Transient
    private UserDTO user;

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