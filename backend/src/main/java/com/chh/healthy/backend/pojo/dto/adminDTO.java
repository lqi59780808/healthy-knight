package com.chh.healthy.backend.pojo.dto;

import com.boss.xtrain.core.common.pojo.dto.BaseDTO;
import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class adminDTO extends BaseDTO {
    private Integer sum;

    private Integer male ;

    private Integer female;

    private Integer invitation;

    private Integer reply;


}