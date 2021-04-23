package com.xuexiang.chh_healthy_android.fragment.step;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.core.BaseActivity;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.fragment.InvitationViewFragment;
import com.xuexiang.chh_healthy_android.fragment.ReplyFragment;
import com.xuexiang.chh_healthy_android.step.UpdateUiCallBack;
import com.xuexiang.chh_healthy_android.step.service.StepService;
import com.xuexiang.chh_healthy_android.utils.MMKVUtils;
import com.xuexiang.chh_healthy_android.utils.SharedPreferencesUtils;
import com.xuexiang.chh_healthy_android.widget.StepArcView;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xutil.app.ServiceUtils;

import butterknife.BindView;


/**
 * 记步主页
 */
@Page(anim = CoreAnim.none)
public class StepCountFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.tv_data)
    TextView tv_data;
    @BindView(R.id.cc)
    StepArcView cc;
    @BindView(R.id.tv_set)
    TextView tv_set;
    @BindView(R.id.tv_isSupport)
    TextView tv_isSupport;

    StepService mService;

    int current;

    private boolean isBind = false;

    @Override
    protected int getLayoutId() {
        return R.layout.step_main;
    }

    @Override
    protected void initViews() {
        initData();
        addListener();
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorTitleBar));
        titleBar.setTitle("步数小助手");
        titleBar.setLeftImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        titleBar.setActionTextColor(getResources().getColor(R.color.white));
        return titleBar;
//        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupService();
    }

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(getActivity(), StepService.class);
        isBind = getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            mService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            String planWalk_QTY = MMKVUtils.getString("planWalk_QTY", "7000");
            tv_isSupport.setText(planWalk_QTY + "步");
            current = stepService.getStepCount();
            cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepService.getStepCount());
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private void addListener() {
        tv_set.setOnClickListener(this);
        tv_data.setOnClickListener(this);
    }

    private void initData() {
        //获取用户设置的计划锻炼步数，没有设置过的话默认7000
        String planWalk_QTY = MMKVUtils.getString("planWalk_QTY", "7000");
        //设置当前步数为0
        cc.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_set:
                PageOption.to(SetPlanFragment.class)
                        .setRequestCode(100)
                        .setAddToBackStack(true)
                        .setAnim(CoreAnim.slide)
                        .open(this);
                break;
            case R.id.tv_data:
                PageOption.to(HistoryStepFragment.class)
                        .setRequestCode(100)
                        .setAddToBackStack(true)
                        .setAnim(CoreAnim.slide)
                        .open(this);
                break;
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == 101) {
                String planWalk_QTY = data.getExtras().getString("planWalk_QTY");
                tv_isSupport.setText(planWalk_QTY + "步");
                cc.setCurrentCount(Integer.parseInt(planWalk_QTY), current);
            }
        }
        super.onFragmentResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (isBind) {
            getActivity().unbindService(conn);
        }
        super.onDestroy();
    }
}
