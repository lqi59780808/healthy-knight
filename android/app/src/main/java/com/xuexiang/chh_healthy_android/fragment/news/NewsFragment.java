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
import com.xuexiang.chh_healthy_android.utils.Utils;
import com.xuexiang.chh_healthy_android.utils.XToastUtils;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xui.widget.imageview.ImageLoader;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
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
//        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
//        recyclerView.setRecycledViewPool(viewPool);
//        viewPool.setMaxRecycledViews(0, 10);
        pageMax = 10;
        pageNow = 1;
//        //轮播条
//        SingleDelegateAdapter bannerAdapter = new SingleDelegateAdapter(R.layout.include_head_view_banner) {
//            @Override
//            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
//                SimpleImageBanner banner = holder.findViewById(R.id.sib_simple_usage);
//                banner.setSource(DemoDataProvider.getBannerList())
//                        .setOnItemClickListener((view, item, position1) -> XToastUtils.toast("headBanner position--->" + position1)).startScroll();
//            }
//        };
//
//        //九宫格菜单
//        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
//        gridLayoutHelper.setPadding(0, 16, 0, 0);
//        gridLayoutHelper.setVGap(10);
//        gridLayoutHelper.setHGap(0);
//        SimpleDelegateAdapter<AdapterItem> commonAdapter = new SimpleDelegateAdapter<AdapterItem>(R.layout.adapter_common_grid_item, gridLayoutHelper, DemoDataProvider.getGridItems(getContext())) {
//            @Override
//            protected void bindData(@NonNull RecyclerViewHolder holder, int position, AdapterItem item) {
//                if (item != null) {
//                    RadiusImageView imageView = holder.findViewById(R.id.riv_item);
//                    imageView.setCircle(true);
//                    ImageLoader.get().loadImage(imageView, item.getIcon());
//                    holder.text(R.id.tv_title, item.getTitle().toString().substring(0, 1));
//                    holder.text(R.id.tv_sub_title, item.getTitle());
//
//                    holder.click(R.id.ll_container, v -> XToastUtils.toast("点击了：" + item.getTitle()));
//                }
//            }
//        };
//
//        //资讯的标题
//        SingleDelegateAdapter titleAdapter = new SingleDelegateAdapter(R.layout.adapter_title_item) {
//            @Override
//            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
//                holder.text(R.id.tv_title, "资讯");
//                holder.text(R.id.tv_action, "更多");
//                holder.click(R.id.tv_action, v -> XToastUtils.toast("更多"));
//            }
//        };

