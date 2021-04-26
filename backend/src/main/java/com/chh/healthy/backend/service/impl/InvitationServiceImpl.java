package com.chh.healthy.backend.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.boss.xtrain.core.annotation.stuffer.IdGenerator;
import com.boss.xtrain.core.common.api.CommonPage;
import com.boss.xtrain.core.common.api.CommonRequest;
import com.boss.xtrain.core.common.api.CommonResponse;
import com.boss.xtrain.core.common.api.CommonResponseUtils;
import com.boss.xtrain.core.common.exception.ServiceException;
import com.boss.xtrain.core.common.service.BaseCURDService;
import com.boss.xtrain.core.context.BaseContextHolder;
import com.boss.xtrain.util.BeanUtil;
import com.chh.healthy.backend.dao.impl.*;
import com.chh.healthy.backend.dao.mapper.InvitationMapper;
import com.chh.healthy.backend.dao.mapper.UserMapper;
import com.chh.healthy.backend.pojo.code.ErrorCode;
import com.chh.healthy.backend.pojo.dto.*;
import com.chh.healthy.backend.pojo.entity.*;
import com.chh.healthy.backend.pojo.query.InvitationQuery;
import com.chh.healthy.backend.pojo.query.UserQuery;
import com.chh.healthy.backend.pojo.vo.InvitationVO;
import com.chh.healthy.backend.service.InvitationService;
import com.chh.healthy.backend.service.UserService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvitationServiceImpl extends BaseCURDService<InvitationDTO, Invitation, InvitationQuery, InvitationMapper> implements InvitationService {

    @Autowired
    InvitationDAO dao;
    @Autowired
    InvitationPictureDAO picDao;
    @Autowired
    CollectDAO collectDao;
    @Autowired
    GoodDAO goodDAO;
    @Autowired
    ReplyDAO replyDAO;
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
            invitation.setComment(0);
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
    public CommonResponse<CommonPage<InvitationDTO>> doQueryInvitation(InvitationQuery request) {
        try {
            List<Invitation> response = dao.queryInvitation(request);
            BaseContextHolder.endPage(response);
            List<InvitationDTO> dtoList = new ArrayList<>();
            for (Invitation invitation : response) {
                Good query = new Good();
                query.setInvitationId(invitation.getId());
                Reply reply = new Reply();
                reply.setInvitationId(invitation.getId());
                invitation.setGood(goodDAO.count(query));
                invitation.setComment(replyDAO.count(reply));
                String json = JSON.toJSONString(invitation);
                dtoList.add(JSON.parseObject(json, InvitationDTO.class));
            }
            CommonPage<InvitationDTO> page = CommonPage.restPage(dtoList);
            page.setTotal(Convert.toLong(BaseContextHolder.get("total")));
            return CommonResponseUtils.success(page);
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.QUERY_INVITATION_EXCEPTION,e);
        }
    }

    @Override
    public CommonResponse<InvitationDTO> doQueryInvitationById(InvitationQuery request) {
        try {
            List<Invitation> invitationList = dao.queryInvitationById(request.getRequestInvitationId());
            if (invitationList.size() != 0) {
                Invitation invitation = invitationList.get(0);
                Collect collect = collectDao.queryCollectForInvitaiton(request.getRequestUserId(),request.getRequestInvitationId());
                Good good = goodDAO.queryCollectForInvitaiton(request.getRequestUserId(),request.getRequestInvitationId());
                invitation.setCollectInfo(collect);
                invitation.setGoodInfo(good);
                Good query = new Good();
                query.setInvitationId(invitation.getId());
                Reply reply = new Reply();
                reply.setInvitationId(invitation.getId());
                invitation.setGood(goodDAO.count(query));
                invitation.setComment(replyDAO.count(reply));
                InvitationDTO invitationDTO = JSON.parseObject(JSON.toJSONString(invitation),InvitationDTO.class);
                return CommonResponseUtils.success(invitationDTO);
            }
            return CommonResponseUtils.success(new InvitationDTO());
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.QUERY_EXCEPTION,e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResponse<GoodDTO> doSaveGood(GoodDTO request) {
        try {
            Good res = goodDAO.saveAndReturn(BeanUtil.copy(request,Good.class));
            return CommonResponseUtils.success(BeanUtil.copy(res,GoodDTO.class));
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.SAVE_EXCEPTION,e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResponse<CollectDTO> doSaveCollect(CollectDTO request) {
        try {
            Collect res = collectDao.saveAndReturn(BeanUtil.copy(request,Collect.class));
            return CommonResponseUtils.success(BeanUtil.copy(res,CollectDTO.class));
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.FAIL,e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResponse<Integer> doDeleteGood(GoodDTO request) {
        try {
            Integer res = goodDAO.delete(request.getId());
            return CommonResponseUtils.success(res);
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.FAIL,e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResponse<Integer> doDeleteCollect(CollectDTO request) {
        try {
            Integer res = collectDao.delete(request.getId());
            return CommonResponseUtils.success(res);
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.FAIL,e);
        }
    }
}
