package com.xuexiang.chh_healthy_android.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.activity.InvitationViewActivity;
import com.xuexiang.chh_healthy_android.adapter.base.broccoli.BroccoliSimpleDelegateAdapter;
import com.xuexiang.chh_healthy_android.adapter.base.delegate.SingleDelegateAdapter;
import com.xuexiang.chh_healthy_android.adapter.picture.ImageSelectGridAdapter;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.SoftKeyBoardListener;
import com.xuexiang.chh_healthy_android.core.http.callback.TipCallBack;
import com.xuexiang.chh_healthy_android.core.http.callback.TipProgressLoadingCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonRequest;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.CollectDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.GoodDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.InvitationDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.InvitationPictureDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.ReplyDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.UserDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.query.InvitationQuery;
import com.xuexiang.chh_healthy_android.core.http.pojo.query.ReplyQuery;
import com.xuexiang.chh_healthy_android.fragment.news.NewsFragment;
import com.xuexiang.chh_healthy_android.fragment.profile.ProfileFragment;
import com.xuexiang.chh_healthy_android.utils.TokenUtils;
import com.xuexiang.chh_healthy_android.utils.Utils;
import com.xuexiang.chh_healthy_android.utils.XToastUtils;
import com.xuexiang.chh_healthy_android.widget.SoftKeyEditText;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.net.JsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.samlss.broccoli.Broccoli;

@Page(anim = CoreAnim.none)
public class InvitationViewFragment extends BaseFragment implements View.OnFocusChangeListener, View.OnClickListener {

