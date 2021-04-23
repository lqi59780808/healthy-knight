package com.chh.healthy.backend.service;

import com.boss.xtrain.core.common.api.CommonResponse;
import com.chh.healthy.backend.pojo.dto.BMIDTO;
import com.chh.healthy.backend.pojo.dto.StepDTO;
import com.chh.healthy.backend.pojo.query.BMIQuery;
import com.chh.healthy.backend.pojo.query.StepQuery;

import java.util.List;

public interface BMIService {

    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<java.lang.Integer>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<List<BMIDTO>> queryBMI(BMIQuery request);

    CommonResponse<BMIDTO> doUpdate(BMIDTO request);

    CommonResponse<BMIDTO> doSave(BMIDTO request);
}
