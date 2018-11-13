package cn.lds.im.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.lds.chat.R;
import cn.lds.chatcore.common.BitmapHelper;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.FileHelper;
import cn.lds.chatcore.common.GraphicHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.data.CarCheckModel;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.UploadModel;
import cn.lds.chatcore.event.FileUploadedEvent;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.manager.FileManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.chatcore.view.CameraActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.data.FaIlureModel;
import cn.lds.im.data.InspectModel;
import cn.lds.im.data.OrderInfoModel;
import cn.lds.im.data.TakeCarRequestModel;
import cn.lds.im.view.adapter.CarReportAdapter;
import cn.lds.im.view.widget.CustomPopuwindow;
import de.greenrobot.event.EventBus;

/**
 * 上报车况
 * Created by E0608 on 2017/12/22.
 */

public class CarReportConditionActivity extends BaseActivity {
    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    // 返回按钮
    @ViewInject(R.id.gr_gridview)
    protected GridView gridView;
    // 照相按钮
    @ViewInject(R.id.iv_take_photo)
    protected ImageView takePhoto;
    // 图片数量
    @ViewInject(R.id.tv_pic_num)
    protected TextView picNum;
    // 车况描述
    @ViewInject(R.id.et_add_remarks)
    protected EditText addRemarks;
    // 车况描述子数
    @ViewInject(R.id.tv_word_num)
    protected TextView wordNum;
    // 车况描述子数
    @ViewInject(R.id.submit)
    protected TextView submitBtn;
    // 车况检查
    @ViewInject(R.id.ll_car_check)
    protected LinearLayout ll_car_check;
    //控件绑定
    @ViewInject(R.id.inspect_exterior_rg)
    protected RadioGroup inspect_exterior_rg;
    @ViewInject(R.id.inspect_inside_rg)
    protected RadioGroup inspect_inside_rg;
    @ViewInject(R.id.inspect_tyre_rg)
    protected RadioGroup inspect_tyre_rg;
    @ViewInject(R.id.inspect_process_rg)
    protected RadioGroup inspect_process_rg;
    protected InspectModel inspectModel;

    protected static final int PHOTO_REQUEST_CUT = 384;// 结果

    protected static final int PHOTO_REQUEST_CAMERA = 1;// 拍照

    private int MAX_PIC = 4;
    private String imageFilePath;
    private File temps;

    private List<String> mPathList = new ArrayList<>();
    private List<String> mPicId = new ArrayList<>();
    private CarReportAdapter carReportAdapter;
    /**
     * 取车model
     */
    private OrderInfoModel takeModel;
    /**
     * 还车model
     */
    private OrderInfoModel returnModel;

