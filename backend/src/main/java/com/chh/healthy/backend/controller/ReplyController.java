package com.chh.healthy.backend.controller;

import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.boss.xtrain.core.common.controller.BaseCRUDController;
import com.boss.xtrain.core.context.BaseContextHolder;
import com.chh.healthy.backend.api.ReplyApi;
import com.chh.healthy.backend.dao.mapper.ReplyMapper;
import com.chh.healthy.backend.pojo.dto.ReplyDTO;
import com.chh.healthy.backend.pojo.entity.Reply;
import com.chh.healthy.backend.pojo.query.ReplyQuery;
import com.chh.healthy.backend.pojo.vo.ReplyVO;
import com.chh.healthy.backend.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReplyController extends BaseCRUDController<ReplyDTO, Reply, ReplyQuery, ReplyMapper, ReplyVO> implements ReplyApi{

    @Autowired
    ReplyService service;

    @Override
    public CommonResponse<ReplyDTO> replyMaster(@RequestBody CommonRequest<ReplyDTO> request) {
        BaseContextHolder.setUserId(request.getBody().getUser().getId());
        return service.doReplyMaster(request.getBody());
    }

    @Override
    public CommonResponse<List<ReplyDTO>> queryReply(@RequestBody CommonRequest<ReplyQuery> request) {
        this.doBeforePagination(request.getBody().getPageNum(),request.getBody().getPageSize());
        CommonResponse<List<ReplyDTO>> response = service.doQueryReply(request.getBody());
        return response;
    }

    @Override
    protected List<ReplyVO> doObjectTransf(List<?> list) {
        return null;
    }
}
