package cn.lds.im.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.lds.chat.R;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.TimeHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.manager.FileManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.ApplyListModel;
import cn.lds.im.data.DepartmentCarsModel;
import cn.lds.im.data.FaIlureModel;
import cn.lds.im.data.MarkingDetailModel;
import de.greenrobot.event.EventBus;

/**
 * 用车审批详情
 * Created by E0608 on 2017/12/26.
 */

public class MarkingDetaiAcitivity extends BaseActivity{
    
    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    // 情况描述
    @ViewInject(R.id.tv_des)
    private TextView descTv;
    // 审批人姓名
    @ViewInject(R.id.tv_approver)
    private TextView approverName;
    // 审批人电话
    @ViewInject(R.id.tv_approver_phone)
    private TextView approverPhone;
    // 用车时间
    @ViewInject(R.id.choice_user_car_time)
    private TextView useCarTimeTv;
    // 通过按钮
    @ViewInject(R.id.btn_agree)
    private Button passBtn;
    // 拒绝按钮
    @ViewInject(R.id.btn_refruse)
    private Button refuseBtn;
    // 审批状态
    @ViewInject(R.id.tv_status)
    private TextView statusTv;
    // 审批状态
    @ViewInject(R.id.tv_apply)
    private TextView applyTv;
    // 审批时间布局
    @ViewInject(R.id.rl_marking_time)
    private RelativeLayout markingTimeRlyt;
    // 审批时间布局
    @ViewInject(R.id.tv_marking_time)
    private TextView markingTimeTv;
    // 标题栏提交
    @ViewInject(R.id.top_right_tv)
    private TextView topMenuRight;
    // 申请人电话
    @ViewInject(R.id.tv_apply_phone)
    private TextView applyPhoneTv;

    // 用车开始时间
    @ViewInject(R.id.choice_user_car_start_time)
    private TextView useCarStartTimeTv;
    // 用车开始时间
    @ViewInject(R.id.tv_car)
    private EditText carTv;
    // 分配车靓图标
    @ViewInject(R.id.iv_car)
    private ImageView carRightRowIv;
    // 分配车辆条目
    @ViewInject(R.id.rl_allot_car)
    private RelativeLayout allotCarRlyt;

    // 审批人布局
    @ViewInject(R.id.rl_marking_person)
    private RelativeLayout markingPersionRlyt;
    // 审批人电话布局
    @ViewInject(R.id.rl_marking_person_phone)
    private RelativeLayout markingPersonPhoneRlyt;