//        //资讯
//        mNewsAdapter = new BroccoliSimpleDelegateAdapter<NewInfo>(R.layout.adapter_news_card_view_list_item, new LinearLayoutHelper(), DemoDataProvider.getEmptyNewInfo()) {
//            @Override
//            protected void onBindData(RecyclerViewHolder holder, NewInfo model, int position) {
//                if (model != null) {
//                    holder.text(R.id.tv_user_name, model.getUserName());
//                    holder.text(R.id.tv_tag, model.getTag());
//                    holder.text(R.id.tv_title, model.getTitle());
//                    holder.text(R.id.tv_summary, model.getSummary());
//                    holder.text(R.id.tv_praise, model.getPraise() == 0 ? "点赞" : String.valueOf(model.getPraise()));
//                    holder.text(R.id.tv_comment, model.getComment() == 0 ? "评论" : String.valueOf(model.getComment()));
//                    holder.text(R.id.tv_read, "阅读量 " + model.getRead());
//                    holder.image(R.id.iv_image, model.getImageUrl());
//
//                    holder.click(R.id.card_view, v -> Utils.goWeb(getContext(), model.getDetailUrl()));
//                }
//            }
//
//            @Override
//            protected void onBindBroccoli(RecyclerViewHolder holder, Broccoli broccoli) {
//                broccoli.addPlaceholders(
//                        holder.findView(R.id.tv_user_name),
//                        holder.findView(R.id.tv_tag),
//                        holder.findView(R.id.tv_title),
//                        holder.findView(R.id.tv_summary),
//                        holder.findView(R.id.tv_praise),
//                        holder.findView(R.id.tv_comment),
//                        holder.findView(R.id.tv_read),
//                        holder.findView(R.id.iv_image)
//                );
//            }
//        };
//        InvitationQuery query = new InvitationQuery();
//        query.setPageNum(this.pageNow);
//        query.setPageSize(this.pageMax);
//        CommonRequest<InvitationQuery> commonRequest = new CommonRequest<>();
//        commonRequest.setBody(query);
//        String body = JsonUtil.toJson(commonRequest);
//        XHttp.post(FinalEnum.frontUrl + "/healthy/invitation/query")
//                .upJson(body)
//                .syncRequest(false)
//                .onMainThread(true)
//                .execute(new CallBackProxy<CommonResponse<CommonPage>, CommonPage>(new TipCallBack<CommonPage>() {
//                    @Override
//                    public void onSuccess(CommonPage response) throws Throwable {
//                        invitationList = response.getData();
//                    }
//                }){});
        //资讯
        mNewsAdapter = new BroccoliSimpleDelegateAdapter<InvitationDTO>(R.layout.adapter_news_invitation_view_list_item, new LinearLayoutHelper()) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, InvitationDTO model, int position) {
                if (model.getUser().getIcon() != null) {
                    holder.image(R.id.invitation_avatar,FinalEnum.frontUrl + model.getUser().getIcon());
                }
//                RadiusImageView img1 = holder.findViewById(R.id.invitation_image1);
//                RadiusImageView img2 = holder.findViewById(R.id.invitation_image2);
//                RadiusImageView img3 = holder.findViewById(R.id.invitation_image3);
                holder.image(R.id.invitation_image1,getResources().getDrawable(R.drawable.ic_ty));
                holder.image(R.id.invitation_image2,getResources().getDrawable(R.drawable.ic_ty));
                holder.image(R.id.invitation_image3,getResources().getDrawable(R.drawable.ic_ty));
                if (model.getPictureList() != null) {
                    List<InvitationPictureDTO> iList = model.getPictureList();
                    LinearLayout linearLayout = holder.findViewById(R.id.image_linear_layout);
                    linearLayout.setVisibility(View.VISIBLE);
                    if (iList.size() == 0) {
                        linearLayout.setVisibility(View.GONE);
                    } else if (iList.size() >= 1) {
                        holder.image(R.id.invitation_image1,FinalEnum.frontUrl + model.getPictureList().get(0).getUrl());
                        if (iList.size() >= 2) {
                            holder.image(R.id.invitation_image2,FinalEnum.frontUrl + model.getPictureList().get(1).getUrl());
                            if (iList.size() >= 3) {
                                holder.image(R.id.invitation_image3,FinalEnum.frontUrl + model.getPictureList().get(2).getUrl());
                            }
                        }
                    }
                }
//                if (model.getPictureList() != null) {
//                    List<InvitationPictureDTO> iList = model.getPictureList();
//                    if (iList.size() == 0) {
//                        img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_ty));
//                        img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_ty));
//                        img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_ty));
//                    }
//                    if (iList.size() >= 1) {
//                        img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_ty));
//                        img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_ty));
//                        if (!iList.get(0).getUrl().equals(img1.getTag(R.id.img_url))) {
//                            img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_ty));
//                        }
//                        img1.setTag(R.id.img_url,iList.get(0).getUrl());
//                        Glide.with(NewsFragment.this)
//                                .load(FinalEnum.frontUrl + iList.get(0).getUrl())
//                                .placeholder(R.drawable.ic_ty)
//                                .into(img1);
//                        if (iList.size() >= 2) {
//                            img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_ty));
//                            if (!iList.get(1).getUrl().equals(img2.getTag(R.id.img_url))) {
//                                img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_ty));
//                            }
//                            img2.setTag(R.id.img_url,iList.get(1).getUrl());
//                            Glide.with(NewsFragment.this)
//                                    .load(FinalEnum.frontUrl + iList.get(1).getUrl())
//                                    .placeholder(R.drawable.ic_ty)
//                                    .into(img2);
//                            if (iList.size() >= 3) {
//                                if (!iList.get(2).getUrl().equals(img3.getTag(R.id.img_url))) {
//                                    img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_ty));
//                                }
//                                img3.setTag(R.id.img_url,iList.get(2).getUrl());
//                                Glide.with(NewsFragment.this)
//                                        .load(FinalEnum.frontUrl + iList.get(2).getUrl())
//                                        .placeholder(R.drawable.ic_ty)
//                                        .into(img3);
//                            }
//                        }
//                    }
//                }
                holder.text(R.id.invitation_nickname,model.getUser().getNickname());
                holder.text(R.id.invitation_title,model.getTitle());
                holder.text(R.id.invitation_content,model.getContent());
                holder.text(R.id.tv_praise,String.valueOf(model.getGood()));
                holder.text(R.id.tv_comment,String.valueOf(model.getComment()));
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
                        }){});
//                refreshLayout.finishRefresh();
            }, 1000);
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
                        }){});
//                refreshLayout.finishLoadMoreWithNoMoreData();
            }, 1000);
        });
        refreshLayout.autoRefresh();
    }
}
