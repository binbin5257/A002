package cn.lds.im.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Date;

import cn.lds.chat.R;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.pickerview.TimePickerView;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.ApproverModel;
import cn.lds.im.data.FaIlureModel;
import de.greenrobot.event.EventBus;

/**
 * 申请详情页面
 * Created by E0608 on 2017/12/26.
 */

public class ApplyDetailActivity extends BaseActivity{

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    // 标题栏提交
    @ViewInject(R.id.top_right_tv)
    private TextView topMenuRight;
    // 情况描述
    @ViewInject(R.id.et_des)
    private EditText desEt;
    // 审批人姓名
    @ViewInject(R.id.tv_approver)
    private TextView approverName;
    // 审批人电话
    @ViewInject(R.id.tv_approver_phone)
    private TextView approverPhone;
    // 用车时间
    @ViewInject(R.id.choice_user_car_time)
    private EditText useCarTimeEt;

    // 用车开始时间
    @ViewInject(R.id.choice_user_car_start_time)
    private EditText useCarStartTimeEt;

    // 用车结束时间
    @ViewInject(R.id.choice_user_car_end_time)
    private EditText useCarEndTimeEt;
    private TimePickerView pickerViewDay;
    private TimePickerView pickerViewStart;
    private TimePickerView pickerViewEnd;

    /**
     * 用车时间
     */
    private long time;
    private String approverPhoneStr;
    private long useCarstartTimeLong = 0;
    private long lastUseCarstartTimeLong = 0;
    private long useCarendTimeLong = 0;
    private long lastUseCarendTimeLong = 0;

