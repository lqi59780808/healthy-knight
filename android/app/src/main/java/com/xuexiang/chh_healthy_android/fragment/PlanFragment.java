package com.xuexiang.chh_healthy_android.fragment;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

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
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.PlanDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.query.PlanQuery;
import com.xuexiang.chh_healthy_android.core.http.pojo.query.StepQuery;
import com.xuexiang.chh_healthy_android.step.bean.StepData;
import com.xuexiang.chh_healthy_android.utils.MMKVUtils;
import com.xuexiang.chh_healthy_android.utils.TokenUtils;
import com.xuexiang.chh_healthy_android.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.alpha.XUIAlphaLinearLayout;
import com.xuexiang.xui.widget.button.SwitchIconView;
import com.xuexiang.xui.widget.button.switchbutton.SwitchButton;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.layout.XUILinearLayout;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xutil.common.logger.Logger;
import com.xuexiang.xutil.net.JsonUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import me.samlss.broccoli.Broccoli;

@Page(anim = CoreAnim.none)
public class PlanFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener{
    @BindView(R.id.recycler_view)
    RecyclerView recyclerViewImages;
    @BindView(R.id.sb_use)
    SwitchButton sbUse;
    @BindView(R.id.et_reply)
    MaterialEditText etName;
    @BindView(R.id.ll_time)
    XUIAlphaLinearLayout llTime;
    @BindView(R.id.btn_reply)
    SuperButton btnAdd;
    @BindView(R.id.tv_time)
    TextView tvTime;

