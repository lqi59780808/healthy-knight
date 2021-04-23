package com.xuexiang.chh_healthy_android.fragment.step;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.luck.picture.lib.entity.LocalMedia;
import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.activity.MainActivity;
import com.xuexiang.chh_healthy_android.core.BaseFragment;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipProgressLoadingCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.InvitationDTO;
import com.xuexiang.chh_healthy_android.fragment.PublishFragment;
import com.xuexiang.chh_healthy_android.utils.MMKVUtils;
import com.xuexiang.chh_healthy_android.utils.TokenUtils;
import com.xuexiang.chh_healthy_android.utils.XToastUtils;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xhttp2.callback.impl.IProgressResponseCallBack;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.file.FileUtils;

import butterknife.BindView;

@Page(anim = CoreAnim.none)
public class SetPlanFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.tv_step_number)
    MaterialEditText tv_step_number;
    @BindView(R.id.cb_remind)
    CheckBox cb_remind;
    @BindView(R.id.tv_remind_time)
    TextView tv_remind_time;
    private String walk_qty;
    private String remind;
    private String achieveTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorTitleBar));
        titleBar.setTitle("锻炼计划");
        titleBar.setLeftImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        titleBar.setActionTextColor(getResources().getColor(R.color.white));
        titleBar.addAction(new TitleBar.TextAction("保存") {
            @Override
            public void performAction(View view) {
                save();
            }
        });
        return titleBar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ac_exercise_plan;
    }

    @Override
    protected void initViews() {
        String planWalk_QTY = MMKVUtils.getString("planWalk_QTY", "7000");
        String remind = MMKVUtils.getString("remind", "1");
        String achieveTime = MMKVUtils.getString("achieveTime", "20:00");
        if (!planWalk_QTY.isEmpty()) {
            if ("0".equals(planWalk_QTY)) {
                tv_step_number.setText("7000");
            } else {
                tv_step_number.setText(planWalk_QTY);
            }
        }
        if (!remind.isEmpty()) {
            if ("0".equals(remind)) {
                cb_remind.setChecked(false);
            } else if ("1".equals(remind)) {
                cb_remind.setChecked(true);
            }
        }

        if (!achieveTime.isEmpty()) {
            tv_remind_time.setText(achieveTime);
        }

    }


    @Override
    protected void initListeners() {
        tv_remind_time.setOnClickListener(this);
        super.initListeners();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_remind_time:
                showTimeDialog1();
                break;
            default:
                break;
        }
    }

    private void save() {
        Intent result = new Intent();
        Bundle bundle = new Bundle();
        walk_qty = tv_step_number.getEditValue();
//        remind = "";
        if (cb_remind.isChecked()) {
            remind = "1";
        } else {
            remind = "0";
        }
        achieveTime = tv_remind_time.getText().toString().trim();
        if (walk_qty.isEmpty() || "0".equals(walk_qty)) {
            MMKVUtils.put("planWalk_QTY", "7000");
            bundle.putString("planWalk_QTY", "7000");
        } else {
            MMKVUtils.put("planWalk_QTY", walk_qty);
            bundle.putString("planWalk_QTY", walk_qty);
        }
        MMKVUtils.put("remind", remind);
        bundle.putString("remind", remind);
        if (achieveTime.isEmpty()) {
            MMKVUtils.put("achieveTime", "21:00");
            bundle.putString("achieveTime", "21:00");
            this.achieveTime = "21:00";
        } else {
            MMKVUtils.put("achieveTime", achieveTime);
            bundle.putString("achieveTime", achieveTime);
        }
        result.putExtras(bundle);
        setFragmentResult(101,result);
        popToBack();
    }

    private void showTimeDialog1() {
        final Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
//        String time = tv_remind_time.getText().toString().trim();
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
                tv_remind_time.setText(df.format(date));
            }
        }, hour, minute, true).show();
    }
}