    // 用车结束时间
    @ViewInject(R.id.choice_user_car_end_time)
    private TextView useCarEndTimeTv;
    private String id;
    private String flag;
    private String approverPhoneStr;
    private DepartmentCarsModel.DataBean carBean;
    private String carNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marking_detail);
        ViewUtils.inject(MarkingDetaiAcitivity.class, this);
        init();
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        LoadingDialog.showDialog(this,"请稍后...");
        ModuleHttpApi.getMarkingDetail(id);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    private void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText("用车审批");
        btnBack.setVisibility(View.VISIBLE);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            id = bundle.getString("ID");
            flag = bundle.getString("FLAG");
            if(flag != null && flag.equals("APPLY")){
                topTitle.setText("用车申请");
                carRightRowIv.setVisibility(View.INVISIBLE);
                allotCarRlyt.setClickable(false);
                allotCarRlyt.setEnabled(false);
            }else{
                topTitle.setText("用车审批");
                carRightRowIv.setVisibility(View.VISIBLE);
                allotCarRlyt.setClickable(true);
                allotCarRlyt.setEnabled(true);
            }
        }

    }

    @OnClick({R.id.top_back_btn,//返回
            R.id.btn_agree,
            R.id.top_right_tv,
            R.id.tv_approver_phone,
            R.id.rl_allot_car,
            R.id.btn_refruse
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn://返回
                finish();
                break;
            case R.id.btn_agree://审批通过
                if(!TextUtils.isEmpty(carTv.getText().toString())){
                    LoadingDialog.showDialog(this,"请稍后...");
                    ModuleHttpApi.markingPass(id,carNo);

                }else{
                    ToolsHelper.showInfo(this,"请指定车辆");
                }

                break;
            case R.id.btn_refruse://审批拒绝
                LoadingDialog.showDialog(this,"请稍后...");
                ModuleHttpApi.markingRefuse(id);
                break;
            case  R.id.top_right_tv: //撤销申请
                cancelApply();
                break;
            case  R.id.tv_approver_phone: //拨打审批人电话
                if(!TextUtils.isEmpty(approverPhoneStr)){
                    PopWindowHelper.getInstance().openTel(this, approverPhoneStr).show(findViewById(R.id.top__iv));
                }

                break;
            case  R.id.rl_allot_car: //撤销申请
                Intent intent = new Intent(this,AllotCarActivity.class);
                startActivityForResult(intent,1);
                break;
        }

    }

    /**
     * 撤销用车申请
     */
    private void cancelApply() {
        PopWindowHelper.getInstance().confirm(mContext, new PopWindowListener() {
            @Override
            public void confirm() {
                ModuleHttpApi.cancelApply(id);
            }

            @Override
            public void cancel() {

            }

            @Override
            public void onItemListener(int position) {

            }
        }).setContentTx("确认撤销用车申请？").show(findViewById(R.id.top__iv));

    }

    public void onEventMainThread(HttpRequestEvent event) {
        LoadingDialog.dismissDialog();
        try {
            // 获取API请求返回值
            HttpResult httpResult = event.getResult();
            // API区分
            String apiNo = httpResult.getApiNo();

            if (!(ModuleHttpApiKey.getMarkingDetail.equals(apiNo)
                 || ModuleHttpApiKey.markingRefuse.equals(apiNo)
                 || ModuleHttpApiKey.markingPass.equals(apiNo)
                 || ModuleHttpApiKey.cancelApply.equals(apiNo)
            ))
                return;
            switch (apiNo){
                case ModuleHttpApiKey.getMarkingDetail:
                    MarkingDetailModel model = GsonImplHelp.get().toObject(httpResult.getResult(), MarkingDetailModel.class);
                    processData(model);
                    break;
                case ModuleHttpApiKey.markingRefuse:
                    ToolsHelper.showStatus(this,true,"申请已拒绝");
                    finish();
                    break;
                case ModuleHttpApiKey.markingPass:
                    ToolsHelper.showStatus(this,true,"申请已通过");
                    finish();
                    break;
                case ModuleHttpApiKey.cancelApply:
                    ToolsHelper.showStatus(this,true,"申请已撤销");
                    finish();
                    break;
            }

        }catch (Exception e){

        }
    }

    /**
     * 解析审批详情
     */
    private void processData(MarkingDetailModel model) {
        if(model != null){
            MarkingDetailModel.DataBean dataBean = model.getData();
            if(dataBean != null){

                if(dataBean.getStatus().equals("APPOVING")){
                    if(flag != null && flag.equals("APPLY")){
                        passBtn.setVisibility(View.GONE);
                        refuseBtn.setVisibility(View.GONE);
                        topMenuRight.setVisibility(View.VISIBLE);
                        topMenuRight.setText("撤销");
                        topMenuRight.setTextColor(getResources().getColor(R.color.important_color_blue));
                        allotCarRlyt.setVisibility(View.GONE);
                    }else{
                        allotCarRlyt.setVisibility(View.VISIBLE);
                        passBtn.setVisibility(View.VISIBLE);
                        refuseBtn.setVisibility(View.VISIBLE);
                    }
                    statusTv.setText("待审批");
                    statusTv.setTextColor(mContext.getResources().getColor(R.color.important_color_blue));
                    markingTimeRlyt.setVisibility(View.GONE);
                    markingPersionRlyt.setVisibility(View.GONE);
                    markingPersonPhoneRlyt.setVisibility(View.GONE);
                }else if(dataBean.getStatus().equals("APPROVED")){
                    topMenuRight.setVisibility(View.GONE);
                    passBtn.setVisibility(View.GONE);
                    refuseBtn.setVisibility(View.GONE);
                    statusTv.setText("已批准");
                    statusTv.setTextColor(mContext.getResources().getColor(R.color.textcolor80));
                    markingTimeRlyt.setVisibility(View.VISIBLE);
                    markingTimeTv.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT5, Long.parseLong(dataBean.getApproveTime())));
                    carTv.setText(dataBean.getExpectedPlateLicenseNo());
                    markingTimeRlyt.setVisibility(View.VISIBLE);
                    markingPersionRlyt.setVisibility(View.VISIBLE);
                    markingPersonPhoneRlyt.setVisibility(View.VISIBLE);
                }else if(dataBean.getStatus().equals("REJECTED")){
                    topMenuRight.setVisibility(View.GONE);
                    passBtn.setVisibility(View.GONE);
                    refuseBtn.setVisibility(View.GONE);
                    statusTv.setText("已驳回");
                    statusTv.setTextColor(mContext.getResources().getColor(R.color.red));
                    markingTimeRlyt.setVisibility(View.VISIBLE);
                    markingTimeTv.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT5, Long.parseLong(dataBean.getApproveTime())));
                    markingTimeRlyt.setVisibility(View.VISIBLE);
                    markingPersionRlyt.setVisibility(View.VISIBLE);
                    markingPersonPhoneRlyt.setVisibility(View.VISIBLE);
                }else if(dataBean.getStatus().equals("WITHDRAW")){
                    topMenuRight.setVisibility(View.GONE);
                    passBtn.setVisibility(View.GONE);
                    refuseBtn.setVisibility(View.GONE);
                    statusTv.setText("已撤销");
                    statusTv.setTextColor(mContext.getResources().getColor(R.color.red));
                    markingTimeRlyt.setVisibility(View.GONE);
                    markingPersionRlyt.setVisibility(View.GONE);
                    markingPersonPhoneRlyt.setVisibility(View.GONE);
                }
                else if(dataBean.getStatus().equals("USED")){
                    topMenuRight.setVisibility(View.GONE);
                    passBtn.setVisibility(View.GONE);
                    refuseBtn.setVisibility(View.GONE);
                    statusTv.setText("已使用");
                    statusTv.setTextColor(mContext.getResources().getColor(R.color.textcolor80));
                    markingTimeTv.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT5, Long.parseLong(dataBean.getApproveTime())));
                    markingTimeRlyt.setVisibility(View.VISIBLE);
                    markingPersionRlyt.setVisibility(View.VISIBLE);
                    markingPersonPhoneRlyt.setVisibility(View.VISIBLE);
                }
                approverPhoneStr = dataBean.getApproverPhone();
                descTv.setText(dataBean.getDescription());
                applyTv.setText(dataBean.getName());
                approverName.setText(dataBean.getApproverName());
                this.approverPhone.setText(dataBean.getApproverPhone());
                applyPhoneTv.setText(dataBean.getPhone());
                useCarTimeTv.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT11, Long.parseLong(dataBean.getUserDate())));
                useCarStartTimeTv.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT2, Long.parseLong(dataBean.getStartTime())));
                useCarEndTimeTv.setText(TimeHelper.getTimeFromMillis(TimeHelper.FORMAT2, Long.parseLong(dataBean.getEndTime())));
                if(allotCarRlyt.getVisibility() != View.GONE){
                    if(dataBean.getStatus().equals("APPOVING")){
                        carRightRowIv.setVisibility(View.VISIBLE);
                        allotCarRlyt.setClickable(true);
                        allotCarRlyt.setEnabled(true);
                    }else{
                        carRightRowIv.setVisibility(View.INVISIBLE);
                        allotCarRlyt.setClickable(false);
                        allotCarRlyt.setEnabled(false);
                    }

                }
            }


        }
    }

    public void onEventMainThread(HttpRequestErrorEvent event) {
        LoadingDialog.dismissDialog();
        try {
            // 获取API请求返回值
            HttpResult httpResult = event.getResult();
            // API区分
            String apiNo = httpResult.getApiNo();
            if (!(ModuleHttpApiKey.getMarkingDetail.equals(apiNo)
                    || ModuleHttpApiKey.markingRefuse.equals(apiNo)
                    || ModuleHttpApiKey.markingPass.equals(apiNo)
            ))
                return;
            FaIlureModel model = GsonImplHelp.get().toObject(httpResult.getResult(), FaIlureModel.class);
            ToolsHelper.showStatus(this,false,model.getErrors().get(0).getErrmsg());


        } catch (Exception ex) {
            LogHelper.e(this.getClass().getSimpleName(), ex);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && RESULT_OK == resultCode){
            carNo = data.getStringExtra("ALLOT_CAR_NO");
            carTv.setText(carNo);
        }
    }
}
