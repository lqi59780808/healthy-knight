package com.chh.healthy.backend.controller;

import com.boss.xtrain.core.common.api.CommonPage;
import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.boss.xtrain.core.common.api.CommonResponseUtils;
import com.boss.xtrain.core.common.controller.BaseCRUDController;
import com.boss.xtrain.core.context.BaseContextHolder;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public CommonResponse<InvitationDTO> publish(@RequestParam String title,@RequestParam String content, @RequestParam MultipartFile[] picture,@RequestParam long id) {
        BaseContextHolder.setUserId(id);
        return invitationService.doPublish(title,content,picture);
    }

    @Override
    public CommonResponse<InvitationDTO> publish2(@RequestParam String title, @RequestParam String content,@RequestParam long id) {
        BaseContextHolder.setUserId(id);
        return invitationService.doPublish(title,content,null);
    }

    @Override
    public CommonResponse<List<InvitationDTO>> queryInvitation(@RequestBody CommonRequest<InvitationQuery> request) {
        InvitationQuery query = request.getBody();
        this.doBeforePagination(query.getPageNum(),query.getPageSize());
        CommonResponse<CommonPage<InvitationDTO>> response = invitationService.doQueryInvitation(query);
        return CommonResponseUtils.success(response.getBody().getData());
    }
}
