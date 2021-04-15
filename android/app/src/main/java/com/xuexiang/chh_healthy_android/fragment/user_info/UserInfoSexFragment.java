package com.xuexiang.chh_healthy_android.fragment.user_info;

import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipProgressLoadingCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonRequest;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.UserDTO;
import com.xuexiang.chh_healthy_android.utils.TokenUtils;
import com.xuexiang.chh_healthy_android.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.net.JsonUtil;

import butterknife.BindView;

@Page(anim = CoreAnim.none)
public class UserInfoSexFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener{

    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    Integer sex;

    private UserDTO userInfo;

    private final int RESULT_SEX = 101;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_info_sex;
    }

    @Override
    protected void initViews() {
        userInfo = TokenUtils.getUserInfo();
        sex = (int)userInfo.getSex();
        rgSex.setOnCheckedChangeListener(this);
        //男
        if (sex == 0) {
            RadioButton rb = rgSex.findViewById(R.id.rb_male);
            rb.setChecked(true);
        } else if (sex == 1) {
            RadioButton rb = rgSex.findViewById(R.id.rb_female);
            rb.setChecked(true);
        } else if (sex == 2) {
            RadioButton rb = rgSex.findViewById(R.id.rb_secret);
            rb.setChecked(true);
        }
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorTitleBar));
        titleBar.setTitle("性别");
        titleBar.setLeftImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        titleBar.setActionTextColor(getResources().getColor(R.color.white));
        titleBar.addAction(new TitleBar.TextAction("保存") {
            @Override
            public void performAction(View view) {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(userInfo.getId());
                userDTO.setSex(sex.byteValue());
                userDTO.setVersion(userInfo.getVersion());
                CommonRequest<UserDTO> commonRequest = new CommonRequest<>();
                commonRequest.setBody(userDTO);
                String body = JsonUtil.toJson(commonRequest);
                XHttp.post(FinalEnum.frontUrl + "/healthy/user/update2")
                        .upJson(body)
                        .syncRequest(false)
                        .onMainThread(true)
                        .execute(new CallBackProxy<CommonResponse<UserDTO>, UserDTO>(new TipProgressLoadingCallBack<UserDTO>(UserInfoSexFragment.this) {
                            @Override
                            public void onSuccess(UserDTO response) throws Throwable {
                                XToastUtils.success("保存成功");
                                userInfo.setSex(sex.byteValue());
                                userInfo.setVersion(userInfo.getVersion() + 1);
                                TokenUtils.putUserInfo(userInfo);
                                Intent intent = new Intent();
                                intent.putExtra("sex",sex);
                                setFragmentResult(RESULT_SEX,intent);
                                popToBack();
                            }
                        }){});
            }
        });
        return titleBar;
    }
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        RadioButton radioButton = radioGroup.findViewById(i);
        if (radioButton.getText().equals("男")) {
            sex = 0;
        } else if (radioButton.getText().equals("女")) {
            sex = 1;
        } else {
            sex = 2;
        }
    }
}
