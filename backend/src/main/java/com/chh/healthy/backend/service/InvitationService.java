package com.chh.healthy.backend.service;

import com.boss.xtrain.core.common.api.CommonPage;
import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.chh.healthy.backend.pojo.dto.InvitationDTO;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.entity.Invitation;
import com.chh.healthy.backend.pojo.query.InvitationQuery;
import com.chh.healthy.backend.pojo.vo.InvitationVO;

public interface InvitationService {
    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<com.chh.healthy.backend.pojo.dto.InvitationDTO>
     * @desc: 发帖
     * @see
     * @since
     */
    CommonResponse<InvitationDTO> doPublish(InvitationDTO request);

    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<com.boss.xtrain.core.common.api.CommonPage<com.chh.healthy.backend.pojo.vo.InvitationVO>>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<CommonPage<InvitationVO>> doQueryInvitation(InvitationQuery request);
}
