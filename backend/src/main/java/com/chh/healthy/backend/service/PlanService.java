package com.chh.healthy.backend.service;

import com.boss.xtrain.core.common.api.CommonResponse;
import com.chh.healthy.backend.pojo.dto.PlanDTO;
import com.chh.healthy.backend.pojo.dto.StepDTO;
import com.chh.healthy.backend.pojo.query.PlanQuery;
import com.chh.healthy.backend.pojo.query.StepQuery;

import java.util.List;

public interface PlanService {

    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<java.lang.Integer>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<List<PlanDTO>> queryPlan(PlanQuery request);

    CommonResponse<PlanDTO> doSave(PlanDTO request);
}
