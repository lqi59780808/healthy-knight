package com.chh.healthy.backend.service.impl;

import cn.hutool.core.convert.Convert;
import com.boss.xtrain.core.annotation.stuffer.IdGenerator;
import com.boss.xtrain.core.common.api.CommonPage;
import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.boss.xtrain.core.common.api.CommonResponseUtils;
import com.boss.xtrain.core.common.exception.ServiceException;
import com.boss.xtrain.core.common.service.BaseCURDService;
import com.boss.xtrain.core.context.BaseContextHolder;
import com.boss.xtrain.util.BeanUtil;
import com.chh.healthy.backend.dao.impl.InvitationDAO;
import com.chh.healthy.backend.dao.impl.InvitationPictureDAO;
import com.chh.healthy.backend.dao.impl.UserDAO;
import com.chh.healthy.backend.dao.mapper.InvitationMapper;
import com.chh.healthy.backend.dao.mapper.UserMapper;
import com.chh.healthy.backend.pojo.code.ErrorCode;
import com.chh.healthy.backend.pojo.dto.InvitationDTO;
import com.chh.healthy.backend.pojo.dto.InvitationPictureDTO;
import com.chh.healthy.backend.pojo.dto.UserDTO;
import com.chh.healthy.backend.pojo.entity.Invitation;
import com.chh.healthy.backend.pojo.entity.InvitationPicture;
import com.chh.healthy.backend.pojo.entity.User;
import com.chh.healthy.backend.pojo.query.InvitationQuery;
import com.chh.healthy.backend.pojo.query.UserQuery;
import com.chh.healthy.backend.pojo.vo.InvitationVO;
import com.chh.healthy.backend.service.InvitationService;
import com.chh.healthy.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
public class InvitationServiceImpl extends BaseCURDService<InvitationDTO, Invitation, InvitationQuery, InvitationMapper> implements InvitationService {

    @Autowired
    InvitationDAO dao;
    @Autowired
    InvitationPictureDAO picDao;
    @Autowired
    IdGenerator idGenerator;

    public InvitationServiceImpl(@Autowired InvitationDAO dao) {
        this.myDao = dao;
    }


    @Override
    public List<?> query(CommonRequest<InvitationQuery> commonRequest) {
        return null;
    }

    @Override
    protected Invitation doObjectTransf(InvitationDTO invitationDTO) {
        return BeanUtil.copy(invitationDTO,Invitation.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResponse<InvitationDTO> doPublish(String title, String content, MultipartFile[] picture) {
        try {
            Invitation invitation = new Invitation();
            invitation.setClick(0);
            invitation.setCollect(0);
            invitation.setGood(0);
            invitation.setTitle(title);
            invitation.setContent(content);
            Invitation res = dao.saveAndReturn(invitation);
            if (picture != null && picture.length != 0) {
                for (MultipartFile multipartFile: picture) {
                    //获取原文件名
                    String name=multipartFile.getOriginalFilename();
                    //获取文件后缀
                    String subffix=name.substring(name.lastIndexOf("."),name.length());
                    String fileName = String.valueOf(idGenerator.snowflakeId());
                    String path = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/picture/";
                    File file = new File(path);
                    //文件夹不存在就创建
                    if(!file.exists())
                    {
                        file.mkdirs();
                    }
                    File save = new File(file+"\\"+ fileName +subffix);
                    multipartFile.transferTo(save);
                    InvitationPicture invitationPicture = new InvitationPicture();
                    invitationPicture.setInvitationId(res.getId());
                    invitationPicture.setUrl("/" + "picture" + "/" + save.getName());
                    picDao.saveAndReturn(invitationPicture);
                }
            }
            return CommonResponseUtils.success(BeanUtil.copy(res,InvitationDTO.class));
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.PUBLISH_EXCEPTION,e);
        }
    }

    @Override
    public CommonResponse<CommonPage<InvitationVO>> doQueryInvitation(InvitationQuery request) {
        try {
            List<Invitation> response = dao.queryInvitation(request);
            BaseContextHolder.endPage(response);
            List<InvitationVO> voList = BeanUtil.copyList(response,InvitationVO.class);
            CommonPage<InvitationVO> page = CommonPage.restPage(voList);
            page.setTotal(Convert.toLong(BaseContextHolder.get("total")));
            return CommonResponseUtils.success(page);
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.QUERY_INVITATION_EXCEPTION,e);
        }
    }
}
