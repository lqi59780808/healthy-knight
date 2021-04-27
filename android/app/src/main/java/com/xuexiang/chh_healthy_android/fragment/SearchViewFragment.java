/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.chh_healthy_android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.chh_healthy_android.activity.InvitationViewActivity;
import com.xuexiang.chh_healthy_android.adapter.base.broccoli.BroccoliSimpleDelegateAdapter;
import com.xuexiang.chh_healthy_android.adapter.base.delegate.SimpleDelegateAdapter;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonRequest;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.InvitationDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.InvitationPictureDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.query.InvitationQuery;
import com.xuexiang.chh_healthy_android.fragment.my.MyInvitationFragment;
import com.xuexiang.chh_healthy_android.utils.TokenUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.searchview.MaterialSearchView;
import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.net.JsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.samlss.broccoli.Broccoli;

/**
 * @author xuexiang
 * @since 2019/1/2 下午4:49
 */
@Page(anim = CoreAnim.none)
public class SearchViewFragment extends BaseFragment {
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    int pageMax;
    int pageNow;

    private SimpleDelegateAdapter<InvitationDTO> mNewsAdapter;

    String q;

    List<InvitationDTO> invitationList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_searchview;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorTitleBar));
        titleBar.setTitle("搜索");
        titleBar.setLeftImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        titleBar.setActionTextColor(getResources().getColor(R.color.white));
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_menu_search) {
            @Override
            @SingleClick
            public void performAction(View view) {
                mSearchView.showSearch();
            }
        });
        return titleBar;
    }

    @Override
    protected void initViews() {
        mSearchView.setVoiceSearch(false);
        mSearchView.setEllipsize(true);
        mSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                q = query;
                initRequest(q);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
        mSearchView.setSubmitOnClick(true);
        mSearchView.showSearch(false);
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
                //由于imageView的复用问题，这边要进行重置
                RadiusImageView img1 = holder.findViewById(R.id.invitation_image1);
                RadiusImageView img2 = holder.findViewById(R.id.invitation_image2);
                RadiusImageView img3 = holder.findViewById(R.id.invitation_image3);
                img1.setClickable(false);
                img2.setClickable(false);
                img3.setClickable(false);
                model.setMediaList(new ArrayList<>());
                if (model.getUser().getIcon() != null) {
                    holder.image(R.id.invitation_avatar, FinalEnum.frontUrl + model.getUser().getIcon());
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
                        img1.setClickable(true);
                        holder.image(R.id.invitation_image1,FinalEnum.frontUrl + model.getPictureList().get(0).getUrl());
                        holder.click(R.id.invitation_image1, v -> PictureSelector.create(SearchViewFragment.this).themeStyle(R.style.picture_default_style).openExternalPreview(0, model.getMediaList()));
                        if (iList.size() >= 2) {
                            img2.setClickable(true);
                            holder.image(R.id.invitation_image2,FinalEnum.frontUrl + model.getPictureList().get(1).getUrl());
                            holder.click(R.id.invitation_image2, v -> PictureSelector.create(SearchViewFragment.this).themeStyle(R.style.picture_default_style).openExternalPreview(1, model.getMediaList()));
                            if (iList.size() >= 3) {
                                img3.setClickable(true);
                                holder.image(R.id.invitation_image3,FinalEnum.frontUrl + model.getPictureList().get(2).getUrl());
                                holder.click(R.id.invitation_image3, v -> PictureSelector.create(SearchViewFragment.this).themeStyle(R.style.picture_default_style).openExternalPreview(2, model.getMediaList()));
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
            refreshLayout.finishRefresh();
        });
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            refreshLayout.getLayout().postDelayed(() -> {
                loadMore(q);
            }, 1);
        });
    }

    private void initRequest(String q) {
        InvitationQuery query = new InvitationQuery();
        this.pageNow = 1;
        this.pageMax = 10;
        query.setPageNum(this.pageNow);
        query.setPageSize(this.pageMax);
        if (!"".equals(q)) {
            query.setTitle(q);
        }
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
    }

    private void loadMore(String q) {
        InvitationQuery query = new InvitationQuery();
        this.pageNow ++;
        this.pageMax = 10;
        query.setPageNum(this.pageNow);
        query.setPageSize(this.pageMax);
        if (!"".equals(q)) {
            query.setTitle(q);
        }
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
                        refreshLayout.finishLoadMore();
                        super.onError(e);
                    }
                }){});
    }

    @Override
    public void onDestroyView() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        }
        super.onDestroyView();
    }
}
