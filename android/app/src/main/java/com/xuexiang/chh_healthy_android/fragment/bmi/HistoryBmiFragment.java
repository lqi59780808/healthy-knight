package com.xuexiang.chh_healthy_android.fragment.bmi;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.adapter.base.broccoli.BroccoliSimpleDelegateAdapter;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonRequest;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.query.StepQuery;
import com.xuexiang.chh_healthy_android.step.bean.StepData;
import com.xuexiang.chh_healthy_android.utils.TokenUtils;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xutil.net.JsonUtil;

import java.util.List;

import butterknife.BindView;
import me.samlss.broccoli.Broccoli;

@Page(anim = CoreAnim.none)
public class HistoryBmiFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerViewImages;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private BroccoliSimpleDelegateAdapter<StepData> stepAdapter;
    private DelegateAdapter delegateAdapter;
    private int pageNum;
    private int pageMax;
    private List<StepData> stepList;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        pageNum = 1;
        pageMax = 10;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorTitleBar));
        titleBar.setTitle("历史步数");
        titleBar.setLeftImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        titleBar.setActionTextColor(getResources().getColor(R.color.white));
        return titleBar;
//        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history_step;
    }

    @Override
    protected void initViews() {
        stepAdapter = new BroccoliSimpleDelegateAdapter<StepData>(R.layout.fragment_step_history_body,new LinearLayoutHelper()) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, StepData model, int position) {
                if (model.getUser().getIcon() != null) {
                    holder.image(R.id.invitation_avatar,FinalEnum.frontUrl + model.getUser().getIcon());
                }
                holder.text(R.id.nickname,model.getUser().getNickname());
                holder.text(R.id.tv_step,model.getStep());
                holder.text(R.id.tv_date,model.getToday());
            }

            @Override
            protected void onBindBroccoli(RecyclerViewHolder holder, Broccoli broccoli) {
            }
        };
        VirtualLayoutManager virtualLayoutManager1 = new VirtualLayoutManager(getContext());
        recyclerViewImages.setLayoutManager(virtualLayoutManager1);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerViewImages.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager1);
        delegateAdapter.addAdapter(stepAdapter);
        recyclerViewImages.setAdapter(delegateAdapter);

        initRequest();
    }

    @Override
    public void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.getLayout().postDelayed(() -> {
                pageNum = 1;
                pageMax = 10;
                initRequest();
            }, 1);
        });
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            refreshLayout.getLayout().postDelayed(() -> {
                pageNum ++;
                pageMax = 10;
                LoadMore();
            }, 1);
        });
    }

    public void initRequest() {
        StepQuery query = new StepQuery();
        query.setCreatedBy(TokenUtils.getUserInfo().getId());
        query.setPageNum(pageNum);
        query.setPageSize(pageMax);
        CommonRequest<StepQuery> commonRequest = new CommonRequest<>();
        commonRequest.setBody(query);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/step/query")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<List<StepData>>, List<StepData>>(new TipCallBack<List<StepData>>() {
                    @Override
                    public void onSuccess(List<StepData> response) throws Throwable {
                        stepList = response;
                        stepAdapter.refresh(stepList);
                        refreshLayout.finishRefresh();
                    }
                }){});
    }

    public void LoadMore() {
        StepQuery query = new StepQuery();
        query.setCreatedBy(TokenUtils.getUserInfo().getId());
        query.setPageNum(pageNum);
        query.setPageSize(pageMax);
        CommonRequest<StepQuery> commonRequest = new CommonRequest<>();
        commonRequest.setBody(query);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/step/query")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<List<StepData>>, List<StepData>>(new TipCallBack<List<StepData>>() {
                    @Override
                    public void onSuccess(List<StepData> response) throws Throwable {
                        stepList = response;
                        stepAdapter.loadMore(stepList);
                        refreshLayout.finishLoadMore();
                    }
                }){});
    }
}
