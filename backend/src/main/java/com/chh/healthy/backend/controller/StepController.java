package com.chh.healthy.backend.controller;

import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.boss.xtrain.core.common.controller.BaseCRUDController;
import com.boss.xtrain.core.context.BaseContextHolder;
import com.chh.healthy.backend.api.ReplyApi;
import com.chh.healthy.backend.api.StepApi;
import com.chh.healthy.backend.dao.mapper.ReplyMapper;
import com.chh.healthy.backend.pojo.dto.ReplyDTO;
import com.chh.healthy.backend.pojo.dto.StepDTO;
import com.chh.healthy.backend.pojo.entity.Reply;
import com.chh.healthy.backend.pojo.query.ReplyQuery;
import com.chh.healthy.backend.pojo.query.StepQuery;
import com.chh.healthy.backend.pojo.vo.ReplyVO;
import com.chh.healthy.backend.service.ReplyService;
import com.chh.healthy.backend.service.StepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StepController implements StepApi {

    @Autowired
    StepService service;

    @Override
    public CommonResponse<List<StepDTO>> queryStep(@RequestBody CommonRequest<StepQuery> request) {
        return service.queryStep(request.getBody());
    }

    @Override
    public CommonResponse<StepDTO> save(@RequestBody CommonRequest<StepDTO> request) {
        return service.doSave(request.getBody());
    }

    @Override
    public CommonResponse<StepDTO> update(@RequestBody CommonRequest<StepDTO> request) {
        return service.doUpdate(request.getBody());
    }
}
