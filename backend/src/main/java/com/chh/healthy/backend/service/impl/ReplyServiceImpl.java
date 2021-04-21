package com.chh.healthy.backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.boss.xtrain.core.annotation.stuffer.IdGenerator;
import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.boss.xtrain.core.common.api.CommonResponseUtils;
import com.boss.xtrain.core.common.exception.ServiceException;
import com.boss.xtrain.core.common.service.BaseCURDService;
import com.boss.xtrain.core.context.BaseContextHolder;
import com.boss.xtrain.util.BeanUtil;
import com.chh.healthy.backend.dao.impl.ReplyDAO;
import com.chh.healthy.backend.dao.impl.UserDAO;
import com.chh.healthy.backend.dao.mapper.UserMapper;
import com.chh.healthy.backend.pojo.code.ErrorCode;
import com.chh.healthy.backend.pojo.dto.InvitationDTO;
import com.chh.healthy.backend.pojo.dto.ReplyDTO;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.entity.Invitation;
import com.chh.healthy.backend.pojo.entity.Reply;
import com.chh.healthy.backend.pojo.entity.User;
import com.chh.healthy.backend.pojo.query.ReplyQuery;
import com.chh.healthy.backend.pojo.query.UserQuery;
import com.chh.healthy.backend.service.ReplyService;
import com.chh.healthy.backend.service.UserService;
import com.chh.healthy.backend.utils.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    ReplyDAO myDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResponse<ReplyDTO> doReplyMaster(ReplyDTO request) {
        try {
            Reply reply;
            reply = BeanUtil.copy(request,Reply.class);
            Reply response = myDao.saveAndReturn(reply);
            return CommonResponseUtils.success(BeanUtil.copy(response,ReplyDTO.class));
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.REPLY_EXCEPTION,e);
        }
    }

    @Override
    public CommonResponse<List<ReplyDTO>> doQueryReply(ReplyQuery request) {
        try {
            List<Reply> enList;
            if (request.getReplyId() != null) {
                enList = myDao.queryByPage2(request);
            } else {
                enList = myDao.queryByPage(request);
            }
            BaseContextHolder.endPage(enList);
            List<ReplyDTO> dtoList = new ArrayList<>();
            for (Reply res : enList) {
                String json = JSON.toJSONString(res);
                dtoList.add(JSON.parseObject(json, ReplyDTO.class));
            }
            return CommonResponseUtils.success(dtoList);
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.QUERY_EXCEPTION,e);
        }
    }
}
