package com.xuexiang.chh_healthy_android.fragment.bmi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonRequest;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.BMIDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.query.BMIQuery;
import com.xuexiang.chh_healthy_android.fragment.step.SetPlanFragment;
import com.xuexiang.chh_healthy_android.step.bean.StepData;
import com.xuexiang.chh_healthy_android.step.service.StepService;
import com.xuexiang.chh_healthy_android.utils.MMKVUtils;
import com.xuexiang.chh_healthy_android.utils.TokenUtils;
import com.xuexiang.chh_healthy_android.utils.XToastUtils;
import com.xuexiang.chh_healthy_android.widget.StepArcView;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.alpha.XUIAlphaButton;
import com.xuexiang.xui.widget.picker.RulerView;
import com.xuexiang.xutil.common.logger.Logger;
import com.xuexiang.xutil.net.JsonUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;


/**
 * 记步主页
 */
@Page(anim = CoreAnim.none)
public class BmiFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.rv_weight)
    RulerView rvWeight;
    @BindView(R.id.rv_height)
    RulerView rvHeight;
    @BindView(R.id.bt_count)
    XUIAlphaButton btCount;
    @BindView(R.id.ll_bmi)
    LinearLayout llBmi;
    @BindView(R.id.tv_bmi)
    TextView tvBmi;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.bt_save)
    XUIAlphaButton btSave;

    private String bmiString;
    private String date;
    private List<BMIDTO> list;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bmi_main;
    }

    @Override
    protected void initViews() {
        llBmi.setVisibility(View.GONE);
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorTitleBar));
        titleBar.setTitle("BMI小助手");
        titleBar.setLeftImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        titleBar.setActionTextColor(getResources().getColor(R.color.white));

        return titleBar;
//        return null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initListeners() {
        btCount.setOnClickListener(this);
        super.initListeners();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_count:
                countBMI();
                break;
            case R.id.bt_save:
                save();
                break;
        }
    }

    public void countBMI() {
        Float height = rvHeight.getCurrentValue();
        Float weight = rvWeight.getCurrentValue();
        Float bmi = weight / ((height / 100) * (height / 100));
        BigDecimal b = new BigDecimal(bmi);
        bmi = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        bmiString = bmi.toString();
        tvBmi.setText(bmiString);
        tvType.setText(judgeType(bmi));
        llBmi.setVisibility(View.VISIBLE);
    }

    private String judgeType(Float bmi) {
        if (bmi <= 18.4) {
            return "偏瘦";
        } else if (18.4>bmi && bmi <= 23.9 ) {
            return "正常";
        } else if (bmi > 23.9 && bmi <= 27.9) {
            return "过重";
        } else if (bmi >27.9) {
            return "肥胖";
        }
        return "异常数值";
    }

    private String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public void save() {
        BMIQuery query = new BMIQuery();
        query.setToday(getTodayDate());
        query.setCreatedBy(TokenUtils.getUserInfo().getId());
        CommonRequest<BMIQuery> commonRequest = new CommonRequest<>();
        commonRequest.setBody(query);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/step/query")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<List<BMIDTO>>, List<BMIDTO>>(new TipCallBack<List<BMIDTO>>() {
                    @Override
                    public void onSuccess(List<BMIDTO> response) throws Throwable {
                        list = response;
                        if (list.size() == 0 || list.isEmpty()) {
                            BMIDTO data = new BMIDTO();
                            data.setToday(getTodayDate());
                            data.setBmi(bmiString);
                            data.setCreatedBy(TokenUtils.getUserInfo().getId());
                            saveBMI(data);
                        } else if (list.size() == 1) {
                            BMIDTO data = list.get(0);
                            data.setBmi(bmiString);
                            data.setVersion(data.getVersion());
                            updateBMI(data);
                        } else {
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                    }
                }) {
                });
    }

    public void saveBMI(BMIDTO data) {
        CommonRequest<BMIDTO> commonRequest = new CommonRequest<>();
        commonRequest.setBody(data);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/bmi/save")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<BMIDTO>, BMIDTO>(new TipCallBack<BMIDTO>() {
                    @Override
                    public void onSuccess(BMIDTO response) throws Throwable {
                        XToastUtils.success("保存BMI成功");
                    }
                }){});
    }

    public void updateBMI(BMIDTO data) {
        CommonRequest<BMIDTO> commonRequest = new CommonRequest<>();
        commonRequest.setBody(data);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/bmi/update")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<BMIDTO>, BMIDTO>(new TipCallBack<BMIDTO>() {
                    @Override
                    public void onSuccess(BMIDTO response) throws Throwable {
                        XToastUtils.success("保存BMI成功");
                    }
                }){});
    }
}
