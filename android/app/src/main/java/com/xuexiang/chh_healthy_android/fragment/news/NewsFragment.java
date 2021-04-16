/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.chh_healthy_android.fragment.news;

import android.graphics.Color;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.activity.InvitationViewActivity;
import com.xuexiang.chh_healthy_android.adapter.base.broccoli.BroccoliSimpleDelegateAdapter;
import com.xuexiang.chh_healthy_android.adapter.base.delegate.SimpleDelegateAdapter;
import com.xuexiang.chh_healthy_android.adapter.base.delegate.SingleDelegateAdapter;
import com.xuexiang.chh_healthy_android.adapter.entity.NewInfo;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipCallBack;
import com.xuexiang.chh_healthy_android.core.http.callback.TipProgressLoadingCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonPage;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonRequest;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.InvitationDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.InvitationPictureDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.UserDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.query.InvitationQuery;
import com.xuexiang.chh_healthy_android.utils.DemoDataProvider;
import com.xuexiang.chh_healthy_android.utils.SettingSPUtils;
import com.xuexiang.chh_healthy_android.utils.Utils;
import com.xuexiang.chh_healthy_android.utils.XToastUtils;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xui.widget.imageview.ImageLoader;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.imageview.preview.PreviewBuilder;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.net.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import butterknife.BindView;
import me.samlss.broccoli.Broccoli;

/**
 * 首页动态
 *
 * @author xuexiang
 * @since 2019-10-30 00:15
 */
