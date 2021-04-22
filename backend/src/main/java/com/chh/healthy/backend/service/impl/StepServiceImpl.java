package com.chh.healthy.backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.boss.xtrain.core.common.api.CommonResponseUtils;
import com.boss.xtrain.core.common.exception.ServiceException;
import com.boss.xtrain.core.context.BaseContextHolder;
import com.boss.xtrain.util.BeanUtil;
import com.chh.healthy.backend.dao.impl.ReplyDAO;
import com.chh.healthy.backend.dao.impl.StepDAO;
import com.chh.healthy.backend.pojo.code.ErrorCode;
import com.chh.healthy.backend.pojo.dto.ReplyDTO;
import com.chh.healthy.backend.pojo.dto.StepDTO;
import com.chh.healthy.backend.pojo.entity.Reply;
import com.chh.healthy.backend.pojo.entity.Step;
import com.chh.healthy.backend.pojo.query.ReplyQuery;
import com.chh.healthy.backend.pojo.query.StepQuery;
import com.chh.healthy.backend.service.ReplyService;
import com.chh.healthy.backend.service.StepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StepServiceImpl implements StepService {
    @Autowired
    StepDAO myDao;

    @Override
    public CommonResponse<List<StepDTO>> queryStep(StepQuery request) {
        try {
            List<Step> enList = myDao.queryStep(request);
            List<StepDTO> dtoList = new ArrayList<>();
            for (Step res : enList) {
                String json = JSON.toJSONString(res);
                dtoList.add(JSON.parseObject(json, StepDTO.class));
            }
            return CommonResponseUtils.success(dtoList);
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.QUERY_EXCEPTION,e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResponse<StepDTO> doUpdate(StepDTO request) {
        try {
            Step res = myDao.updateAndReturn(BeanUtil.copy(request,Step.class));
            return CommonResponseUtils.success(BeanUtil.copy(res,StepDTO.class));
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.UPDATE_EXCEPTION,e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResponse<StepDTO> doSave(StepDTO request) {
        try {
            BaseContextHolder.setUserId(request.getCreatedBy());
            Step res = myDao.saveAndReturn(BeanUtil.copy(request,Step.class));
            return CommonResponseUtils.success(BeanUtil.copy(res,StepDTO.class));
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.SAVE_EXCEPTION,e);
        }
    }
}
