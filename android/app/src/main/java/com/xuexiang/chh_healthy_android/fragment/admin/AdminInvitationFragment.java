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

package com.xuexiang.chh_healthy_android.fragment.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.activity.InvitationViewActivity;
import com.xuexiang.chh_healthy_android.adapter.base.broccoli.BroccoliSimpleDelegateAdapter;
import com.xuexiang.chh_healthy_android.adapter.base.delegate.SimpleDelegateAdapter;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipCallBack;
import com.xuexiang.chh_healthy_android.core.http.callback.TipProgressLoadingCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonRequest;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.InvitationDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.UserDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.query.InvitationQuery;
import com.xuexiang.chh_healthy_android.core.http.pojo.query.UserQuery;
import com.xuexiang.chh_healthy_android.utils.XToastUtils;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.searchview.MaterialSearchView;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.net.JsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.samlss.broccoli.Broccoli;


@Page(anim = CoreAnim.none)
public class AdminInvitationFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;

    List<InvitationDTO> invitationList = new ArrayList<>();

    int pageMax;
    int pageNow;

    String search = null;
    int flag = 0;

    private SimpleDelegateAdapter<InvitationDTO> invitationAdapter;

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setTitle("");
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorTitleBar));
        titleBar.setLeftImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        titleBar.setActionTextColor(getResources().getColor(R.color.white));
        titleBar.addAction(new TitleBar.TextAction("名称搜索") {
            @Override
            public void performAction(View view) {
                flag = 2;
                mSearchView.showSearch();
                mSearchView.setHint("按名称搜索");
                mSearchView.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });
        titleBar.addAction(new TitleBar.TextAction("ID搜索") {
            @Override
            public void performAction(View view) {
                flag = 1;
                mSearchView.showSearch();
                mSearchView.setHint("按ID搜索");
                mSearchView.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });
        titleBar.addAction(new TitleBar.TextAction("重置") {
            @Override
            public void performAction(View view) {
                search = null;
                flag = 0;
                initRequest(search,flag);
            }
        });
        return titleBar;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_main;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        mSearchView.setVoiceSearch(false);
        mSearchView.setEllipsize(true);
        mSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (StringUtils.isEmpty(query)) {
                    search  = null;
                } else {
                    search = query;
                }
                initRequest(search,flag);
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
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(getContext());
        recyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);
        pageMax = 10;
        pageNow = 1;
        invitationAdapter = new BroccoliSimpleDelegateAdapter<InvitationDTO>(R.layout.fragment_invitation_body,new LinearLayoutHelper()) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, InvitationDTO model, int position) {
                holder.text(R.id.tv_username,model.getUser().getUsername());
                holder.text(R.id.tv_id,model.getId().toString());
                holder.text(R.id.tv_title,model.getTitle());
                int status = model.getStatus().intValue();
                SuperButton delete = holder.findViewById(R.id.btn_delete);
                delete.setVisibility(View.VISIBLE);
                if (status == 1) {
                    holder.text(R.id.tv_status,"正常");
                    delete.setText("封帖");
                } else if (status == 2) {
                    holder.text(R.id.tv_status,"封帖中");
                    delete.setText("解封");
                }
                holder.click(R.id.btn_delete,v -> {
                    if (status == 1) {
                        model.setStatus((byte) 2);
                    } else if (status == 2) {
                        model.setStatus((byte) 1);
                    }
                    update(model,position);
                });
                holder.click(R.id.invitation_view,v -> ActivityUtils.startActivity(InvitationViewActivity.class,"invitationId",model.getId()));
            }
            @Override
            protected void onBindBroccoli(RecyclerViewHolder holder, Broccoli broccoli) {
            }
        };
        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        delegateAdapter.addAdapter(invitationAdapter);
        recyclerView.setAdapter(delegateAdapter);
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.getLayout().postDelayed(() -> {
                initRequest(search,flag);
            }, 1);
        });
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            refreshLayout.getLayout().postDelayed(() -> {
                loadMore(search,flag);
            }, 1);
        });
    }

    private void initRequest(String search,int flag) {
        InvitationQuery query = new InvitationQuery();
        this.pageNow = 1;
        this.pageMax = 10;
        query.setPageNum(this.pageNow);
        query.setPageSize(this.pageMax);
        if (search != null && flag == 1) {
            query.setQueryId(Long.parseLong(search));
        } else if (search != null && flag == 2) {
            query.setTitle(search);
        }
        CommonRequest<InvitationQuery> commonRequest = new CommonRequest<>();
        commonRequest.setBody(query);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/invitation/admin")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<List<InvitationDTO>>, List<InvitationDTO>>(new TipCallBack<List<InvitationDTO>>() {
                    @Override
                    public void onSuccess(List<InvitationDTO> response) throws Throwable {
                        invitationList = response;
                        invitationAdapter.refresh(invitationList);
                        refreshLayout.finishRefresh();
                    }
                    @Override
                    public void onError(ApiException e) {
                        refreshLayout.finishRefresh();
                        super.onError(e);
                    }
                }){});
    }

    private void update(InvitationDTO model,int position) {
        CommonRequest<InvitationDTO> commonRequest = new CommonRequest<>();
        commonRequest.setBody(model);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/invitation/update")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<InvitationDTO>, InvitationDTO>(new TipProgressLoadingCallBack<InvitationDTO>(this) {
                    @Override
                    public void onSuccess(InvitationDTO response) throws Throwable {
                        XToastUtils.success("操作成功");
                        invitationList.set(position,response);
                        invitationAdapter.refresh(position,response);
                    }
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }
                }){});
    }

    private void loadMore(String search,int flag) {
        InvitationQuery query = new InvitationQuery();
        this.pageNow ++;
        this.pageMax = 10;
        if (search != null && flag == 1) {
            query.setQueryId(Long.parseLong(search));
        } else if (search != null && flag == 2) {
            query.setTitle(search);
        }
        query.setPageNum(this.pageNow);
        query.setPageSize(this.pageMax);
        CommonRequest<InvitationQuery> commonRequest = new CommonRequest<>();
        commonRequest.setBody(query);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/invitation/admin")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<List<InvitationDTO>>, List<InvitationDTO>>(new TipCallBack<List<InvitationDTO>>() {
                    @Override
                    public void onSuccess(List<InvitationDTO> response) throws Throwable {
                        invitationList = response;
                        invitationAdapter.loadMore(invitationList);
                        invitationList = invitationAdapter.getData();
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
