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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    CommonResponse<InvitationDTO> publish(String title, String content, MultipartFile[] picture,long userId);

    @PostMapping("/publish2")
    @ApiOperation(value = "用户发帖2")
    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<com.chh.healthy.backend.pojo.dto.InvitationDTO>
     * @desc: 用户发帖2
     * @see
     * @since
     */
    CommonResponse<InvitationDTO> publish2(String title, String content,long userId);

    @PostMapping("/query")
    @ApiOperation(value = "查询")
    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<com.boss.xtrain.core.common.api.CommonPage<com.chh.healthy.backend.pojo.vo.InvitationVO>>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<List<InvitationDTO>> queryInvitation(CommonRequest<InvitationQuery> request);

    @PostMapping("/query/id")
    @ApiOperation(value = "查询")
    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<com.boss.xtrain.core.common.api.CommonPage<com.chh.healthy.backend.pojo.vo.InvitationVO>>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<InvitationDTO> queryInvitationById(CommonRequest<Long> request);

}
