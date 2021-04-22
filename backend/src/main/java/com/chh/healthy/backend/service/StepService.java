package com.chh.healthy.backend.service;

import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.chh.healthy.backend.pojo.dto.ReplyDTO;
import com.chh.healthy.backend.pojo.dto.StepDTO;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.query.ReplyQuery;
import com.chh.healthy.backend.pojo.query.StepQuery;

import java.util.List;

public interface StepService {

    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<java.lang.Integer>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<List<StepDTO>> queryStep(StepQuery request);

    CommonResponse<StepDTO> doUpdate(StepDTO request);

    CommonResponse<StepDTO> doSave(StepDTO request);
}
