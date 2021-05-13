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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.adapter.base.broccoli.BroccoliSimpleDelegateAdapter;
import com.xuexiang.chh_healthy_android.adapter.base.delegate.SimpleDelegateAdapter;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipCallBack;
import com.xuexiang.chh_healthy_android.core.http.callback.TipProgressLoadingCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonRequest;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.InvitationDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.PlanDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.UserDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.query.InvitationQuery;
import com.xuexiang.chh_healthy_android.core.http.pojo.query.UserQuery;
import com.xuexiang.chh_healthy_android.fragment.UserSettingFragment;
import com.xuexiang.chh_healthy_android.utils.MMKVUtils;
import com.xuexiang.chh_healthy_android.utils.TokenUtils;
import com.xuexiang.chh_healthy_android.utils.XToastUtils;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.SwitchIconView;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xutil.net.JsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.samlss.broccoli.Broccoli;

/**
 * 首页动态
 *
 * @author xuexiang
 * @since 2019-10-30 00:15
 */
@Page(anim = CoreAnim.none)
public class AdminUserFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    UserDTO res;

    List<UserDTO> userList = new ArrayList<>();

    int pageMax;
    int pageNow;

    private SimpleDelegateAdapter<UserDTO> userAdapter;

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorTitleBar));
        titleBar.setTitle("用户管理");
        titleBar.setLeftImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        titleBar.setActionTextColor(getResources().getColor(R.color.white));
        titleBar.addAction(new TitleBar.TextAction("新增") {
            @Override
            public void performAction(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("type","新增");
                PageOption.to(AdminUserUpdateFragment.class)
                        .setBundle(bundle)
                        .setRequestCode(100)
                        .setAddToBackStack(true)
                        .setAnim(CoreAnim.slide)
                        .open(AdminUserFragment.this);
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
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(getContext());
        recyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);
        pageMax = 10;
        pageNow = 1;
        userAdapter = new BroccoliSimpleDelegateAdapter<UserDTO>(R.layout.fragment_user_body,new LinearLayoutHelper()) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, UserDTO model, int position) {
                holder.text(R.id.tv_username,model.getUsername());
                holder.text(R.id.tv_nickname,model.getNickname());
                int sex = model.getSex().intValue();
                if (sex == 0) {
                    holder.text(R.id.tv_sex,"男");
                } else if (sex == 1) {
                    holder.text(R.id.tv_sex,"女");
                } else if (sex == 2) {
                    holder.text(R.id.tv_sex,"保密");
                }
                int status = model.getStatus().intValue();
                SuperButton delete = holder.findViewById(R.id.btn_delete);
                if (status == 2) {
                    holder.text(R.id.tv_status,"正常");
                    delete.setText("封禁");
                } else if (status == 3) {
                    holder.text(R.id.tv_status,"封禁中");
                    delete.setText("解封");
                }
                holder.click(R.id.btn_delete,v -> {
                    if (status == 2) {
                        model.setStatus((byte) 3);
                    } else if (status == 3) {
                        model.setStatus((byte) 2);
                    }
                    updateUser(model,position);
                });
                holder.click(R.id.btn_update,v -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("type","修改");
                    bundle.putInt("position",position);
                    bundle.putString("model",JsonUtil.toJson(model));
                    PageOption.to(AdminUserUpdateFragment.class)
                            .setBundle(bundle)
                            .setRequestCode(100)
                            .setAddToBackStack(true)
                            .setAnim(CoreAnim.slide)
                            .open(AdminUserFragment.this);
                });
            }
            @Override
            protected void onBindBroccoli(RecyclerViewHolder holder, Broccoli broccoli) {
            }
        };
        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        delegateAdapter.addAdapter(userAdapter);
        recyclerView.setAdapter(delegateAdapter);
        initRequest(null);
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.getLayout().postDelayed(() -> {
                initRequest(null);
            }, 1);
        });
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            refreshLayout.getLayout().postDelayed(() -> {
                loadMore(null);
            }, 1);
        });
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == 101) {
                Bundle bundle = data.getExtras();
                int position = bundle.getInt("position");
                UserDTO userDTO = JsonUtil.fromJson(bundle.getString("model"),UserDTO.class);
                userList.set(position,userDTO);
                userAdapter.refresh(position,userDTO);
            } else if (resultCode == 102) {
                Bundle bundle = data.getExtras();
                UserDTO userDTO = JsonUtil.fromJson(bundle.getString("model"),UserDTO.class);
                userList.add(userDTO);
                userAdapter.add(userDTO);
            }
        }
    }

    private void initRequest(String username) {
        UserQuery query = new UserQuery();
        this.pageNow = 1;
        this.pageMax = 10;
        query.setPageNum(this.pageNow);
        query.setPageSize(this.pageMax);
        if (username != null) {
            query.setUsername(username);
        }
        CommonRequest<UserQuery> commonRequest = new CommonRequest<>();
        commonRequest.setBody(query);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/user/query")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<List<UserDTO>>, List<UserDTO>>(new TipCallBack<List<UserDTO>>() {
                    @Override
                    public void onSuccess(List<UserDTO> response) throws Throwable {
                        userList = response;
                        userAdapter.refresh(userList);
                        refreshLayout.finishRefresh();
                    }
                    @Override
                    public void onError(ApiException e) {
                        refreshLayout.finishRefresh();
                        super.onError(e);
                    }
                }){});
    }

    private void updateUser(UserDTO model,int position) {
        CommonRequest<UserDTO> commonRequest = new CommonRequest<>();
        commonRequest.setBody(model);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/user/update2")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<UserDTO>, UserDTO>(new TipCallBack<UserDTO>() {
                    @Override
                    public void onSuccess(UserDTO response) throws Throwable {
                        XToastUtils.success("操作成功");
                        userList.set(position,response);
                        userAdapter.refresh(position,response);
                    }
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }
                }){});
    }

    private void loadMore(String username) {
        UserQuery query = new UserQuery();
        this.pageNow ++;
        this.pageMax = 10;
        if (username != null) {
            query.setUsername(username);
        }
        query.setPageNum(this.pageNow);
        query.setPageSize(this.pageMax);
        CommonRequest<UserQuery> commonRequest = new CommonRequest<>();
        commonRequest.setBody(query);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/user/query")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<List<UserDTO>>, List<UserDTO>>(new TipCallBack<List<UserDTO>>() {
                    @Override
                    public void onSuccess(List<UserDTO> response) throws Throwable {
                        userList = response;
                        userAdapter.loadMore(userList);
                        userList = userAdapter.getData();
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
