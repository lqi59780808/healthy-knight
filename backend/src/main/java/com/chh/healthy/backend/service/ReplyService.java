package com.chh.healthy.backend.service;

import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.chh.healthy.backend.pojo.dto.ReplyDTO;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.query.ReplyQuery;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReplyService {
    /**
     * @param: [userDTO]
     * @return: java.lang.Integer
     * @desc: 登录
     * @see
     * @since
     */
    CommonResponse<ReplyDTO> doReplyMaster(ReplyDTO request);

    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<java.util.List<com.chh.healthy.backend.pojo.dto.ReplyDTO>>
     * @desc:
     * @see
     * @since
     */
    CommonResponse<List<ReplyDTO>> doQueryReply(ReplyQuery request);
}
