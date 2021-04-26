
package com.xuexiang.chh_healthy_android.fragment.utils;

import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.activity.BmiActivity;
import com.xuexiang.chh_healthy_android.activity.PlanActivity;
import com.xuexiang.chh_healthy_android.activity.StepActivity;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.app.ActivityUtils;

import butterknife.BindView;

@Page(anim = CoreAnim.none)
public class UtilsFragment extends BaseFragment implements SuperTextView.OnSuperTextViewClickListener{

    @BindView(R.id.st_step)
    SuperTextView stStep;
    @BindView(R.id.st_bmi)
    SuperTextView stBmi;
    @BindView(R.id.st_plan)
    SuperTextView stPlan;

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
        return R.layout.fragment_utils;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {
        stStep.setOnSuperTextViewClickListener(this);
        stBmi.setOnSuperTextViewClickListener(this);
        stPlan.setOnSuperTextViewClickListener(this);
        super.initListeners();
    }

    @Override
    public void onClick(SuperTextView superTextView) {
        if (superTextView.getId() == R.id.st_step) {
            ActivityUtils.startActivity(StepActivity.class);
        } else if (superTextView.getId() == R.id.st_bmi) {
            ActivityUtils.startActivity(BmiActivity.class);
        } else if (superTextView.getId() == R.id.st_plan) {
            ActivityUtils.startActivity(PlanActivity.class);
        }
    }
}
