package com.xuexiang.chh_healthy_android.fragment.user_info;

import android.content.Intent;
import android.view.View;

import com.luck.picture.lib.entity.LocalMedia;
import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.activity.MainActivity;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipProgressLoadingCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonRequest;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.InvitationDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.UserDTO;
import com.xuexiang.chh_healthy_android.fragment.PublishFragment;
import com.xuexiang.chh_healthy_android.utils.TokenUtils;
import com.xuexiang.chh_healthy_android.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xhttp2.callback.impl.IProgressResponseCallBack;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xutil.net.JsonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@Page(anim = CoreAnim.none)
public class UserInfoNicknameFragment extends BaseFragment {

    @BindView(R.id.et_nickname)
    MaterialEditText nickname;

    private UserDTO userInfo;

    private final int RESULT_NICKNAME = 100;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_info_nickname;
    }

    @Override
    protected void initViews() {
        userInfo = TokenUtils.getUserInfo();
        nickname.setText(userInfo.getNickname());
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorTitleBar));
        titleBar.setTitle("昵称");
        titleBar.setLeftImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        titleBar.setActionTextColor(getResources().getColor(R.color.white));
        titleBar.addAction(new TitleBar.TextAction("保存") {
            @Override
            public void performAction(View view) {
                if (nickname.validate()) {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId(userInfo.getId());
                    userDTO.setNickname(nickname.getEditValue());
                    userDTO.setVersion(userInfo.getVersion());
                    CommonRequest<UserDTO> commonRequest = new CommonRequest<>();
                    commonRequest.setBody(userDTO);
                    String body = JsonUtil.toJson(commonRequest);
                    XHttp.post(FinalEnum.frontUrl + "/healthy/user/update2")
                            .upJson(body)
                            .syncRequest(false)
                            .onMainThread(true)
                            .execute(new CallBackProxy<CommonResponse<UserDTO>, UserDTO>(new TipProgressLoadingCallBack<UserDTO>(UserInfoNicknameFragment.this) {
                                @Override
                                public void onSuccess(UserDTO response) throws Throwable {
                                    XToastUtils.success("保存成功");
                                    userInfo.setNickname(nickname.getEditValue());
                                    userInfo.setVersion(userInfo.getVersion() + 1);
                                    TokenUtils.putUserInfo(userInfo);
                                    Intent intent = new Intent();
                                    intent.putExtra("nickname",nickname.getEditValue());
                                    setFragmentResult(RESULT_NICKNAME,intent);
                                    popToBack();
                                }
                            }){});
                } else {
                    XToastUtils.error("请检查格式是否正确");
                }
            }
        });
        return titleBar;
    }
}
