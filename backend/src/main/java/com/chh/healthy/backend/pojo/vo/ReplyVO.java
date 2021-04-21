package com.chh.healthy.backend.pojo.vo;

import com.boss.xtrain.core.common.pojo.vo.BaseVO;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.dto.UserReplyDTO;
import lombok.Data;

import javax.persistence.Table;
import java.util.List;

@Table(name = "t_user")
@Data
public class ReplyVO extends BaseVO {
    private Long invitationId;

    private String content;

    private List<UserReplyDTO> userReplyList;

    private UserDTO user;
}