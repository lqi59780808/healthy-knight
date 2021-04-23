package com.chh.healthy.backend.controller;

import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.chh.healthy.backend.api.BMIApi;
import com.chh.healthy.backend.api.StepApi;
import com.chh.healthy.backend.pojo.dto.BMIDTO;
import com.chh.healthy.backend.pojo.dto.StepDTO;
import com.chh.healthy.backend.pojo.query.BMIQuery;
import com.chh.healthy.backend.pojo.query.StepQuery;
import com.chh.healthy.backend.service.BMIService;
import com.chh.healthy.backend.service.StepService;
import com.github.pagehelper.page.PageMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BMIController implements BMIApi {

    @Autowired
    BMIService service;

    @Override
    public CommonResponse<List<BMIDTO>> queryBMI(@RequestBody CommonRequest<BMIQuery> request) {
        if (request.getBody().getPageNum() != null) {
            PageMethod.startPage(request.getBody().getPageNum(),request.getBody().getPageSize());
        }
        return service.queryBMI(request.getBody());
    }

    @Override
    public CommonResponse<BMIDTO> save(@RequestBody CommonRequest<BMIDTO> request) {
        return service.doSave(request.getBody());
    }

    @Override
    public CommonResponse<BMIDTO> update(@RequestBody CommonRequest<BMIDTO> request) {
        return service.doUpdate(request.getBody());
    }
}