@Page(anim = CoreAnim.none)
public class NewsFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    List<InvitationDTO> invitationList = new ArrayList<>();

    int pageMax;
    int pageNow;

    private SimpleDelegateAdapter<InvitationDTO> mNewsAdapter;

    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(getContext());
        recyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);
        pageMax = 10;
        pageNow = 1;
        mNewsAdapter = new BroccoliSimpleDelegateAdapter<InvitationDTO>(R.layout.adapter_news_invitation_view_list_item, new LinearLayoutHelper()) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, InvitationDTO model, int position) {
                holder.image(R.id.invitation_avatar,getResources().getDrawable(R.drawable.ic_knight));
                holder.click(R.id.invitation_image1, null);
                holder.click(R.id.invitation_image2, null);
                holder.click(R.id.invitation_image3, null);
                model.setMediaList(new ArrayList<>());
                if (model.getUser().getIcon() != null) {
                    holder.image(R.id.invitation_avatar,FinalEnum.frontUrl + model.getUser().getIcon());
                }
                holder.image(R.id.invitation_image1,getResources().getDrawable(R.drawable.ic_ty));
                holder.image(R.id.invitation_image2,getResources().getDrawable(R.drawable.ic_ty));
                holder.image(R.id.invitation_image3,getResources().getDrawable(R.drawable.ic_ty));
                if (model.getPictureList() != null) {
                    List<InvitationPictureDTO> iList = model.getPictureList();
                    for (int i = 0; i < 3 && i < iList.size(); i++) {
                        LocalMedia localMedia = new LocalMedia();
                        localMedia.setPath(FinalEnum.frontUrl + iList.get(i).getUrl());
                        model.getMediaList().add(localMedia);
                    }
                    LinearLayout linearLayout = holder.findViewById(R.id.image_linear_layout);
                    linearLayout.setVisibility(View.VISIBLE);
                    if (iList.size() == 0) {
                        linearLayout.setVisibility(View.GONE);
                    } else if (iList.size() >= 1) {
                        holder.image(R.id.invitation_image1,FinalEnum.frontUrl + model.getPictureList().get(0).getUrl());
                        holder.click(R.id.invitation_image1, v -> PictureSelector.create(NewsFragment.this).themeStyle(R.style.picture_default_style).openExternalPreview(0, model.getMediaList()));
                        if (iList.size() >= 2) {
                            holder.image(R.id.invitation_image2,FinalEnum.frontUrl + model.getPictureList().get(1).getUrl());
                            holder.click(R.id.invitation_image2, v -> PictureSelector.create(NewsFragment.this).themeStyle(R.style.picture_default_style).openExternalPreview(1, model.getMediaList()));
                            if (iList.size() >= 3) {
                                holder.image(R.id.invitation_image3,FinalEnum.frontUrl + model.getPictureList().get(2).getUrl());
                                holder.click(R.id.invitation_image3, v -> PictureSelector.create(NewsFragment.this).themeStyle(R.style.picture_default_style).openExternalPreview(2, model.getMediaList()));
                            }
                        }
                    }
                }
                holder.text(R.id.invitation_nickname,model.getUser().getNickname());
                holder.text(R.id.invitation_title,model.getTitle());
                holder.text(R.id.invitation_content,model.getContent());
                holder.text(R.id.tv_praise,String.valueOf(model.getGood()));
                holder.text(R.id.tv_comment,String.valueOf(model.getComment()));
                holder.click(R.id.invitation_view, v -> ActivityUtils.startActivity(InvitationViewActivity.class,"invitationId",model.getId()));
            }

            @Override
            protected void onBindBroccoli(RecyclerViewHolder holder, Broccoli broccoli) {
                broccoli.addPlaceholders(
                        holder.findView(R.id.invitation_image1),
                        holder.findView(R.id.invitation_image2),
                        holder.findView(R.id.invitation_image3),
                        holder.findView(R.id.invitation_avatar),
                        holder.findView(R.id.invitation_nickname),
                        holder.findView(R.id.invitation_title),
                        holder.findView(R.id.invitation_content),
                        holder.findView(R.id.tv_praise),
                        holder.findView(R.id.tv_comment)
                );
            }
        };

        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        delegateAdapter.addAdapter(mNewsAdapter);

        recyclerView.setAdapter(delegateAdapter);
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.getLayout().postDelayed(() -> {
                InvitationQuery query = new InvitationQuery();
                this.pageNow = 1;
                this.pageMax = 10;
                query.setPageNum(this.pageNow);
                query.setPageSize(this.pageMax);
                CommonRequest<InvitationQuery> commonRequest = new CommonRequest<>();
                commonRequest.setBody(query);
                String body = JsonUtil.toJson(commonRequest);
                XHttp.post(FinalEnum.frontUrl + "/healthy/invitation/query")
                        .upJson(body)
                        .syncRequest(false)
                        .onMainThread(true)
                        .execute(new CallBackProxy<CommonResponse<List<InvitationDTO>>, List<InvitationDTO>>(new TipCallBack<List<InvitationDTO>>() {
                            @Override
                            public void onSuccess(List<InvitationDTO> response) throws Throwable {
                                invitationList = response;
                                mNewsAdapter.refresh(invitationList);
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
            refreshLayout.getLayout().postDelayed(() -> {
                InvitationQuery query = new InvitationQuery();
                this.pageNow++;
                this.pageMax = 10;
                query.setPageNum(this.pageNow);
                query.setPageSize(this.pageMax);
                CommonRequest<InvitationQuery> commonRequest = new CommonRequest<>();
                commonRequest.setBody(query);
                String body = JsonUtil.toJson(commonRequest);
                XHttp.post(FinalEnum.frontUrl + "/healthy/invitation/query")
                        .upJson(body)
                        .syncRequest(false)
                        .onMainThread(true)
                        .execute(new CallBackProxy<CommonResponse<List<InvitationDTO>>, List<InvitationDTO>>(new TipCallBack<List<InvitationDTO>>() {
                            @Override
                            public void onSuccess(List<InvitationDTO> response) throws Throwable {
                                invitationList = response;
                                mNewsAdapter.loadMore(invitationList);
                                refreshLayout.finishLoadMore();
                            }

                            @Override
                            public void onError(ApiException e) {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                                super.onError(e);
                            }
                        }){});
            }, 1);
        });
        refreshLayout.autoRefresh();
    }
}
