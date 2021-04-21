package com.xuexiang.chh_healthy_android.fragment;

import android.content.Context;
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
//    private SoftKeyBoardListener softKeyBoardListener;
//    private Rect mChangeImageBackgroundRect = null;

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
//        return null;
    }

    @Override
    protected void initViews() {
        llReply.setVisibility(View.GONE);
        llEdit.setVisibility(View.GONE);
        etReply.setOnFocusChangeListener(this);
//        etReply.setOnKeyBoardHideListener(new SoftKeyEditText.OnKeyBoardHideListener() {
//            @Override
//            public void onKeyHide(int keyCode, KeyEvent event) {
//                etReply.clearFocus();
//            }
//        });
        clickReply.setOnClickListener(this);
        //初始化数据
        initRequest(true);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = llBottom;
//            //当isShouldHideInput(v, ev)为true时，表示的是点击输入框区域，则需要显示键盘，同时显示光标，反之，需要隐藏键盘、光标
//            if (isShouldHideInput(v, ev)) {
//
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                    etReply.clearFocus();
//                }
//            }
//            return super.dispatchTouchEvent(ev);
//        }
//        // 必不可少，否则所有的组件都不会有TouchEvent了
//        if (getActivity().getWindow().superDispatchTouchEvent(ev)) {
//            return true;
//        }
//        return super.dispatchTouchEvent(ev);
//    }


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = llBottom;
//            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (isShouldHideInput(v, ev)) {//点击editText控件外部
//                if (imm != null) {
//                    assert v != null;
//                    KeyboardUtils.hideSoftInput(v);
//                }
//                if (etReply != null) {
//                    etReply.clearFocus();
//                }
//            }
//            return super.dispatchTouchEvent(ev);
//        }
//        return getActivity().getWindow().superDispatchTouchEvent(ev) || getActivity().onTouchEvent(ev);
//    }

//    public  boolean isShouldHideInput(View v, MotionEvent event) {
//        if (v != null && (v instanceof LinearLayout)) {
//            int[] leftTop = { 0, 0 };
//            //获取输入框当前的location位置
//            v.getLocationInWindow(leftTop);
//            int left = leftTop[0];
//            int top = leftTop[1];
//            int bottom = top + v.getHeight();
//            int right = left + v.getWidth();
//            float x = event.getX();
//            float y = event.getY();
//            float rx = event.getRawX();
//            float ry = event.getRawY();
//            if (event.getX() > left && event.getX() < right
//                    && event.getY() > top && event.getY() < bottom) {
//                // 点击的是输入框区域，保留点击EditText的事件
//                return false;
//            } else {
//                return true;
//            }
//        }
//        return false;
//    }

//    public boolean isShouldHideInput(View v, MotionEvent event) {
//        if (v != null && (v instanceof LinearLayout)) {
//            int[] leftTop = {0, 0};
//            //获取输入框当前的location位置
//            v.getLocationInWindow(leftTop);
//            int left = leftTop[0];
//            int top = leftTop[1];
//            int bottom = top + v.getHeight();
//            int right = left + v.getWidth();
//            return !(event.getX() > left && event.getX() < right
//                    && event.getY() > top && event.getY() < bottom);
//        }
//        return false;
//    }

//    private boolean isInChangeImageZone(View view, int x, int y) {
//        if (null == mChangeImageBackgroundRect) {
//            mChangeImageBackgroundRect = new Rect();
//        }
//        view.getDrawingRect(mChangeImageBackgroundRect);
//        int[] location = new int[2];
//        view.getLocationOnScreen(location);
//        mChangeImageBackgroundRect.left = location[0];
//        mChangeImageBackgroundRect.top = location[1];
//        mChangeImageBackgroundRect.right = mChangeImageBackgroundRect.right + location[0];
//        mChangeImageBackgroundRect.bottom = mChangeImageBackgroundRect.bottom + location[1];
//        return mChangeImageBackgroundRect.contains(x, y);
//    }

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
            } else if (!b && !KeyboardUtils.isSoftInputShow(getActivity())){
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
        recyclerViewImages.setOnTouchListener(null);
//        etReply.setOnKeyBoardHideListener(null);
//        getActivity().getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(null);
//        softKeyBoardListener.listener = new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//
//            }
//        };
        super.onDestroyView();
    }


    @Override
    @SingleClick
    public void onClick(View view) {
        if (view.getId() == R.id.click_reply) {
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
        }
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
//        setSoftKeyBoardListener();
//        llBottom.setOnTouchListener(this);
//        llHead.setOnTouchListener(this);
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

//    /**
//     * 添加软键盘的监听
//     */
//    private void setSoftKeyBoardListener(){
//        softKeyBoardListener = new SoftKeyBoardListener(getActivity());
//        softKeyBoardListener.setOnSoftKeyBoardChangeListener(new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
//            @Override
//            public void keyBoardShow(int height) {
//            }
//
//            @Override
//            public void keyBoardHide(int height) {
//                etReply.clearFocus();
//            }
//        });
//    }

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

//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = llBottom;
//            if (view.getId() == R.id.ll_bottom) {
//                return true;
//            } else if (view.getId() == R.id.ll_head) {
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                    etReply.clearFocus();
//                }
//                return true;
//            }
//        }
//        return true;
//    }

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
                        master = response.getUser();
                        replyId = master.getId();
                        invitation = response;
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

