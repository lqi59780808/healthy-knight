package com.xuexiang.chh_healthy_android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.adapter.base.broccoli.BroccoliSimpleDelegateAdapter;
import com.xuexiang.chh_healthy_android.adapter.picture.ImageSelectGridAdapter;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonRequest;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.InvitationDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.InvitationPictureDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.ReplyDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.query.InvitationQuery;
import com.xuexiang.chh_healthy_android.fragment.news.NewsFragment;
import com.xuexiang.chh_healthy_android.fragment.profile.ProfileFragment;
import com.xuexiang.chh_healthy_android.utils.Utils;
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
    @BindView(R.id.invitation_avatar)
    RadiusImageView invitationAvatar;
    @BindView(R.id.invitation_nickname)
    TextView invitationNickname;
    @BindView(R.id.invitation_title)
    TextView invitationTitle;
    @BindView(R.id.invitation_content)
    TextView invitationContent;
    @BindView(R.id.tv_praise)
    TextView praise;
    @BindView(R.id.tv_comment)
    TextView comment;

    private Long invitationId;
    private List<LocalMedia> lmList = new ArrayList<>();
    private List<ReplyDTO> replyList = new ArrayList<>();
    private List<InvitationPictureDTO> ipList = new ArrayList<>();
    private InvitationDTO invitation = new InvitationDTO();

    private BroccoliSimpleDelegateAdapter<LocalMedia> lmAdapter;
    private BroccoliSimpleDelegateAdapter<ReplyDTO> replyAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invitation_view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