    private int SELECT_USE_CAR_DAY = 1;
    private int SELECT_START_USE_CAR_TIME = 2;
    private int SELECT_END_USE_CAR_TIME = 3;
    private int SELECT_TIME_TYPE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_detail);
        ViewUtils.inject(ApplyDetailActivity.class, this);
        EventBus.getDefault().register(this);
        init();
        ModuleHttpApi.getApprover();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    @OnClick({R.id.top_back_btn,//返回
            R.id.top_right_tv,
            R.id.tv_approver_phone,
            R.id.choice_user_car_time,
            R.id.choice_user_car_start_time,
            R.id.choice_user_car_end_time,
            R.id.rl_choice_use_car_start_time,
            R.id.rl_choice_use_car_end_time,
            R.id.rl_choice_use_car_time
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn://返回
                finish();
                break;
            case  R.id.top_right_tv: //提交申请
                submitApply();
                break;
            case  R.id.rl_choice_use_car_time: //弹出时间选择器
            case  R.id.choice_user_car_time: //弹出时间选择器

                SELECT_TIME_TYPE = SELECT_USE_CAR_DAY;
                pickerViewDay.show();
                break;
            case  R.id.rl_choice_use_car_start_time: //用车开始时间
            case  R.id.choice_user_car_start_time:
                if(TextUtils.isEmpty(useCarTimeEt.getText().toString())){
                    ToolsHelper.showStatus(mContext,false,"请选择用车日期");
                    return;
                }
                SELECT_TIME_TYPE = SELECT_START_USE_CAR_TIME;
                pickerViewStart.show();
                break;
            case  R.id.rl_choice_use_car_end_time: //用车结束时间
            case  R.id.choice_user_car_end_time:
                if(TextUtils.isEmpty(useCarTimeEt.getText().toString())){
                    ToolsHelper.showStatus(mContext,false,"请选择用车日期");
                    return;
                }else if(TextUtils.isEmpty(useCarStartTimeEt.getText().toString())){
                    ToolsHelper.showStatus(mContext,false,"请选择用车开始时间");
                    return;
                }
                SELECT_TIME_TYPE = SELECT_END_USE_CAR_TIME;
                pickerViewEnd.show();
                break;
            case  R.id.tv_approver_phone: //给审批人打电话
                if(!TextUtils.isEmpty(approverPhoneStr)){
                    PopWindowHelper.getInstance().openTel(this, approverPhoneStr).show(findViewById(R.id.top__iv));
                }

                break;
        }

    }
    private void showConfirmDialog(String content) {
        PopWindowHelper.getInstance().alert(mContext, new PopWindowListener() {
            @Override
            public void confirm() {
                if(SELECT_TIME_TYPE == SELECT_USE_CAR_DAY){
                    pickerViewDay.show();
                }else if(SELECT_TIME_TYPE == SELECT_START_USE_CAR_TIME){
                    pickerViewStart.show();
                }else{
                    pickerViewEnd.show();
                }

            }

            @Override
            public void cancel() {

            }

            @Override
            public void onItemListener(int position) {

            }
        }).setContentTx(content).setConfirmTx("确认").show(findViewById(R.id.top__iv));
    }

    /**
     * 提交用车申请
     */
    private void submitApply() {
        String descStr = desEt.getText().toString().trim();
        String useCarTimeStr = useCarTimeEt.getText().toString().trim();
        String useCarStartTimeStr = useCarStartTimeEt.getText().toString().trim();
        String useCarEndTimeStr = useCarEndTimeEt.getText().toString().trim();
        if(TextUtils.isEmpty(useCarTimeStr)){
            ToolsHelper.showStatus(this,false,"请选择用车日期");
            return;
        }
        if(TextUtils.isEmpty(useCarStartTimeStr)){
            ToolsHelper.showStatus(mContext,false,"请选择用车开始时间");
            return;
        }
        if(TextUtils.isEmpty(useCarEndTimeStr)){
            ToolsHelper.showStatus(mContext,false,"请选择用车结束时间");
            return;
        }
        if(TextUtils.isEmpty(descStr)){
            ToolsHelper.showStatus(this,false,"请填写申请说明");
            return;
        }
        LoadingDialog.showDialog(this,"请稍后...");
        ModuleHttpApi.submitApplyForUserCar(time,descStr,lastUseCarstartTimeLong,lastUseCarendTimeLong);

    }


    private void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText("用车申请");
        btnBack.setVisibility(View.VISIBLE);
        topMenuRight.setVisibility(View.VISIBLE);
        topMenuRight.setText("提交");
        topMenuRight.setTextColor(getResources().getColor(R.color.important_color_blue));
        final long currentTimeMillis = System.currentTimeMillis();
        useCarTimeEt.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT11, currentTimeMillis).trim());
        //设置用车开始和结束时间为当前时间半个小时之后
        useCarstartTimeLong = currentTimeMillis;
        useCarendTimeLong = currentTimeMillis + 60 * 60 * 1000;
        useCarStartTimeEt.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT2, useCarstartTimeLong).trim());
        useCarEndTimeEt.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT2, useCarendTimeLong).trim());

        pickerViewStart = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pickerViewStart.setTitle("选择用车开始时间");
        pickerViewStart.setTime(new Date(useCarstartTimeLong));
        lastUseCarstartTimeLong = useCarstartTimeLong;
        lastUseCarendTimeLong = useCarendTimeLong;
        time = currentTimeMillis;
        pickerViewStart.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                useCarstartTimeLong = getUseCarTime(date);
                if(useCarstartTimeLong + 60 * 1000 < System.currentTimeMillis()){
                    showConfirmDialog("请选择当前时间或之后的时间");
                }else if(!TextUtils.isEmpty(useCarEndTimeEt.getText().toString().trim()) && useCarendTimeLong < useCarstartTimeLong){
                    showConfirmDialog("开始时间不能大于结束时间");
                }else{
                    lastUseCarstartTimeLong = useCarstartTimeLong;
                    useCarStartTimeEt.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT2, date.getTime()).trim());
                }

            }
        });
        pickerViewEnd = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pickerViewEnd.setTitle("选择用车结束时间");
        pickerViewEnd.setTime(new Date(useCarendTimeLong));
        pickerViewEnd.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                useCarendTimeLong = getUseCarTime(date);
                if(!TextUtils.isEmpty(useCarStartTimeEt.getText().toString().trim())){
                    if(useCarstartTimeLong > useCarendTimeLong){
                        showConfirmDialog("结束时间不能小于开始时间");
                    }else{
                        lastUseCarendTimeLong = useCarendTimeLong;
                        useCarEndTimeEt.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT2, date.getTime()).trim());
                    }
                }

            }
        });
        pickerViewDay = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        pickerViewDay.setTitle("选择用车日期");
        pickerViewDay.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {



            @Override
            public void onTimeSelect(Date date) {

                if(System.currentTimeMillis() - date.getTime() > 24 * 60 * 60 * 1000){
                    showConfirmDialog("请选择当前日期或当前日之后日期");
                }else{
                    time = date.getTime();
                    useCarTimeEt.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT11, time).trim());
                }

            }
        });
    }

    /**
     * 将用车日和用车时间拼接成毫秒值
     */
    private long getUseCarTime(Date date){
        String useDay = useCarTimeEt.getText().toString().trim();
        //户选择时间 HH:mm
        String hhmm = TimeHelper.getTimeFromMillis(TimeHelper.FORMAT2, date.getTime()).trim();
        //拼接成 yyyy-MM-dd HH:mm
        String dayHhmm = useDay +" "+ hhmm;
        return TimeHelper.getTime(TimeHelper.FORMAT6, dayHhmm);
    }

    public void onEventMainThread(HttpRequestEvent event) throws Exception {
        LoadingDialog.dismissDialog();
        // 获取API请求返回值
        HttpResult httpResult = event.getResult();
        // API区分
        String apiNo = httpResult.getApiNo();

        if (!(ModuleHttpApiKey.submitApplyForUserCar.equals(apiNo)
                || ModuleHttpApiKey.getApprover.equals(apiNo)
                || ModuleHttpApiKey.cancelApply.equals(apiNo)
           ))
            return;
        switch (apiNo){
            case ModuleHttpApiKey.submitApplyForUserCar:
                ToolsHelper.showStatus(this,true,"申请成功");
                finish();
                break;
            case ModuleHttpApiKey.cancelApply:
                ToolsHelper.showStatus(this,true,"撤销成功");
                finish();
                break;
            case ModuleHttpApiKey.getApprover:
                ApproverModel model = GsonImplHelp.get().toObject(httpResult.getResult(), ApproverModel.class);
                if(model != null && model.getData() != null){
                    String approverName = model.getData().getApproverName();
                    approverPhoneStr = model.getData().getApproverPhone();
                    String approverId = String.valueOf(model.getData().getApproverId());
                    CacheHelper.setApproverId(approverId);
                    CacheHelper.setApproverName(approverName);
                    CacheHelper.setApproverPhone(approverPhoneStr);
                    this.approverName.setText(model.getData().getApproverName());
                    this.approverPhone.setText(model.getData().getApproverPhone());
                }


                break;
        }

    }
    public void onEventMainThread(HttpRequestErrorEvent event) {
        LoadingDialog.dismissDialog();
        try {
            // 获取API请求返回值
            HttpResult httpResult = event.getResult();
            // API区分
            String apiNo = httpResult.getApiNo();
            if (!(ModuleHttpApiKey.submitApplyForUserCar.equals(apiNo)
                    || ModuleHttpApiKey.getApprover.equals(apiNo)
            ))
                return;
            FaIlureModel model = GsonImplHelp.get().toObject(httpResult.getResult(), FaIlureModel.class);
            switch (apiNo){
                case ModuleHttpApiKey.submitApplyForUserCar:
                    ToolsHelper.showStatus(this,false,model.getErrors().get(0).getErrmsg());
                    break;
                case ModuleHttpApiKey.getApprover:
                    PopWindowHelper.getInstance().alert(this, new PopWindowListener() {
                        @Override
                        public void confirm() {
                            finish();
                        }

                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void onItemListener(int position) {

                        }
                    }).setConfirmTx("好的")

                            .setContentTx("您当前所在部门还没有创建用车申请审批人，请联系平台管理员").show(findViewById(R.id.top__iv));

                    break;
            }

        } catch (Exception ex) {
            LogHelper.e(this.getClass().getSimpleName(), ex);
        }
    }
}
