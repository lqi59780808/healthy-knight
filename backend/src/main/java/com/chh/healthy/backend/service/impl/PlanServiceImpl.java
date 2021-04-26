package com.chh.healthy.backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.boss.xtrain.core.common.api.CommonResponseUtils;
import com.boss.xtrain.core.common.exception.ServiceException;
import com.boss.xtrain.core.context.BaseContextHolder;
import com.boss.xtrain.util.BeanUtil;
import com.chh.healthy.backend.dao.impl.PlanDAO;
import com.chh.healthy.backend.dao.impl.StepDAO;
import com.chh.healthy.backend.pojo.code.ErrorCode;
import com.chh.healthy.backend.pojo.dto.PlanDTO;
import com.chh.healthy.backend.pojo.dto.StepDTO;
import com.chh.healthy.backend.pojo.entity.Plan;
import com.chh.healthy.backend.pojo.entity.Step;
import com.chh.healthy.backend.pojo.query.PlanQuery;
import com.chh.healthy.backend.pojo.query.StepQuery;
import com.chh.healthy.backend.service.PlanService;
import com.chh.healthy.backend.service.StepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {
    @Autowired
    PlanDAO myDao;

    @Override
    public CommonResponse<List<PlanDTO>> queryPlan(PlanQuery request) {
        try {
            List<Plan> enList = myDao.queryPlan(request);
            List<PlanDTO> dtoList = new ArrayList<>();
            for (Plan res : enList) {
                String json = JSON.toJSONString(res);
                dtoList.add(JSON.parseObject(json, PlanDTO.class));
            }
            return CommonResponseUtils.success(dtoList);
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.QUERY_EXCEPTION,e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResponse<PlanDTO> doSave(PlanDTO request) {
        try {
            BaseContextHolder.setUserId(request.getCreatedBy());
            myDao.deletePlan(request.getCreatedBy());
            List<PlanDTO> resList = new ArrayList<>();
            for (PlanDTO dto : request.getPlanList()) {
                Plan res = myDao.saveAndReturn(BeanUtil.copy(dto,Plan.class));
                resList.add(BeanUtil.copy(res,PlanDTO.class));
            }
            request.setPlanList(resList);
            return CommonResponseUtils.success(request);
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.SAVE_EXCEPTION,e);
        }
    }
}