//        return null;
    }

    @Override
    protected void initViews() {
        llReply.setVisibility(View.GONE);
        llEdit.setVisibility(View.GONE);
        etReply.setOnFocusChangeListener(this);
        clickReply.setOnClickListener(this);
        CommonRequest<Long> commonRequest = new CommonRequest<>();
        commonRequest.setBody(invitationId);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/invitation/query/id")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<InvitationDTO>, InvitationDTO>(new TipCallBack<InvitationDTO>() {
                    @Override
                    public void onSuccess(InvitationDTO response) throws Throwable {
                        invitation = response;
                        System.out.println(invitation);
                        ipList = invitation.getPictureList();
                        if (ipList != null) {
                            for (InvitationPictureDTO ipd:
                                    ipList) {
                                LocalMedia localMedia = new LocalMedia();
                                localMedia.setPath(FinalEnum.frontUrl + ipd.getUrl());
                                lmList.add(localMedia);
                            }
                        }
                        Utils.useGlide(InvitationViewFragment.this,FinalEnum.frontUrl + invitation.getUser().getIcon(),invitationAvatar);
                        invitationNickname.setText(invitation.getUser().getNickname());
                        invitationTitle.setText(invitation.getTitle());
                        invitationContent.setText(invitation.getContent());
                        praise.setText(String.valueOf(invitation.getGood()));
                        comment.setText(String.valueOf(invitation.getComment()));
                        refreshLayout.finishRefresh();
                    }
                    @Override
                    public void onError(ApiException e) {
                        refreshLayout.finishRefresh();
                        super.onError(e);
                    }
                }){});
        //init adapter
        VirtualLayoutManager virtualLayoutManager1 = new VirtualLayoutManager(getContext());
        recyclerViewImages.setLayoutManager(virtualLayoutManager1);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerViewImages.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);
        lmAdapter = new BroccoliSimpleDelegateAdapter<LocalMedia>(R.layout.adapter_show_image_grid_item,new GridLayoutHelper(3),lmList) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, LocalMedia model, int position) {
                holder.image(R.id.iv_show_pic,model.getPath());
                holder.click(R.id.iv_show_pic,view -> PictureSelector.create(InvitationViewFragment.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, lmList));
            }

            @Override
            protected void onBindBroccoli(RecyclerViewHolder holder, Broccoli broccoli) {
                holder.findView(R.id.iv_show_pic);
            }
        };
        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager1);
        delegateAdapter.addAdapter(lmAdapter);
        recyclerViewImages.setAdapter(lmAdapter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getActivity().getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (isShouldHideInput(v, ev)) {//点击editText控件外部
                if (imm != null) {
                    assert v != null;
                    KeyboardUtils.hideSoftInput(v);
                }
                if (etReply != null) {
                    etReply.clearFocus();
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        return getActivity().getWindow().superDispatchTouchEvent(ev) || getActivity().onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        MaterialEditText editText;
        if (v != null && (v instanceof MaterialEditText)) {
            editText = (MaterialEditText) v;
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (view.getId() == R.id.et_reply) {
            if (b) {
//                if (etReply != null) {
//                    etReply.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//                }
                llGood.setVisibility(View.GONE);
                llCollect.setVisibility(View.GONE);
                llComment.setVisibility(View.GONE);
                llReply.setVisibility(View.VISIBLE);
                llEdit.setVisibility(View.VISIBLE);
            } else {
//                if (etReply != null) {
//                    etReply.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
//                }
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
        super.onDestroyView();
    }

    @Override
    @SingleClick
    public void onClick(View view) {
        if (view.getId() == R.id.click_reply) {
            etReply.setFocusable(true);
            etReply.setFocusableInTouchMode(true);
            etReply.requestFocus();
            etReply.requestFocusFromTouch();
            KeyboardUtils.showSoftInput(etReply);
        }
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.getLayout().postDelayed(() -> {
                lmList = new ArrayList<>();
                CommonRequest<Long> commonRequest = new CommonRequest<>();
                commonRequest.setBody(invitationId);
                String body = JsonUtil.toJson(commonRequest);
                XHttp.post(FinalEnum.frontUrl + "/healthy/invitation/query/id")
                        .upJson(body)
                        .syncRequest(false)
                        .onMainThread(true)
                        .execute(new CallBackProxy<CommonResponse<InvitationDTO>, InvitationDTO>(new TipCallBack<InvitationDTO>() {
                            @Override
                            public void onSuccess(InvitationDTO response) throws Throwable {
                                invitation = response;
                                ipList = invitation.getPictureList();
                                if (ipList != null) {
                                    for (InvitationPictureDTO ipd:
                                            ipList) {
                                        LocalMedia localMedia = new LocalMedia();
                                        localMedia.setPath(FinalEnum.frontUrl + ipd.getUrl());
                                        lmList.add(localMedia);
                                    }
                                }
                                Utils.useGlide(InvitationViewFragment.this,FinalEnum.frontUrl + invitation.getUser().getIcon(),invitationAvatar);
                                invitationNickname.setText(invitation.getUser().getNickname());
                                invitationTitle.setText(invitation.getTitle());
                                invitationContent.setText(invitation.getContent());
                                praise.setText(String.valueOf(invitation.getGood()));
                                comment.setText(String.valueOf(invitation.getComment()));
                                lmAdapter.refresh(lmList);
                                refreshLayout.finishRefresh();
                            }
                            @Override
                            public void onError(ApiException e) {
                                refreshLayout.finishRefresh();
                                super.onError(e);
                            }
                        }){});
            }, 1);
        });
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
//            refreshLayout.getLayout().postDelayed(() -> {
//                InvitationQuery query = new InvitationQuery();
//                query.setPageNum(1);
//                query.setPageSize(10);
//                CommonRequest<InvitationQuery> commonRequest = new CommonRequest<>();
//                commonRequest.setBody(query);
//                String body = JsonUtil.toJson(commonRequest);
//                XHttp.post(FinalEnum.frontUrl + "/healthy/invitation/query")
//                        .upJson(body)
//                        .syncRequest(false)
//                        .onMainThread(true)
//                        .execute(new CallBackProxy<CommonResponse<List<InvitationDTO>>, List<InvitationDTO>>(new TipCallBack<List<InvitationDTO>>() {
//                            @Override
//                            public void onSuccess(List<InvitationDTO> response) throws Throwable {
//                                refreshLayout.finishLoadMore();
//                            }
//
//                            @Override
//                            public void onError(ApiException e) {
//                                refreshLayout.finishLoadMoreWithNoMoreData();
//                                super.onError(e);
//                            }
//                        }){});
//            }, 1);
            refreshLayout.finishLoadMore();
        });
        refreshLayout.autoRefresh();
    }
}

