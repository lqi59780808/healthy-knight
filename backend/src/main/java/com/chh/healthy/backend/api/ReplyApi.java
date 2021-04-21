package com.chh.healthy.backend.api;

import com.boss.xtrain.core.common.api.CommonCRUDApi;
import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.chh.healthy.backend.pojo.dto.ReplyDTO;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.query.ReplyQuery;
import com.chh.healthy.backend.pojo.query.UserQuery;
import com.chh.healthy.backend.pojo.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/healthy/reply")
public interface ReplyApi {
    @PostMapping("/master")
    @ApiOperation(value = "回复楼主")
    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<java.lang.Integer>
     * @desc: 回复楼主
     * @see
     * @since
     */
    CommonResponse<ReplyDTO> replyMaster(CommonRequest<ReplyDTO> request);

    @PostMapping("/reply")
    @ApiOperation(value = "查询回复")
    /**
     * @param: [request]
     * @return: com.boss.xtrain.core.common.api.CommonResponse<java.lang.Integer>
     * @desc: 查询回复
     * @see
     * @since
     */
    CommonResponse<List<ReplyDTO>> queryReply(CommonRequest<ReplyQuery> request);
}
