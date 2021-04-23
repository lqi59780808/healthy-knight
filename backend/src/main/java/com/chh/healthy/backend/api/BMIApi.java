package com.chh.healthy.backend.api;

import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.chh.healthy.backend.pojo.dto.BMIDTO;
import com.chh.healthy.backend.pojo.dto.StepDTO;
import com.chh.healthy.backend.pojo.query.BMIQuery;
import com.chh.healthy.backend.pojo.query.StepQuery;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/healthy/bmi")
public interface BMIApi {
    @PostMapping("/query")
    @ApiOperation(value = "查询")
    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<java.lang.Integer>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<List<BMIDTO>> queryBMI(CommonRequest<BMIQuery> request);

    @PostMapping("/save")
    @ApiOperation(value = "保存")
    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<java.lang.Integer>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<BMIDTO> save(CommonRequest<BMIDTO> request);

    @PostMapping("/update")
    @ApiOperation(value = "更新")
    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<java.lang.Integer>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<BMIDTO> update(CommonRequest<BMIDTO> request);
}