    /**
     * 界面标识   ==TARKE_CAR 取车上报车况，否则还车上报车况
     */
    private String flag;
    private CustomPopuwindow popuwindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_condition);
        ViewUtils.inject(CarReportConditionActivity.class, this);
        init();
        getIntentData();

    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取上个页面传递数据（根据标识确定该界面是取车上报车况还是还车上报车况）
     */
    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            flag = bundle.getString("FLAG");
            if(flag != null && flag.equals("TARKE_CAR")){
                //取车页面
                takeModel = (OrderInfoModel) bundle.getSerializable("TAKEMODEL");
                submitBtn.setText("取电子钥匙");
                topTitle.setText("车况检查");
                ll_car_check.setVisibility(View.VISIBLE);
                //设置默认值
                inspectModel = new InspectModel();
                inspectModel.setPicList(null);
                inspectModel.setSanitaryState(true);
                inspectModel.setScratch(true);
                inspectModel.setWheelOk(true);
                //设置默认选择
                inspect_exterior_rg.check(R.id.inspect_exterior_yes_rb);
                inspect_inside_rg.check(R.id.inspect_inside_clean_rb);
                inspect_tyre_rg.check(R.id.inspect_tyre_yes_rb);
                inspect_process_rg.check(R.id.inspect_process_yes_rb);
            }else{
                //还车页面
                returnModel = (OrderInfoModel) bundle.getSerializable("RETURNMODEL");
                submitBtn.setText("立即还车");
                topTitle.setText("车况上报");
                ll_car_check.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.top_back_btn,
            R.id.inspect_confirm_tv,
            R.id.submit,
            R.id.iv_take_photo//返回
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn://返回
                finish();
                break;
            case R.id.iv_take_photo://照相
                // 判断存储卡是否可以用，可用进行存储
                startCustomCamerActivity();
                break;
            case R.id.submit:
                //TODO 上传四张车况图片，图片上传完成后，取车或还车接口
//                if(mPathList.size() < 4){
//                    ToolsHelper.showStatus(this,false,"请上传四张车况图片");
//                    return;
//                }
                if("TARKE_CAR".equals(flag)){
                    comitTarkCarData();
                }else if("RETURN_CAR".equals(flag)){
                    comitReturnCarData();
                }
                break;
        }
    }
    public void onEventMainThread(FileUploadedEvent event) {
        if (null == event)
            return;
        UploadModel uploadModel = GsonImplHelp.get().toObject(event.getResult(), UploadModel.class);
        String owner = event.getOwner();
        if (owner.equals(CacheHelper.getAccount() + "0")) {
            mPicId.add(uploadModel.getData().get(0).getNo());
        } else if (owner.equals(CacheHelper.getAccount() + "1")){
            mPicId.add(uploadModel.getData().get(0).getNo());
        } else if (owner.equals(CacheHelper.getAccount() + "2")) {
            mPicId.add(uploadModel.getData().get(0).getNo());
        } else if (owner.equals(CacheHelper.getAccount() + "3")) {
            mPicId.add(uploadModel.getData().get(0).getNo());
        }
        if(mPicId.size() == 4){
            subimTakeInfo();
        }
    }
    @OnRadioGroupCheckedChange({R.id.inspect_exterior_rg, R.id.inspect_inside_rg, R.id.inspect_tyre_rg})
    public void OnCheckedChangeListener(RadioGroup arg0, int arg1) {
        switch (arg0.getId()) {
            case R.id.inspect_exterior_rg:
                switch (arg1) {
                    case R.id.inspect_exterior_yes_rb:
                        inspectModel.setScratch(true);
                        break;
                    case R.id.inspect_exterior_no_rb:
                        inspectModel.setScratch(false);
                        break;
                }
                break;
            case R.id.inspect_inside_rg:
                switch (arg1) {
                    case R.id.inspect_inside_clean_rb:
                        inspectModel.setSanitaryState(true);
                        break;
                    case R.id.inspect_inside_dirty_rb:
                        inspectModel.setSanitaryState(false);
                        break;
                }
                break;
            case R.id.inspect_tyre_rg:
                switch (arg1) {
                    case R.id.inspect_tyre_yes_rb:
                        inspectModel.setWheelOk(true);
                        break;
                    case R.id.inspect_tyre_no_rb:
                        inspectModel.setWheelOk(false);
                        break;
                }
                break;
        }
    }

    private void subimTakeInfo() {
        //提交数据
        if("TARKE_CAR".equals(flag)){
            if (null == takeModel) {
                LoadingDialog.showDialog(mContext, getString(R.string.take_car_loading));
                ModuleHttpApi.getExistOrder();
                return;
            }
            TakeCarRequestModel requestData = new TakeCarRequestModel();
            requestData.setDescription(addRemarks.getText().toString().trim());
            requestData.setId(takeModel.getData().getId());
            requestData.setLocationId(takeModel.getData().getReservationLocationId());
            requestData.setPicList(mPicId);
            requestData.setSanitaryState(inspectModel.isSanitaryState());
            requestData.setScratch(inspectModel.isScratch());
            requestData.setWheelOk(inspectModel.isWheelOk());
            ModuleHttpApi.reservationOrdersPickup(takeModel,requestData);
        }else{
            TakeCarRequestModel requestData = new TakeCarRequestModel();
            requestData.setDescription(addRemarks.getText().toString().trim());
            requestData.setId(returnModel.getData().getId());
            requestData.setLocationId(returnModel.getData().getReservationLocationId());
            requestData.setPicList(mPicId);
            ModuleHttpApi.reservationOrdersReturn(returnModel.getData().getId(), requestData);
        }
    }

    /**
     * 提交还车数据
     */
    private void comitReturnCarData() {
        if (returnModel != null && returnModel.getData() != null) {
                showReturnCarPrompt();
        }
    }
    /**
     * 显示还车提示
     */
    private void showReturnCarPrompt() {
        PopWindowHelper.getInstance().confirm(mContext, new PopWindowListener() {
            @Override
            public void confirm() {
                if(mPicId.size() != MAX_PIC && mPathList.size() == MAX_PIC){
                    for (int i= 0; i < mPathList.size(); i++) {
                        //上传图片
                        FileManager.getInstance().uploadChatImage(mPathList.get(i), CacheHelper.getAccount() + i);
                    }
                }else{
                    subimTakeInfo();
                }
                popuwindow.checkCarStatus(CarReportConditionActivity.this,new View(CarReportConditionActivity.this));
            }

            @Override
            public void cancel() {

            }

            @Override
            public void onItemListener(int position) {

            }
        }).setContentTx("还车后您将无法继续使用此车辆，确认还车吗？").show(findViewById(R.id.top__iv));
    }
    /**
     * 提交取车数据
     */
    private void comitTarkCarData() {
          PopWindowHelper.getInstance().confirm(mContext, new PopWindowListener() {
                    @Override
                    public void confirm() {
                        if(mPicId.size() != MAX_PIC && mPathList.size() == MAX_PIC){
                            for (int i= 0; i < mPathList.size(); i++) {
                                //上传图片
                                FileManager.getInstance().uploadChatImage(mPathList.get(i), CacheHelper.getAccount() + i);
                            }
                        }else{
                            subimTakeInfo();
                        }
                        LoadingDialog.showDialog(CarReportConditionActivity.this,"请稍后...");
                    }

                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void onItemListener(int position) {

                    }
                }).setContentTx("取电子钥匙后开始收费").show(findViewById(R.id.top__iv));
    }
    public void onEventMainThread(HttpRequestEvent event) {
        LoadingDialog.dismissDialog();
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getExistOrder.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersPickup.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersReturn.equals(apiNo)
        )) {
            return;

        }
        if (!ToolsHelper.isNull(httpResult.getResult())) {

            switch (apiNo) {
                case ModuleHttpApiKey.reservationOrdersReturn:
                    final CarCheckModel checkModel = GsonImplHelp.get().toObject(httpResult.getResult(), CarCheckModel.class);
                    if(CarInUseActivity.instance != null){
                        CarInUseActivity.instance.finish();
                    }
                    if(checkModel != null
                            && checkModel.getData() != null ){
                        popuwindow.setChecked(
                                checkModel.getData().isAccSuccess(),
                                checkModel.getData().isChargeSuccess(),
                                checkModel.getData().isDoorLockedSuccess(),
                                checkModel.getData().isLightOffSuccess());
                    }

                    popuwindow.dismissCarCheckPopuWindow();
                    popuwindow.setDismissCarCheckPopuwindowListener(new CustomPopuwindow.DismissCarCheckPopuwindowListener() {
                        @Override
                        public void onDismissCarCheckEvent() {

                            if(checkModel != null
                                    && checkModel.getData() != null
                                    &&checkModel.getData().isAccSuccess()
                                    && checkModel.getData().isChargeSuccess()
                                    && checkModel.getData().isDoorLockedSuccess()
                                    && checkModel.getData().isLightOffSuccess()
                                    ){
                                Intent orderDetailIntent = new Intent(mContext, OrderDetailActivity.class);
                                startActivity(orderDetailIntent);
                                finish();
                            }
                        }
                    });

                    break;
                case ModuleHttpApiKey.reservationOrdersPickup:
                    if(TakeCarActivity.instance != null){
                        TakeCarActivity.instance.finish();
                    }
                    Intent carInUseintent = new Intent(mContext, CarInUseActivity.class);
                    startActivity(carInUseintent);
                    finish();
                    break;
            }
        }
    }
    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult httpResult = event.httpResult;
        String apiNo = httpResult.getApiNo();
        if (!(ModuleHttpApiKey.getExistOrder.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersPickup.equals(apiNo)
                || ModuleHttpApiKey.reservationOrdersReturn.equals(apiNo)

        )) {
            return;
        }
        LoadingDialog.dismissDialog();
        switch (apiNo) {
            case ModuleHttpApiKey.reservationOrdersReturn:
                FaIlureModel model = GsonImplHelp.get().toObject(httpResult.getResult(), FaIlureModel.class);
                if(model != null && model.getErrors() != null && model.getErrors().size() > 0){
                    if(model.getStatus().equals("failure")
                            && model.getErrors().get(0).getErrcode().equals("state.fetch.error")){
                        popuwindow.getStatusFailure();
                        popuwindow.dismissCarCheckPopuWindow();
                    }else{
                        //TODO 其它状态没想好怎么弄
                        popuwindow.nowDismissCarCheckPopuWindow();
                        ToolsHelper.showStatus(mContext,false,model.getErrors().get(0).getErrmsg());
                    }
                }
                break;
        }
    }
    private final int START_CUSTOM_CAMER = 0x00;
    /**
     * 启动连拍自定义相机
     */
    private void startCustomCamerActivity(){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent,START_CUSTOM_CAMER);
        this.overridePendingTransition(R.anim.slide_in_bottom,0);
    }

    /**
     * 启动系统相机
     */
    private void enterTakePhoto() {
        if (hasSdcard()) {
            imageFilePath = FileHelper.getTakeCarPath();
            Intent caremaIntent = GraphicHelper.getPhotoIntent(imageFilePath);
            startActivityForResult(caremaIntent, PHOTO_REQUEST_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_REQUEST_CAMERA:
                    if (hasSdcard()) {
                        String compressPath = BitmapHelper.compressPic(imageFilePath, 100);
                        mPathList.add(compressPath);
                        gridView.setVisibility(View.VISIBLE);
                        picNum.setText("("+ mPathList.size() + "/" + MAX_PIC +")");
                        reflushPicAdapter();
                    } else {
                        Toast.makeText(this,"未找到存储卡，无法存储照片！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PHOTO_REQUEST_CUT:
                    if (null == temps || ToolsHelper.isNull(temps.getPath())) {
                        ToolsHelper.showStatus(mContext, false, getString(R.string.profileactivity_getdate_failed));
                        return;
                    }
                    mPathList.add(temps.getPath());
                    gridView.setVisibility(View.VISIBLE);
                    picNum.setText("("+ mPathList.size() + "/" + MAX_PIC +")");
                    reflushPicAdapter();
                    break;
                case START_CUSTOM_CAMER:
                    Bundle bundle = data.getExtras();
                    if(bundle != null){
                        ArrayList<String> pathList = bundle.getStringArrayList("PATH_LIST");
                        if(pathList != null && pathList.size() == 4){
                            mPathList.clear();
                            mPathList.addAll(pathList);
                            gridView.setVisibility(View.VISIBLE);
                            picNum.setText("("+ mPathList.size() + "/" + MAX_PIC +")");
                            reflushPicAdapter();
                        }
                    }

            }
        }
    }


    private void reflushPicAdapter() {
        if(carReportAdapter == null){
            carReportAdapter = new CarReportAdapter(this,mPathList);
            gridView.setAdapter(carReportAdapter);
        }else{
            carReportAdapter.notifyDataSetChanged();
        }
        carReportAdapter.setOnDelPictureListener(new CarReportAdapter.OnDelPictureListener() {
            @Override
            public void onDelPictureEvent(int position) {
                mPathList.remove(position);
                carReportAdapter.notifyDataSetChanged();
                picNum.setText("("+ mPathList.size() + "/" + MAX_PIC +")");
            }
        });
    }

    /**
     * 判断是否存在sd卡
     * @return
     */
    protected boolean hasSdcard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            return true;
        else
            return false;
    }

    protected void init() {
        topTitle.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
        popuwindow = CustomPopuwindow.getInstance();
        addRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                wordNum.setText("(" + addRemarks.getText().length() + "/50)");
            }
        });
    }


}
