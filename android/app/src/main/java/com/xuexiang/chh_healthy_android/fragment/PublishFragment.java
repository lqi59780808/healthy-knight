package com.xuexiang.chh_healthy_android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.activity.MainActivity;
import com.xuexiang.chh_healthy_android.adapter.picture.ImageSelectGridAdapter;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipProgressLoadingCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonRequest;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.InvitationDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.InvitationPictureDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.UserDTO;
import com.xuexiang.chh_healthy_android.utils.SettingUtils;
import com.xuexiang.chh_healthy_android.utils.TokenUtils;
import com.xuexiang.chh_healthy_android.utils.Utils;
import com.xuexiang.chh_healthy_android.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xhttp2.callback.impl.IProgressResponseCallBack;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xutil.net.JsonUtil;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@Page(anim = CoreAnim.none)
public class PublishFragment extends BaseFragment implements ImageSelectGridAdapter.OnAddPicClickListener{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.et_title)
    MaterialEditText title;
    @BindView(R.id.mlet_text)
    MultiLineEditText text;

    private ImageSelectGridAdapter mAdapter;

    private List<LocalMedia> mSelectList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_publish;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorTitleBar));
        titleBar.setTitle("发表");
        titleBar.setLeftImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        titleBar.setActionTextColor(getResources().getColor(R.color.white));
        titleBar.addAction(new TitleBar.TextAction("确定") {
            @Override
            public void performAction(View view) {
                if (title.validate()) {
                    InvitationDTO invitationDTO = new InvitationDTO();
                    invitationDTO.setTitle(title.getEditValue());
                    invitationDTO.setContent(text.getContentText());
                    invitationDTO.setGood(0);
                    invitationDTO.setClick(0);
                    invitationDTO.setType(0);
                    invitationDTO.setCollect(0);
                    List<File> picList = new ArrayList<>();
                    if (!mSelectList.isEmpty()) {
                        for (LocalMedia lm:
                             mSelectList) {
                            picList.add(FileUtils.getFileByPath(lm.getCompressPath()));
                        }
                        //请求post
                        XHttp.post(FinalEnum.frontUrl + "/healthy/invitation/publish")
                                .params("title",invitationDTO.getTitle())
                                .params("content",invitationDTO.getContent())
                                .params("id",TokenUtils.getUserInfo().getId())
                                .uploadFiles("picture", picList, new IProgressResponseCallBack() {
                                    @Override
                                    public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {

                                    }
                                })
                                .syncRequest(false)
                                .onMainThread(true)
                                .execute(new CallBackProxy<CommonResponse<InvitationDTO>, InvitationDTO>(new TipProgressLoadingCallBack<InvitationDTO>(PublishFragment.this) {
                                    @Override
                                    public void onSuccess(InvitationDTO response) throws Throwable {
                                        XToastUtils.success("发布成功");
                                        ActivityUtils.startActivity(MainActivity.class);
                                    }
                                }){});
                    } else {
                        //请求post
                        XHttp.post(FinalEnum.frontUrl + "/healthy/invitation/publish2")
                                .params("title",invitationDTO.getTitle())
                                .params("content",invitationDTO.getContent())
                                .params("id",TokenUtils.getUserInfo().getId())
                                .syncRequest(false)
                                .onMainThread(true)
                                .execute(new CallBackProxy<CommonResponse<InvitationDTO>, InvitationDTO>(new TipProgressLoadingCallBack<InvitationDTO>(PublishFragment.this) {
                                    @Override
                                    public void onSuccess(InvitationDTO response) throws Throwable {
                                        XToastUtils.success("发布成功");
                                        ActivityUtils.startActivity(MainActivity.class);
                                    }
                                }){});
                    }

                } else {
                    XToastUtils.error("请检查格式是否正确");
                }
            }
        });
        return titleBar;
    }

    @Override
    protected void initViews() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter = new ImageSelectGridAdapter(getActivity(), this));
        mAdapter.setSelectList(mSelectList);
        mAdapter.setSelectMax(8);
        mAdapter.setOnItemClickListener((position, v) -> PictureSelector.create(this).themeStyle(R.style.XUIPictureStyle).openExternalPreview(position, mSelectList));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    mSelectList = PictureSelector.obtainMultipleResult(data);
                    mAdapter.setSelectList(mSelectList);
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onAddPicClick() {
        Utils.getPictureSelector(this)
                .selectionMedia(mSelectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
}

