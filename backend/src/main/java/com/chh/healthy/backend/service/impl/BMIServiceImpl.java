package com.chh.healthy.backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.boss.xtrain.core.common.api.CommonResponseUtils;
import com.boss.xtrain.core.common.exception.ServiceException;
import com.boss.xtrain.core.context.BaseContextHolder;
import com.boss.xtrain.util.BeanUtil;
import com.chh.healthy.backend.dao.impl.BMIDAO;
import com.chh.healthy.backend.dao.impl.StepDAO;
import com.chh.healthy.backend.pojo.code.ErrorCode;
import com.chh.healthy.backend.pojo.dto.BMIDTO;
import com.chh.healthy.backend.pojo.dto.StepDTO;
import com.chh.healthy.backend.pojo.entity.BMI;
import com.chh.healthy.backend.pojo.entity.Step;
import com.chh.healthy.backend.pojo.query.BMIQuery;
import com.chh.healthy.backend.pojo.query.StepQuery;
import com.chh.healthy.backend.service.BMIService;
import com.chh.healthy.backend.service.StepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BMIServiceImpl implements BMIService {
    @Autowired
    BMIDAO myDao;

    @Override
    public CommonResponse<List<BMIDTO>> queryBMI(BMIQuery request) {
        try {
            List<BMI> enList = myDao.queryBMI(request);
            List<BMIDTO> dtoList = new ArrayList<>();
            for (BMI res : enList) {
                String json = JSON.toJSONString(res);
                dtoList.add(JSON.parseObject(json, BMIDTO.class));
            }
            return CommonResponseUtils.success(dtoList);
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.QUERY_EXCEPTION,e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResponse<BMIDTO> doUpdate(BMIDTO request) {
        try {
            BMI res = myDao.updateAndReturn(BeanUtil.copy(request,BMI.class));
            return CommonResponseUtils.success(BeanUtil.copy(res,BMIDTO.class));
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.UPDATE_EXCEPTION,e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResponse<BMIDTO> doSave(BMIDTO request) {
        try {
            BaseContextHolder.setUserId(request.getCreatedBy());
            BMI res = myDao.saveAndReturn(BeanUtil.copy(request,BMI.class));
            return CommonResponseUtils.success(BeanUtil.copy(res,BMIDTO.class));
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.SAVE_EXCEPTION,e);
        }
    }
}
