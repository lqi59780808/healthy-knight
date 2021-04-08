package com.chh.healthy.backend.controller;

import com.boss.xtrain.core.common.api.CommonPage;
import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.boss.xtrain.core.common.controller.BaseCRUDController;
import com.boss.xtrain.util.BeanUtil;
import com.chh.healthy.backend.api.InvitationApi;
import com.chh.healthy.backend.dao.mapper.InvitationMapper;
import com.chh.healthy.backend.pojo.dto.InvitationDTO;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.entity.Invitation;
import com.chh.healthy.backend.pojo.query.InvitationQuery;
import com.chh.healthy.backend.pojo.vo.InvitationVO;
import com.chh.healthy.backend.service.InvitationService;
import com.chh.healthy.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InvitationController extends BaseCRUDController<InvitationDTO, Invitation, InvitationQuery, InvitationMapper, InvitationVO> implements InvitationApi {
    @Autowired
    InvitationService invitationService;

    @Override
    protected List<InvitationVO> doObjectTransf(List<?> list) {
        return BeanUtil.copyList(list,InvitationVO.class);
    }

    @Override
    public CommonResponse<InvitationDTO> publish(CommonRequest<InvitationDTO> request) {
        return invitationService.doPublish(request.getBody());
    }

    @Override
    public CommonResponse<CommonPage<InvitationVO>> queryInvitation(CommonRequest<InvitationQuery> request) {
        InvitationQuery query = request.getBody();
        this.doBeforePagination(query.getPageNum(),query.getPageSize());
        List<InvitationQuery> response;
        return null;
    }
}
