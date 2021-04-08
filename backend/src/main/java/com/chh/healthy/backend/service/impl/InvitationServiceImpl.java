package com.chh.healthy.backend.service.impl;

import cn.hutool.core.convert.Convert;
import com.boss.xtrain.core.common.api.CommonPage;
import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.boss.xtrain.core.common.api.CommonResponseUtils;
import com.boss.xtrain.core.common.exception.ServiceException;
import com.boss.xtrain.core.common.service.BaseCURDService;
import com.boss.xtrain.core.context.BaseContextHolder;
import com.boss.xtrain.util.BeanUtil;
import com.chh.healthy.backend.dao.impl.InvitationDAO;
import com.chh.healthy.backend.dao.impl.UserDAO;
import com.chh.healthy.backend.dao.mapper.InvitationMapper;
import com.chh.healthy.backend.dao.mapper.UserMapper;
import com.chh.healthy.backend.pojo.code.ErrorCode;
import com.chh.healthy.backend.pojo.dto.InvitationDTO;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.entity.Invitation;
import com.chh.healthy.backend.pojo.entity.User;
import com.chh.healthy.backend.pojo.query.InvitationQuery;
import com.chh.healthy.backend.pojo.query.UserQuery;
import com.chh.healthy.backend.pojo.vo.InvitationVO;
import com.chh.healthy.backend.service.InvitationService;
import com.chh.healthy.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class InvitationServiceImpl extends BaseCURDService<InvitationDTO, Invitation, InvitationQuery, InvitationMapper> implements InvitationService {

    @Autowired
    InvitationDAO dao;

    public InvitationServiceImpl(@Autowired InvitationDAO dao) {
        this.myDao = dao;
    }


    @Override
    public List<?> query(CommonRequest<InvitationQuery> commonRequest) {
        return null;
    }

    @Override
    protected Invitation doObjectTransf(InvitationDTO invitationDTO) {
        return BeanUtil.copy(invitationDTO,Invitation.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResponse<InvitationDTO> doPublish(InvitationDTO request) {
        try {
            if (request != null) {
                Invitation invitation = dao.saveAndReturn(BeanUtil.copy(request,Invitation.class));
                return CommonResponseUtils.success(BeanUtil.copy(invitation,InvitationDTO.class));
            } else {
                return CommonResponseUtils.failed("发布失败");
            }
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.PUBLISH_EXCEPTION,e);
        }
    }

    @Override
    public CommonResponse<CommonPage<InvitationVO>> doQueryInvitation(InvitationQuery request) {
        try {
            List<Invitation> response = dao.queryInvitation(request);
            BaseContextHolder.endPage(response);
            List<InvitationVO> voList = BeanUtil.copyList(response,InvitationVO.class);
            CommonPage<InvitationVO> page = CommonPage.restPage(voList);
            page.setTotal(Convert.toLong(BaseContextHolder.get("total")));
            return CommonResponseUtils.success(page);
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.QUERY_INVITATION_EXCEPTION,e);
        }

    }
}
