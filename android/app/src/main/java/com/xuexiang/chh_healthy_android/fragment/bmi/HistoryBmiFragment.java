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
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.BMIDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.query.BMIQuery;
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

    private BroccoliSimpleDelegateAdapter<BMIDTO> bmiAdapter;
    private DelegateAdapter delegateAdapter;
    private int pageNum;
    private int pageMax;
    private List<BMIDTO> bmiList;



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
        titleBar.setTitle("历史BMI");
        titleBar.setLeftImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        return titleBar;
//        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history_bmi;
    }

    @Override
    protected void initViews() {
        bmiAdapter = new BroccoliSimpleDelegateAdapter<BMIDTO>(R.layout.fragment_bmi_history_body,new LinearLayoutHelper()) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, BMIDTO model, int position) {
                if (model.getUser().getIcon() != null) {
                    holder.image(R.id.invitation_avatar,FinalEnum.frontUrl + model.getUser().getIcon());
                }
                holder.text(R.id.nickname,model.getUser().getNickname());
                holder.text(R.id.tv_bmi,model.getBmi() + " " + judgeType(Float.parseFloat(model.getBmi())));
                holder.text(R.id.tv_height,model.getHeight() + "cm");
                holder.text(R.id.tv_weight,model.getWeight() + "kg");
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
        delegateAdapter.addAdapter(bmiAdapter);
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
        BMIQuery query = new BMIQuery();
        query.setCreatedBy(TokenUtils.getUserInfo().getId());
        query.setPageNum(pageNum);
        query.setPageSize(pageMax);
        CommonRequest<BMIQuery> commonRequest = new CommonRequest<>();
        commonRequest.setBody(query);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/bmi/query")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<List<BMIDTO>>, List<BMIDTO>>(new TipCallBack<List<BMIDTO>>() {
                    @Override
                    public void onSuccess(List<BMIDTO> response) throws Throwable {
                        bmiList = response;
                        bmiAdapter.refresh(bmiList);
                        refreshLayout.finishRefresh();
                    }
                }){});
    }

    private String judgeType(Float bmi) {
        if (bmi <= 18.4) {
            return "偏瘦";
        } else if (bmi > 18.4 && bmi <= 23.9 ) {
            return "正常";
        } else if (bmi > 23.9 && bmi <= 27.9) {
            return "过重";
        } else if (bmi >27.9) {
            return "肥胖";
        }
        return "异常数值";
    }

    public void LoadMore() {
        BMIQuery query = new BMIQuery();
        query.setCreatedBy(TokenUtils.getUserInfo().getId());
        query.setPageNum(pageNum);
        query.setPageSize(pageMax);
        CommonRequest<BMIQuery> commonRequest = new CommonRequest<>();
        commonRequest.setBody(query);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/bmi/query")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<List<BMIDTO>>, List<BMIDTO>>(new TipCallBack<List<BMIDTO>>() {
                    @Override
                    public void onSuccess(List<BMIDTO> response) throws Throwable {
                        bmiList = response;
                        bmiAdapter.loadMore(bmiList);
                        refreshLayout.finishLoadMore();
                    }
                }){});
    }
}
