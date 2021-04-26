package com.chh.healthy.backend.service;

import com.boss.xtrain.core.common.api.CommonPage;
import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.chh.healthy.backend.pojo.dto.CollectDTO;
import com.chh.healthy.backend.pojo.dto.GoodDTO;
import com.chh.healthy.backend.pojo.dto.InvitationDTO;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.entity.Collect;
import com.chh.healthy.backend.pojo.entity.Invitation;
import com.chh.healthy.backend.pojo.query.InvitationQuery;
import com.chh.healthy.backend.pojo.vo.InvitationVO;
import org.springframework.web.multipart.MultipartFile;

public interface InvitationService {
    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<com.chh.healthy.backend.pojo.dto.InvitationDTO>
     * @desc: 发帖
     * @see
     * @since
     */
    CommonResponse<InvitationDTO> doPublish(String title, String content, MultipartFile[] picture);

    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<com.boss.xtrain.core.common.api.CommonPage<com.chh.healthy.backend.pojo.vo.InvitationVO>>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<CommonPage<InvitationDTO>> doQueryInvitation(InvitationQuery request);

    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<com.boss.xtrain.core.common.api.CommonPage<com.chh.healthy.backend.pojo.vo.InvitationVO>>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<InvitationDTO> doQueryInvitationById(InvitationQuery request);

    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<com.boss.xtrain.core.common.api.CommonPage<com.chh.healthy.backend.pojo.vo.InvitationVO>>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<GoodDTO> doSaveGood(GoodDTO request);

    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<com.boss.xtrain.core.common.api.CommonPage<com.chh.healthy.backend.pojo.vo.InvitationVO>>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<CollectDTO> doSaveCollect(CollectDTO request);

    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<com.boss.xtrain.core.common.api.CommonPage<com.chh.healthy.backend.pojo.vo.InvitationVO>>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<Integer> doDeleteGood(GoodDTO request);

    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<com.boss.xtrain.core.common.api.CommonPage<com.chh.healthy.backend.pojo.vo.InvitationVO>>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<Integer> doDeleteCollect(CollectDTO request);
}
