package com.chh.healthy.backend.api;

import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.chh.healthy.backend.pojo.dto.ReplyDTO;
import com.chh.healthy.backend.pojo.dto.StepDTO;
import com.chh.healthy.backend.pojo.query.ReplyQuery;
import com.chh.healthy.backend.pojo.query.StepQuery;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/healthy/step")
public interface StepApi {
    @PostMapping("/query")
    @ApiOperation(value = "查询步数")
    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<java.lang.Integer>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<List<StepDTO>> queryStep(CommonRequest<StepQuery> request);

    @PostMapping("/save")
    @ApiOperation(value = "保存步数")
    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<java.lang.Integer>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<StepDTO> save(CommonRequest<StepDTO> request);

    @PostMapping("/update")
    @ApiOperation(value = "更新步数")
    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<java.lang.Integer>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<StepDTO> update(CommonRequest<StepDTO> request);
}
