package com.xuexiang.chh_healthy_android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.adapter.base.broccoli.BroccoliSimpleDelegateAdapter;
import com.xuexiang.chh_healthy_android.adapter.base.delegate.SingleDelegateAdapter;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipCallBack;
import com.xuexiang.chh_healthy_android.core.http.callback.TipProgressLoadingCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonRequest;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.InvitationDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.InvitationPictureDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.ReplyDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.UserDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.query.ReplyQuery;
import com.xuexiang.chh_healthy_android.utils.TokenUtils;
import com.xuexiang.chh_healthy_android.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xutil.net.JsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.samlss.broccoli.Broccoli;

@Page(anim = CoreAnim.none)
public class ReplyFragment extends BaseFragment implements  View.OnClickListener {

    @BindView(R.id.et_reply)
    MaterialEditText etReply;
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.ll_reply)
    LinearLayout llReply;
    @BindView(R.id.ll_edit)
    LinearLayout llEdit;
    @BindView(R.id.recycler_view_images)
    RecyclerView recyclerViewImages;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.btn_reply)
    SuperButton btnReply;

    private int pageNum;
    private int pageMax;
    private boolean isReplyMaster;

    private Long invitationId;
    private Long replyId;
    private Long replyUserId;
    private UserDTO master;
    private ReplyDTO reply = new ReplyDTO();

    private BroccoliSimpleDelegateAdapter<ReplyDTO> replyAdapter;
    private SingleDelegateAdapter headAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reply_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        pageNum = 1;
        pageMax = 10;
        isReplyMaster = false;
        Bundle bundle = getArguments();
        reply = JsonUtil.fromJson(bundle.getString("replyDTO"),ReplyDTO.class);
        master = JsonUtil.fromJson(bundle.getString("master"),UserDTO.class);
        invitationId = reply.getInvitationId();
        replyId = reply.getId();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorTitleBar));
        titleBar.setTitle("全部回复");
        titleBar.setLeftImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        titleBar.setActionTextColor(getResources().getColor(R.color.white));
        return titleBar;
//        return null;
    }

    @Override
    protected void initViews() {
        //初始化数据
        initRequest(true);
    }

    @Override
    @SingleClick
    public void onClick(View view) {
        if (view.getId() == R.id.btn_reply) {
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
                                setFragmentResult(101,new Intent());
                                etReply.setText("");
                                refreshLayout.autoRefresh();
                            }
                        }){});
            }
        }
    }

    @Override
    public void initListeners() {
//        recyclerViewImages.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (etReply.isFocused()) {
//                    etReply.clearFocus();
//                } else {
//                    llGood.setVisibility(View.VISIBLE);
//                    llCollect.setVisibility(View.VISIBLE);
//                    llComment.setVisibility(View.VISIBLE);
//                    llEdit.setVisibility(View.GONE);
//                    llReply.setVisibility(View.GONE);
//                }
//                return false;
//            }
//        });
        btnReply.setOnClickListener(this);
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
    public void onDestroyView() {
        recyclerViewImages.setOnTouchListener(null);
        super.onDestroyView();
    }

    public void initRecycleView() {
        //初始化头部信息
        headAdapter = new SingleDelegateAdapter(R.layout.fragment_reply_body) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                holder.image(R.id.invitation_avatar,R.drawable.ic_knight);
                RadiusImageView isMaster = holder.findViewById(R.id.is_master);
                isMaster.setVisibility(View.GONE);
                if (reply.getUser().getIcon() != null) {
                    holder.image(R.id.invitation_avatar,FinalEnum.frontUrl + reply.getUser().getIcon());
                }
                if (reply.getUser().getId().equals(master.getId())) {
                    isMaster.setVisibility(View.VISIBLE);
                }
                holder.text(R.id.invitation_nickname,reply.getUser().getNickname());
                holder.text(R.id.invitation_content,reply.getContent());
                holder.text(R.id.iv_date,reply.getCreatedTime());
                holder.click(R.id.invitation_view,view -> {
                   replyId = reply.getId();
                   replyUserId = reply.getUser().getId();
                   etReply.setHint("回复楼主");
                   focusEdit(etReply);
                });
            }
        };

        SingleDelegateAdapter lineAdapter = new SingleDelegateAdapter(R.layout.fragment_line) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

            }
        };

        replyAdapter = new BroccoliSimpleDelegateAdapter<ReplyDTO>(R.layout.fragment_reply_body, new LinearLayoutHelper()) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, ReplyDTO model, int position) {
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
                holder.text(R.id.invitation_content,toReplyString(model));
                holder.text(R.id.iv_date,model.getCreatedTime());
                holder.click(R.id.invitation_view, v -> {
                    etReply.setHint("回复" + model.getUser().getNickname());
                    replyId = reply.getId();
                    replyUserId = model.getUser().getId();
                    isReplyMaster = false;
                    focusEdit(etReply);
                });
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
        //    private InvitationDTO invitation = new InvitationDTO();
        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager1);
        delegateAdapter.addAdapter(headAdapter);
        delegateAdapter.addAdapter(lineAdapter);
        delegateAdapter.addAdapter(replyAdapter);
        recyclerViewImages.setAdapter(delegateAdapter);
        headAdapter.notifyDataSetChanged();
        replyAdapter.refresh(reply.getUserReplyList());
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
        replyAdapter.refresh(reply.getUserReplyList());
    }

    public void initRequest(boolean flag) {
        ReplyQuery query = new ReplyQuery();
        query.setPageNum(pageNum);
        query.setPageSize(pageMax);
        query.setInvitationId(invitationId);
        query.setReplyId(reply.getId());
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
                        reply.setUserReplyList(response);
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

    public void loadMoreReply() {
        ReplyQuery query = new ReplyQuery();
        query.setPageNum(pageNum);
        query.setPageSize(pageMax);
        query.setInvitationId(invitationId);
        query.setReplyId(reply.getId());
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