    private BroccoliSimpleDelegateAdapter<PlanDTO> planAdapter;
    private DelegateAdapter delegateAdapter;
    private List<PlanDTO> planList = new ArrayList<>();
    private PlanDTO plan = new PlanDTO();
    private int nowPosition = -1;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorTitleBar));
        titleBar.setTitle("计划小助手");
        titleBar.setLeftImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        titleBar.setActionTextColor(getResources().getColor(R.color.white));
        titleBar.addAction(new TitleBar.TextAction("保存") {
            @Override
            public void performAction(View view) {
                save();
                XToastUtils.success("保存成功");
            }
        });
        return titleBar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_plan_main;
    }


    @Override
    protected void initViews() {
        planAdapter = new BroccoliSimpleDelegateAdapter<PlanDTO>(R.layout.fragment_plan_body,new LinearLayoutHelper()) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, PlanDTO model, int position) {
                holder.text(R.id.tv_name,model.getName());
                holder.text(R.id.tv_time,model.getTime());
                SwitchIconView switchIconView = holder.findViewById(R.id.switchIconView);
                TextView tvSwitch = holder.findViewById(R.id.tv_switch);
                SuperButton btnDelete = holder.findViewById(R.id.btn_delete);
                LinearLayout llSwitch = holder.findViewById(R.id.ll_switch);
                if (model.getStatus() == 0) {
                    switchIconView.switchState();
                }
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.getLayoutPosition() != -1) {
                            planAdapter.delete(holder.getLayoutPosition());
                            planList.remove(holder.getLayoutPosition());
                            plan.setPlanList(planList);
                            resetEdit();
                            MMKVUtils.put("plan" + TokenUtils.getUserInfo().getId(),JsonUtil.toJson(plan));
                        }
                    }
                });
                llSwitch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switchIconView.switchState();
                        if (model.getStatus() == 0) {
                            model.setStatus((byte)1);
                            MMKVUtils.put("plan" + TokenUtils.getUserInfo().getId(),JsonUtil.toJson(plan));
                        } else {
                            model.setStatus((byte)0);
                            MMKVUtils.put("plan" + TokenUtils.getUserInfo().getId(),JsonUtil.toJson(plan));
                        }
                    }
                });
                holder.click(R.id.invitation_view,view -> {
                    focusEdit(etName);
                    etName.setText(model.getName());
                    tvTime.setText(model.getTime());
                    nowPosition = holder.getLayoutPosition();
                    btnAdd.setText("修改");
                });
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
        delegateAdapter.addAdapter(planAdapter);
        recyclerViewImages.setAdapter(delegateAdapter);
        initRequest();
    }

    @Override
    public void initListeners() {
        recyclerViewImages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (etName.isFocused()) {
                    nowPosition = -1;
                    etName.clearFocus();
                    etName.setText("");
                    btnAdd.setText("新增");
                } else {
                    nowPosition = -1;
                    etName.setText("");
                    btnAdd.setText("新增");
                }
                return false;
            }
        });
        sbUse.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.btn_reply,R.id.ll_time})
    @SingleClick
    void onClick(View v) {
        if (v.getId() == R.id.btn_reply) {
            if (etName.validate()) {
                if (nowPosition == -1) {
                    PlanDTO planDTO= new PlanDTO();
                    planDTO.setStatus((byte) 1);
                    planDTO.setName(etName.getEditValue());
                    planDTO.setTime((String) tvTime.getText());
                    planDTO.setCreatedBy(TokenUtils.getUserInfo().getId());
                    planList.add(planDTO);
                    plan.setPlanList(planList);
                    planAdapter.add(planDTO);
                    etName.clear();
                    XToastUtils.success("新增成功");
                    MMKVUtils.put("plan" + TokenUtils.getUserInfo().getId(),JsonUtil.toJson(plan));
                } else {
                    planList.get(nowPosition).setName(etName.getEditValue());
                    planList.get(nowPosition).setTime((String) tvTime.getText());
                    plan.setPlanList(planList);
                    planAdapter.refresh(nowPosition,planList.get(nowPosition));
                    XToastUtils.success("修改成功");
                    resetEdit();
                    MMKVUtils.put("plan" + TokenUtils.getUserInfo().getId(),JsonUtil.toJson(plan));
                }
            }
        } else if (v.getId() == R.id.ll_time) {
            showTimeDialog1();
        }
    }

    public void focusEdit(EditText editText) {
        editText.requestFocus();
        editText.requestFocusFromTouch();
        KeyboardUtils.showSoftInput(editText);
    }

    public void initRequest() {
        String planString = MMKVUtils.getString("plan" + TokenUtils.getUserInfo().getId(),"0");
        plan.setPlanList(planList);
        plan.setStatus((byte)0);
        plan.setCreatedBy(TokenUtils.getUserInfo().getId());
        if ("0".equals(planString)) {
            PlanQuery query = new PlanQuery();
            query.setCreatedBy(TokenUtils.getUserInfo().getId());
            CommonRequest<PlanQuery> commonRequest = new CommonRequest<>();
            commonRequest.setBody(query);
            String body = JsonUtil.toJson(commonRequest);
            XHttp.post(FinalEnum.frontUrl + "/healthy/plan/query")
                    .upJson(body)
                    .syncRequest(false)
                    .onMainThread(true)
                    .execute(new CallBackProxy<CommonResponse<List<PlanDTO>>, List<PlanDTO>>(new TipCallBack<List<PlanDTO>>() {
                        @Override
                        public void onSuccess(List<PlanDTO> response) throws Throwable {
                            planList = response;
                            plan.setPlanList(response);
                            plan.setStatus((byte)0);
                            plan.setCreatedBy(TokenUtils.getUserInfo().getId());
                            planAdapter.refresh(planList);
                        }

                        @Override
                        public void onError(ApiException e) {
                        }
                    }){});
        } else {
            plan = JsonUtil.fromJson(planString,PlanDTO.class);
            if (plan.getPlanList() == null) {
                planList = new ArrayList<>();
            } else {
                planList = plan.getPlanList();
            }
            planAdapter.refresh(planList);
        }
        if (plan.getStatus() == 1) {
            sbUse.setChecked(true);
        } else {
            sbUse.setChecked(false);
        }

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            plan.setStatus((byte) 1);
            MMKVUtils.put("plan" + TokenUtils.getUserInfo().getId(),JsonUtil.toJson(plan));
        } else {
            plan.setStatus((byte) 0);
            MMKVUtils.put("plan" + TokenUtils.getUserInfo().getId(),JsonUtil.toJson(plan));
        }
    }

    @Override
    public void onDestroyView() {
        save();
        super.onDestroyView();
    }

    private void save() {
        CommonRequest<PlanDTO> commonRequest = new CommonRequest<>();
        commonRequest.setBody(plan);
        String body = JsonUtil.toJson(commonRequest);
        MMKVUtils.put("plan" + TokenUtils.getUserInfo().getId(),JsonUtil.toJson(plan));
        XHttp.post(FinalEnum.frontUrl + "/healthy/plan/save")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<PlanDTO>, PlanDTO>(new TipCallBack<PlanDTO>() {
                    @Override
                    public void onSuccess(PlanDTO response) throws Throwable {
                    }

                    @Override
                    public void onError(ApiException e) {
                    }
                }){});
    }

    private void resetEdit() {
        if (etName.isFocused()) {
            nowPosition = -1;
            etName.clearFocus();
            etName.setText("");
            btnAdd.setText("新增");
        } else {
            nowPosition = -1;
            etName.setText("");
            btnAdd.setText("新增");
        }
    }

    private void showTimeDialog1() {
        final Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String time = (String) tvTime.getText();
        int tvHour = Integer.parseInt(time.substring(0,2));
        int tvMinute = Integer.parseInt(time.substring(3,5));
//        String time = tv_remind_time.getText().toString1().trim();
        final DateFormat df = new SimpleDateFormat("HH:mm");
//        Date date = null;
//        try {
//            date = df.parse(time);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if (null != date) {
//            calendar.setTime(date);
//        }
        new TimePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                String remaintime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                Date date = null;
                try {
                    date = df.parse(remaintime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (null != date) {
                    calendar.setTime(date);
                }
                tvTime.setText(df.format(date));
            }
        }, tvHour, tvMinute, true).show();
    }
}
