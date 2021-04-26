package com.chh.healthy.backend.controller;

import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.chh.healthy.backend.api.BMIApi;
import com.chh.healthy.backend.api.PlanApi;
import com.chh.healthy.backend.pojo.dto.BMIDTO;
import com.chh.healthy.backend.pojo.dto.PlanDTO;
import com.chh.healthy.backend.pojo.query.BMIQuery;
import com.chh.healthy.backend.pojo.query.PlanQuery;
import com.chh.healthy.backend.service.BMIService;
import com.chh.healthy.backend.service.PlanService;
import com.github.pagehelper.page.PageMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlanController implements PlanApi {

    @Autowired
    PlanService service;

    @Override
    public CommonResponse<List<PlanDTO>> queryPlan(@RequestBody CommonRequest<PlanQuery> request) {
        return service.queryPlan(request.getBody());
    }

    @Override
    public CommonResponse<PlanDTO> save(@RequestBody CommonRequest<PlanDTO> request) {
        return service.doSave(request.getBody());
    }
}
