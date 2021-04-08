package com.chh.healthy.backend.api;

import com.boss.xtrain.core.common.api.CommonCRUDApi;
import com.boss.xtrain.core.common.api.CommonPage;
import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.chh.healthy.backend.pojo.dto.InvitationDTO;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.query.InvitationQuery;
import com.chh.healthy.backend.pojo.query.UserQuery;
import com.chh.healthy.backend.pojo.vo.InvitationVO;
import com.chh.healthy.backend.pojo.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/healthy/invitation")
public interface InvitationApi extends CommonCRUDApi<InvitationDTO, InvitationQuery, InvitationVO> {
    @PostMapping("/publish")
    @ApiOperation(value = "用户发帖")
    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<com.chh.healthy.backend.pojo.dto.InvitationDTO>
     * @desc: 用户发帖
     * @see
     * @since
     */
    CommonResponse<InvitationDTO> publish(CommonRequest<InvitationDTO> request);

    @PostMapping("/query")
    @ApiOperation(value = "查询")
    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<com.boss.xtrain.core.common.api.CommonPage<com.chh.healthy.backend.pojo.vo.InvitationVO>>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<CommonPage<InvitationVO>> queryInvitation(CommonRequest<InvitationQuery> request);
}