    @BindView(R.id.et_reply)
    MaterialEditText etReply;
    @BindView(R.id.ll_good)
    LinearLayout llGood;
    @BindView(R.id.ll_collect)
    LinearLayout llCollect;
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.ll_reply)
    LinearLayout llReply;
    @BindView(R.id.ll_comment)
    LinearLayout llComment;
    @BindView(R.id.click_reply)
    LinearLayout clickReply;
    @BindView(R.id.ll_edit)
    LinearLayout llEdit;
    @BindView(R.id.invitation_view)
    FrameLayout invitationView;
    @BindView(R.id.recycler_view_images)
    RecyclerView recyclerViewImages;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.btn_reply)
    SuperButton btnReply;
    @BindView(R.id.invitation_image_collect)
    RadiusImageView imageCollcet;
    @BindView(R.id.invitation_tv_collect)
    TextView tvCollect;
    @BindView(R.id.invitation_image_good)
    RadiusImageView imageGood;
    @BindView(R.id.invitation_tv_good)
    TextView tvGood;

    private int pageNum;
    private int pageMax;
    private boolean isReplyMaster;

    private Long invitationId;
    private Long replyId;
    private Long replyUserId;
    private UserDTO master;
    private List<LocalMedia> lmList = new ArrayList<>();
    private List<ReplyDTO> replyList = new ArrayList<>();
    private List<InvitationPictureDTO> ipList = new ArrayList<>();
    private InvitationDTO invitation = new InvitationDTO();
    private DelegateAdapter delegateAdapter;

    private BroccoliSimpleDelegateAdapter<LocalMedia> lmAdapter;
    private BroccoliSimpleDelegateAdapter<ReplyDTO> replyAdapter;
    private SingleDelegateAdapter headAdapter;
    private SingleDelegateAdapter headBAdapter;
    private SingleDelegateAdapter noCommentAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invitation_view_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        pageNum = 1;
        pageMax = 10;
        isReplyMaster = true;
        Bundle bundle = getArguments();
        invitationId = bundle.getLong("invitationId");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorTitleBar));
        titleBar.setTitle("帖子");
        titleBar.setLeftImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        titleBar.setActionTextColor(getResources().getColor(R.color.white));
        return titleBar;
    }

    @Override
    protected void initViews() {
        llReply.setVisibility(View.GONE);
        llEdit.setVisibility(View.GONE);
        //初始化数据
        initRequest(true);
    }

    @Override
    public void initListeners() {
        recyclerViewImages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (etReply.isFocused()) {
                    etReply.clearFocus();
                } else {
                    llGood.setVisibility(View.VISIBLE);
                    llCollect.setVisibility(View.VISIBLE);
                    llComment.setVisibility(View.VISIBLE);
                    llEdit.setVisibility(View.GONE);
                    llReply.setVisibility(View.GONE);
                }
                return false;
            }
        });
        btnReply.setOnClickListener(this);
        etReply.setOnFocusChangeListener(this);
        llComment.setOnClickListener(this);
        llGood.setOnClickListener(this);
        llCollect.setOnClickListener(this);
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.getLayout().postDelayed(() -> {
                pageNum = 1;
                pageMax = 10;
                initRequest(false);
            }, 1);
        });
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            refreshLayout.getLayout().postDelayed(() -> {
                pageNum ++;
                pageMax = 10;
                loadMoreReply();
            }, 1);
        });
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (view.getId() == R.id.et_reply) {
            if (b) {
                llGood.setVisibility(View.GONE);
                llCollect.setVisibility(View.GONE);
                llComment.setVisibility(View.GONE);
                llReply.setVisibility(View.VISIBLE);
                llEdit.setVisibility(View.VISIBLE);
            } else if (!b && !KeyboardUtils.isSoftInputShow(getActivity())){
                llGood.setVisibility(View.VISIBLE);
                llCollect.setVisibility(View.VISIBLE);
                llComment.setVisibility(View.VISIBLE);
                llEdit.setVisibility(View.GONE);
                llReply.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        etReply.setOnFocusChangeListener(null);
        recyclerViewImages.setOnTouchListener(null);
        super.onDestroyView();
    }


    @Override
    @SingleClick
    public void onClick(View view) {
        if (view.getId() == R.id.ll_comment) {
            etReply.setHint("回复楼主");
            isReplyMaster = true;
            etReply.setFocusable(true);
            etReply.setFocusableInTouchMode(true);
            etReply.requestFocus();
            etReply.requestFocusFromTouch();
            KeyboardUtils.showSoftInput(etReply);
        } else if (view.getId() == R.id.btn_reply) {
            //回复消息
            ReplyDTO replyDTO = new ReplyDTO();
            replyDTO.setContent(etReply.getEditValue());
            replyDTO.setInvitationId(invitationId);
            replyDTO.setUser(TokenUtils.getUserInfo());
            //如果是回复楼主
            if (!isReplyMaster) {
                replyDTO.setReplyId(replyId);
                replyDTO.setReplyUserId(replyUserId);
            }
            if (etReply.validate()) {
                CommonRequest<ReplyDTO> commonRequest = new CommonRequest<>();
                commonRequest.setBody(replyDTO);
                String body = JsonUtil.toJson(commonRequest);
                XHttp.post(FinalEnum.frontUrl + "/healthy/reply/master")
                        .upJson(body)
                        .syncRequest(false)
                        .onMainThread(true)
                        .execute(new CallBackProxy<CommonResponse<ReplyDTO>, ReplyDTO>(new TipProgressLoadingCallBack<ReplyDTO>(this) {
                            @Override
                            public void onSuccess(ReplyDTO response) throws Throwable {
                                XToastUtils.success("回复成功");
                                etReply.setText("");
                                refreshLayout.autoRefresh();
                            }
                        }){});
            }
        } else if (view.getId() == R.id.ll_good) {
            if (invitation.getGoodInfo() == null) {
                doGood();
            } else {
                undoGood();
            }

        } else if (view.getId() == R.id.ll_collect) {
            if (invitation.getCollectInfo() == null) {
                doCollect();
            } else {
                undoCollect();
            }

        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == 101) {
                refreshLayout.autoRefresh();
            }
        }
        super.onFragmentResult(requestCode, resultCode, data);
    }

    private void doGood() {
        GoodDTO goodDTO = new GoodDTO();
        goodDTO.setInvitationId(invitationId);
        goodDTO.setCreatedBy(TokenUtils.getUserInfo().getId());
        CommonRequest<GoodDTO> commonRequest = new CommonRequest<>();
        commonRequest.setBody(goodDTO);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/invitation/good/save")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<GoodDTO>, GoodDTO>(new TipCallBack<GoodDTO>() {
                    @Override
                    public void onSuccess(GoodDTO response) throws Throwable {
                        invitation.setGoodInfo(response);
                        invitation.setGood(invitation.getGood() + 1);
                        headBAdapter.notifyDataSetChanged();
                        resetGoodAndCollect();
                    }
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }
                }){});
    }

    private void doCollect() {
        CollectDTO dto = new CollectDTO();
        dto.setInvitationId(invitationId);
        dto.setCreatedBy(TokenUtils.getUserInfo().getId());
        CommonRequest<CollectDTO> commonRequest = new CommonRequest<>();
        commonRequest.setBody(dto);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/invitation/collect/save")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<CollectDTO>, CollectDTO>(new TipCallBack<CollectDTO>() {
                    @Override
                    public void onSuccess(CollectDTO response) throws Throwable {
                        invitation.setCollectInfo(response);
                        resetGoodAndCollect();
                    }
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }
                }){});
    }

    private void undoCollect() {
        CommonRequest<CollectDTO> commonRequest = new CommonRequest<>();
        commonRequest.setBody(invitation.getCollectInfo());
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/invitation/collect/delete")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<Integer>, Integer>(new TipCallBack<Integer>() {
                    @Override
                    public void onSuccess(Integer response) throws Throwable {
                        if (response == 1) {
                            invitation.setCollectInfo(null);
                            resetGoodAndCollect();
                        }
                    }
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }
                }){});
    }

    private void undoGood() {
        CommonRequest<GoodDTO> commonRequest = new CommonRequest<>();
        commonRequest.setBody(invitation.getGoodInfo());
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/invitation/good/delete")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<Integer>, Integer>(new TipCallBack<Integer>() {
                    @Override
                    public void onSuccess(Integer response) throws Throwable {
                        if (response == 1) {
                            invitation.setGoodInfo(null);
                            invitation.setGood(invitation.getGood() - 1);
                            headBAdapter.notifyDataSetChanged();
                            resetGoodAndCollect();
                        }
                    }
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }
                }){});
    }

    private void resetGoodAndCollect() {
        if (invitation.getGoodInfo() != null) {
            tvGood.setTextColor(getResources().getColor(R.color.color_pink));
            imageGood.setImageDrawable(getResources().getDrawable(R.drawable.ic_good_pink));
        } else {
            tvGood.setTextColor(getResources().getColor(R.color.xui_config_color_gray_4));
            imageGood.setImageDrawable(getResources().getDrawable(R.drawable.ic_good));
        }
        if (invitation.getCollectInfo() != null) {
            tvCollect.setTextColor(getResources().getColor(R.color.color_pink));
            imageCollcet.setImageDrawable(getResources().getDrawable(R.drawable.ic_collect_pink));
        } else {
            tvCollect.setTextColor(getResources().getColor(R.color.xui_config_color_gray_4));
            imageCollcet.setImageDrawable(getResources().getDrawable(R.drawable.ic_collect));
        }

    }

    public void initRecycleView() {
        ipList = invitation.getPictureList();
        if (ipList != null) {
            for (InvitationPictureDTO ipd:
                    ipList) {
                LocalMedia localMedia = new LocalMedia();
                localMedia.setPath(FinalEnum.frontUrl + ipd.getUrl());
                lmList.add(localMedia);
            }
        }

        //初始化头部信息
        headAdapter = new SingleDelegateAdapter(R.layout.fragment_invitation_view_head) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                holder.image(R.id.invitation_avatar,R.drawable.ic_knight);
                if (invitation.getUser().getIcon() != null) {
                    holder.image(R.id.invitation_avatar,FinalEnum.frontUrl + invitation.getUser().getIcon());
                }
                holder.text(R.id.invitation_nickname,invitation.getUser().getNickname());
                holder.text(R.id.invitation_title,invitation.getTitle());
                holder.text(R.id.invitation_content,invitation.getContent());
            }
        };

        //初始化图片信息
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(3);
        gridLayoutHelper.setPadding(10, 16, 10, 10);
        gridLayoutHelper.setVGap(10);
        gridLayoutHelper.setHGap(10);
        gridLayoutHelper.setAutoExpand(true);
        gridLayoutHelper.setWeights(new float[]{33,33,33});
        lmAdapter = new BroccoliSimpleDelegateAdapter<LocalMedia>(R.layout.adapter_show_image_grid_item,gridLayoutHelper,lmList) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, LocalMedia model, int position) {
                holder.image(R.id.iv_show_pic,model.getPath());
                holder.click(R.id.iv_show_pic,view -> PictureSelector.create(InvitationViewFragment.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, lmList));
            }

            @Override
            protected void onBindBroccoli(RecyclerViewHolder holder, Broccoli broccoli) {
                broccoli.addPlaceholders(
                        holder.findView(R.id.iv_show_pic)
                );
            }
        };

        //初始化点赞评论数量信息
        headBAdapter = new SingleDelegateAdapter(R.layout.fragment_invitation_view_headb) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                holder.text(R.id.headb_comment,String.valueOf(invitation.getComment()));
                holder.text(R.id.headb_praise,String.valueOf(invitation.getGood()));
                holder.text(R.id.headb_date,String.valueOf(invitation.getCreatedTime()));
            }
        };

        noCommentAdapter = new SingleDelegateAdapter(R.layout.fragment_invitation_view_no_comment) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                LinearLayout ll = holder.findViewById(R.id.ll_no_comment);
                ll.setVisibility(View.GONE);
                if (invitation.getReplyList()!= null && invitation.getReplyList().size() == 0) {
                    ll.setVisibility(View.VISIBLE);
                } else if (invitation.getReplyList()!= null && invitation.getReplyList().size() != 0) {
                    ll.setVisibility(View.GONE);
                }
            }
        };

        replyAdapter = new BroccoliSimpleDelegateAdapter<ReplyDTO>(R.layout.fragment_invitation_view_reply, new LinearLayoutHelper()) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, ReplyDTO model, int position) {
                LinearLayout llReply = holder.findViewById(R.id.ll_reply_reply);
                LinearLayout llReply1 = holder.findViewById(R.id.ll_reply1);
                LinearLayout llReply2 = holder.findViewById(R.id.ll_reply2);
                LinearLayout llShowMore = holder.findViewById(R.id.ll_show_more);
                llReply.setClickable(false);
                llReply.setVisibility(View.GONE);
                holder.image(R.id.is_master,R.drawable.ic_master);
                RadiusImageView radiusImageView = holder.findViewById(R.id.is_master);
                radiusImageView.setVisibility(View.GONE);
                holder.image(R.id.invitation_avatar,getResources().getDrawable(R.drawable.ic_knight));
                if (model.getUser().getIcon() != null) {
                    holder.image(R.id.invitation_avatar,FinalEnum.frontUrl + model.getUser().getIcon());
                }
                if (model.getUser().getId().equals(master.getId())) {
                    radiusImageView.setVisibility(View.VISIBLE);
                }
                holder.text(R.id.invitation_nickname,model.getUser().getNickname());
                holder.text(R.id.invitation_content,model.getContent());
                holder.text(R.id.iv_date,model.getCreatedTime());
                holder.click(R.id.invitation_view, v -> {
                    etReply.setHint("回复" + model.getUser().getNickname());
                    replyId = model.getId();
                    replyUserId = model.getUser().getId();
                    isReplyMaster = false;
                    focusEdit(etReply);
                });
                List<ReplyDTO> userReplyList = model.getUserReplyList();
                if (userReplyList == null || userReplyList.size() == 0) {
                    llReply.setVisibility(View.GONE);
                } else {
                    if (userReplyList.size() >= 1) {
                        llReply.setVisibility(View.VISIBLE);
                        llReply1.setVisibility(View.VISIBLE);
                        llReply2.setVisibility(View.GONE);
                        llShowMore.setVisibility(View.GONE);
                        Bundle bundle = new Bundle();
                        bundle.putString("replyDTO",JsonUtil.toJson(model));
                        bundle.putString("master",JsonUtil.toJson(master));
                        llReply.setClickable(true);
                        llReply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PageOption.to(ReplyFragment.class)
                                        .setBundle(bundle)
                                        .setRequestCode(100)
                                        .setAddToBackStack(true)
                                        .setAnim(CoreAnim.slide)
                                        .open(InvitationViewFragment.this);
                            }
                        });
                        holder.text(R.id.user_reply1,toReplyString(userReplyList.get(0)));
                        if (userReplyList.size() >= 2) {
                            llReply2.setVisibility(View.VISIBLE);
                            llShowMore.setVisibility(View.GONE);
                            holder.text(R.id.user_reply2,toReplyString(userReplyList.get(1)));
                            if (userReplyList.size() >=3) {
                                llShowMore.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }

            @Override
            protected void onBindBroccoli(RecyclerViewHolder holder, Broccoli broccoli) {
                broccoli.addPlaceholders(
                        holder.findView(R.id.invitation_avatar),
                        holder.findView(R.id.invitation_nickname),
                        holder.findView(R.id.invitation_content)
                );
            }
        };


        //init adapter
        VirtualLayoutManager virtualLayoutManager1 = new VirtualLayoutManager(getContext());
        recyclerViewImages.setLayoutManager(virtualLayoutManager1);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerViewImages.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager1);
        delegateAdapter.addAdapter(headAdapter);
        delegateAdapter.addAdapter(lmAdapter);
        delegateAdapter.addAdapter(headBAdapter);
        delegateAdapter.addAdapter(noCommentAdapter);
        delegateAdapter.addAdapter(replyAdapter);
        recyclerViewImages.setAdapter(delegateAdapter);
        headAdapter.notifyDataSetChanged();
        headBAdapter.notifyDataSetChanged();
        lmAdapter.refresh(lmList);
        replyAdapter.refresh(invitation.getReplyList());
    }

    public String toReplyString (ReplyDTO reply) {
        UserDTO user = reply.getUser();
        UserDTO replyUser = reply.getReplyUser();
        if (replyUser == null) {
            return user.getNickname() + " : " + reply.getContent();
        } else {
            return user.getNickname() + " 回复 " + replyUser.getNickname() + " : " + reply.getContent();
        }
    }

    public void focusEdit(EditText editText) {
        editText.requestFocus();
        editText.requestFocusFromTouch();
        KeyboardUtils.showSoftInput(editText);
    }

    public void resetAdapter() {
        headAdapter.notifyDataSetChanged();
        headBAdapter.notifyDataSetChanged();
        noCommentAdapter.notifyDataSetChanged();
        lmAdapter.refresh(lmList);
        replyAdapter.refresh(invitation.getReplyList());
    }

    public void initRequest(boolean flag) {
        InvitationQuery query = new InvitationQuery();
        query.setRequestUserId(TokenUtils.getUserInfo().getId());
        query.setRequestInvitationId(invitationId);
        CommonRequest<InvitationQuery> commonRequest = new CommonRequest<>();
        commonRequest.setBody(query);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/invitation/query/id")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<InvitationDTO>, InvitationDTO>(new TipCallBack<InvitationDTO>() {
                    @Override
                    public void onSuccess(InvitationDTO response) throws Throwable {
                        master = response.getUser();
                        replyId = master.getId();
                        invitation = response;
                        if (invitation.getGoodInfo() != null) {
                            tvGood.setTextColor(getResources().getColor(R.color.color_pink));
                            imageGood.setImageDrawable(getResources().getDrawable(R.drawable.ic_good_pink));
                        }
                        if (invitation.getCollectInfo() != null) {
                            tvCollect.setTextColor(getResources().getColor(R.color.color_pink));
                            imageCollcet.setImageDrawable(getResources().getDrawable(R.drawable.ic_collect_pink));
                        }
                        //查询回复
                        ReplyQuery query = new ReplyQuery();
                        query.setPageNum(pageNum);
                        query.setPageSize(pageMax);
                        query.setInvitationId(invitationId);
                        CommonRequest<ReplyQuery> commonRequest = new CommonRequest<>();
                        commonRequest.setBody(query);
                        String body = JsonUtil.toJson(commonRequest);
                        XHttp.post(FinalEnum.frontUrl + "/healthy/reply/reply")
                                .upJson(body)
                                .syncRequest(false)
                                .onMainThread(true)
                                .execute(new CallBackProxy<CommonResponse<List<ReplyDTO>>, List<ReplyDTO>>(new TipCallBack<List<ReplyDTO>>() {
                                    @Override
                                    public void onSuccess(List<ReplyDTO> response) throws Throwable {
                                        invitation.setReplyList(response);
                                        if (flag) {
                                            initRecycleView();
                                        } else {
                                            resetAdapter();
                                        }
                                        refreshLayout.finishRefresh();
                                    }
                                    @Override
                                    public void onError(ApiException e) {
                                        refreshLayout.finishRefresh();
                                        super.onError(e);
                                    }
                                }){});
                    }
                    @Override
                    public void onError(ApiException e) {
                        refreshLayout.finishRefresh();
                        super.onError(e);
                    }
                }){});
    }

    public void loadMoreReply() {
        ReplyQuery query = new ReplyQuery();
        query.setPageNum(pageNum);
        query.setPageSize(pageMax);
        query.setInvitationId(invitationId);
        CommonRequest<ReplyQuery> commonRequest = new CommonRequest<>();
        commonRequest.setBody(query);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/reply/reply")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<List<ReplyDTO>>, List<ReplyDTO>>(new TipCallBack<List<ReplyDTO>>() {
                    @Override
                    public void onSuccess(List<ReplyDTO> response) throws Throwable {
                        replyAdapter.loadMore(response);
                        refreshLayout.finishLoadMore();
                    }
                    @Override
                    public void onError(ApiException e) {
                        refreshLayout.finishLoadMore();
                        super.onError(e);
                    }
                }){});
    }
}

