package com.xuexiang.chh_healthy_android.fragment.admin;

import android.content.Intent;
import android.os.Bundle;
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
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xutil.net.JsonUtil;

import butterknife.BindView;

@Page(anim = CoreAnim.none)
public class AdminUserUpdateFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener{

    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    @BindView(R.id.et_username)
    MaterialEditText etUsername;
    @BindView(R.id.et_nickname)
    MaterialEditText etNickname;
    @BindView(R.id.et_password)
    MaterialEditText etPassword;
    @BindView(R.id.et_email)
    MaterialEditText etEmail;

    String type;
    int position = -1;

    Integer sex;

    private UserDTO userInfo = null;

    private final int RESULT_UPDATE = 101;
    private final int RESULT_ADD = 102;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_admin;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        if ("修改".equals(type)) {
            userInfo = JsonUtil.fromJson(bundle.getString("model"),UserDTO.class);
            position = bundle.getInt("position");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        if (userInfo != null) {
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
            etUsername.setText(userInfo.getUsername());
            etUsername.setFocusable(false);
            etEmail.setText(userInfo.getEmail());
        } else {
            userInfo = new UserDTO();
        }
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorTitleBar));
        if ("修改".equals(type)) {
            titleBar.setTitle("修改用户");
        } else {
            titleBar.setTitle("新增用户");
        }
        titleBar.setLeftImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        titleBar.setActionTextColor(getResources().getColor(R.color.white));
        titleBar.addAction(new TitleBar.TextAction("确定") {
            @Override
            public void performAction(View view) {
                String url = "";
                userInfo.setUsername(etUsername.getEditValue());
                userInfo.setSex(sex.byteValue());
                userInfo.setNickname(etNickname.getEditValue());
                userInfo.setEmail(etEmail.getEditValue());
                userInfo.setPassword("".equals(etPassword.getEditValue()) ? null : etPassword.getEditValue());
                if ("修改".equals(type) ) {
                    url = "/healthy/user/update2";
                } else {
                    url = "/healthy/user/register";
                    userInfo.setStatus((byte) 2);
                    if (userInfo.getPassword() == null || userInfo.getPassword().equals("")) {
                        XToastUtils.error("密码不能为空");
                        return;
                    }
                }
                CommonRequest<UserDTO> commonRequest = new CommonRequest<>();
                commonRequest.setBody(userInfo);
                String body = JsonUtil.toJson(commonRequest);
                XHttp.post(FinalEnum.frontUrl + url)
                        .upJson(body)
                        .syncRequest(false)
                        .onMainThread(true)
                        .execute(new CallBackProxy<CommonResponse<UserDTO>, UserDTO>(new TipProgressLoadingCallBack<UserDTO>(AdminUserUpdateFragment.this) {
                            @Override
                            public void onSuccess(UserDTO response) throws Throwable {
                                if (type.equals("修改")) {
                                    XToastUtils.success("修改成功");
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("model",JsonUtil.toJson(response));
                                    bundle.putInt("position",position);
                                    setFragmentResult(RESULT_UPDATE,intent);
                                    popToBack();
                                } else {
                                    XToastUtils.success("新增成功");
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("model",JsonUtil.toJson(response));
                                    setFragmentResult(RESULT_ADD,intent);
                                    popToBack();
                                }

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
