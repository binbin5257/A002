package cn.lds.im.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.lds.chat.R;
import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.BitmapHelper;
import cn.lds.chatcore.common.Constants;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.DbHelper;
import cn.lds.chatcore.common.FileHelper;
import cn.lds.chatcore.common.GraphicHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.WebViewActivityHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.PersonInfo;
import cn.lds.chatcore.data.UploadModel;
import cn.lds.chatcore.db.AccountsTable;
import cn.lds.chatcore.event.AccountAvaliableEvent;
import cn.lds.chatcore.event.FileUploadedEvent;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.manager.FileManager;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.view.widget.CustomPopuwindow;
import de.greenrobot.event.EventBus;
/**
 * 身份认证界面
 * Created by E0608 on 2017/11/9.
 */
public class AuthenticationActivity extends BaseActivity {
    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    // 帮助
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView mHelp;
    // 姓名
    @ViewInject(R.id.name)
    protected TextView name;
    // 身份证号
    @ViewInject(R.id.id_no)
    protected TextView idNo;
    // 驾驶证档案编号
    @ViewInject(R.id.driver_no)
    protected TextView driverNo;
    // 身份证审核状态
    @ViewInject(R.id.id_status)
    protected ImageView idStatus;
    // 身份证审核拒绝理由
    @ViewInject(R.id.id_refruse_reson)
    protected TextView idRefrusedReson;
    // 身份证正面照片
    @ViewInject(R.id.iv_identify_front)
    protected ImageView idFront;
    // 身份证正面照片相机图标
    @ViewInject(R.id.iv_identify_front_icon)
    protected ImageView idFrontIcon;
    // 身份证反面照片
    @ViewInject(R.id.iv_identify_back)
    protected ImageView idBack;
    // 身份证反面照片相机图标
    @ViewInject(R.id.iv_identify_back_icon)
    protected ImageView idBackIcon;
    // 驾驶证审核状态
    @ViewInject(R.id.driver_status)
    protected ImageView driverStatus;
    // 驾驶证审核拒绝理由
    @ViewInject(R.id.driver_refruse_reason)
    protected TextView driverRefruseReason;
    // 驾驶证正面照片
    @ViewInject(R.id.iv_dirver_front)
    protected ImageView dirverFront;
    // 驾驶证正面照片相机图标
    @ViewInject(R.id.iv_dirver_front_icon)
    protected ImageView dirverFrontIcon;
    // 驾驶证反面照片
    @ViewInject(R.id.iv_dirver_back)
    protected ImageView dirverBack;
    // 驾驶证反面照片相机图标
    @ViewInject(R.id.iv_dirver_back_icon)
    protected ImageView dirverBackIcon;
    // 认证提交按钮
    @ViewInject(R.id.submit)
    protected TextView submit;
    // 认证提交按钮
    @ViewInject(R.id.main_view)
    protected LinearLayout mainView;

    private int pic_res = -1;
    private final int id_1 = 0;
    private final int id_2 = 1;
    private final int dirver_3 = 3;
    private final int dirver_4 = 4;

    protected String imageFilePath;// 图片路径
    protected static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    protected static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    protected static final int PHOTO_REQUEST_CUT = 384;// 结果
    private File temps;
    protected boolean flag_f = false;
    protected String idCard = null, idCardBack = null, driverCard = null, driverCardBack = null;
    protected File temps1 = null, temps2 = null, temps3 = null, temps4 = null;
    /**
     * 审核状态
     */
    private String reviewType;
    private AccountsTable accountsTable;
    private String avatarFileTableId1;
    private String avatarFileTableId2;
    private String avatarFileTableId3;
    private String avatarFileTableId4;
    /**
     * 拒绝理由
     */
    private String refuseReason;
    private List<String> idRefrushReasonList;
    private List<String> driverRefrushReasonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        EventBus.getDefault().register(this);
        ViewUtils.inject(AuthenticationActivity.class, this);
        init();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
    /**
     * 页面点击事件
     *
     * @param view
     */

    @OnClick({R.id.top_back_btn
            , R.id.top_menu_btn_del
            , R.id.submit
            , R.id.iv_identify_front_icon
            , R.id.iv_identify_back_icon
            , R.id.iv_dirver_front_icon
            , R.id.iv_dirver_back_icon
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn: //返回上一个界面
                finish();
                break;
            case R.id.top_menu_btn_del: //进入帮助页面
                WebViewActivityHelper.startWebViewActivity(mContext, MyApplication.getInstance().getSERVER_HOST() + "/static/authenticationHelp.html", getString(R.string.identifying_help_detail));
                break;
            case R.id.iv_identify_front_icon: //身份证正面照片
                pic_res = id_1;
                getPhotoPop();
                break;
            case R.id.iv_identify_back_icon: //身份证反面照片
                pic_res = id_2;
                getPhotoPop();
                break;
            case R.id.iv_dirver_front_icon: //驾驶证正面照片
                pic_res = dirver_3;
                getPhotoPop();
                break;
            case R.id.iv_dirver_back_icon: //驾驶证饭面照片
                pic_res = dirver_4;
                getPhotoPop();
                break;
            case R.id.submit: //认证界面按钮
                submitClick();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }
        } else if (requestCode == PHOTO_REQUEST_CAMERA && resultCode == RESULT_OK) {
            if (hasSdcard()) {
                File tempFile = new File(imageFilePath);
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(this,"未找到存储卡，无法存储照片！",Toast.LENGTH_SHORT).show();
            }

        }else if (requestCode == PHOTO_REQUEST_CUT && resultCode == RESULT_OK) {
            if (null == temps || ToolsHelper.isNull(temps.getPath())) {
                ToolsHelper.showStatus(mContext, false, getString(R.string.profileactivity_getdate_failed));
                return;
            }
            switch (pic_res) {
                case id_1:
                    flag_f = true;
                    String path1 = temps.getPath();
                    temps1 = new File(BitmapHelper.compressPic(path1, 100));
                    idCard = temps1.getPath();
                    BitmapHelper.displayPic(idFront, idCard);
                    break;
                case id_2:
                    flag_f = true;
                    String path2 = temps.getPath();
                    temps2 = new File(BitmapHelper.compressPic(path2, 100));
                    idCardBack = temps2.getPath();
                    BitmapHelper.displayPic(idBack, idCardBack);
                    break;
                case dirver_3:
                    flag_f = true;
                    String path3 = temps.getPath();
                    temps3 = new File(BitmapHelper.compressPic(path3, 100));
                    driverCard = temps3.getPath();
                    BitmapHelper.displayPic(dirverFront, driverCard);
                    break;
                case dirver_4:
                    flag_f = true;
                    String path4 = temps.getPath();
                    temps4 = new File(BitmapHelper.compressPic(path4, 100));
                    driverCardBack = temps4.getPath();
                    BitmapHelper.displayPic(dirverBack, driverCardBack);
                    break;
            }
        }
    }
    /**
     * 剪切图片
     */
    protected void crop(Uri uri) {
        Intent intents = new Intent("com.android.camera.action.CROP");
        intents.setDataAndType(uri, "image/*");
        temps = new File(FileHelper.getTakePhotoPath());
        if (!temps.getParentFile().exists())
            temps.getParentFile().mkdirs();
        intents.putExtra("output", Uri.fromFile(temps));
        intents.putExtra("crop", "true");
        intents.putExtra("aspectX", 800);// 裁剪框比例
        intents.putExtra("aspectY", 480);
        intents.putExtra("outputX", 800);// 输出图片大小
        intents.putExtra("outputY", 480);
        intents.putExtra("outputFormat", "JPEG"); // 输入文件格式
        startActivityForResult(intents, PHOTO_REQUEST_CUT);
    }

    /**
     * 弹出获取照片的pop
     */
    private void getPhotoPop() {
        CustomPopuwindow.getInstance().getPicture(this, mainView,findViewById(R.id.top__iv),new CustomPopuwindow.OnChoicePictureListener() {
            @Override
            public void formCameraEvent() {
                // 判断存储卡是否可以用，可用进行存储
                if (hasSdcard()) {
                    imageFilePath = FileHelper.getTakeAvatarPath();
                    Intent caremaIntent = GraphicHelper.getPhotoIntent(imageFilePath);
                    startActivityForResult(caremaIntent, PHOTO_REQUEST_CAMERA);
                }
            }

            @Override
            public void formMediaEvent() {
                // 激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
            }
        });
    }

    protected boolean hasSdcard() {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            return true;
        else
            return false;
    }

    /**
     * 认证界面按钮点击事件
     */
    private void submitClick() {
        Pattern p = Pattern
                .compile("[^\\d]+");
        Matcher m = p.matcher(name.getText().toString());
        switch (reviewType){
            case Constants.REVIEWING: //审核中
            case Constants.APPROVED: //审核通过
                finish();
                break;
            case Constants.UNCOMMITTED: //未提交
                if (ToolsHelper.isNull(idCard)) {
                    ToolsHelper.showStatus(mContext, false, "请选择身份证正面图片");
                    return;
                }
                if (ToolsHelper.isNull(idCardBack)) {
                    ToolsHelper.showStatus(mContext, false, "请选择身份证反面图片");
                    return;
                }
                if (ToolsHelper.isNull(driverCard)) {
                    ToolsHelper.showStatus(mContext, false, "请选择驾驶证正本");
                    return;
                }
                if (ToolsHelper.isNull(driverCardBack)) {
                    ToolsHelper.showStatus(mContext, false, "请选择驾驶证副本");
                    return;
                }
                if (ToolsHelper.isNull(driverNo.getText().toString())) {
                    ToolsHelper.showStatus(mContext, false, "请输入行驶证档案编号");
                    return;
                }
                if (ToolsHelper.isNull(name.getText().toString())) {
                    ToolsHelper.showStatus(mContext, false, "请输入姓名");
                    return;
                }

                if(!m.matches()){
                    ToolsHelper.showStatus(mContext, false, "用户名不能含有数字");
                    return;
                }
                String card = idNo.getText().toString();
                Pattern pattern = Pattern.compile("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))");
                Matcher matcher = pattern.matcher(card);
                boolean flag = matcher.matches();
                if (ToolsHelper.isNull(card)) {
                    ToolsHelper.showStatus(mContext, false, "请输入身份证号");
                    return;
                } else {
                    if (!flag) {
                        ToolsHelper.showStatus(mContext, false, "身份证格式错误");
                        return;
                    }
                }
                LoadingDialog.showDialog(mContext, getString(R.string.profileactivity_uploading));
                //上传图片
                FileManager.getInstance().uploadChatImage(idCard, accountsTable.getAccount() + "1");
                FileManager.getInstance().uploadChatImage(idCardBack, accountsTable.getAccount() + "2");
                FileManager.getInstance().uploadChatImage(driverCard, accountsTable.getAccount() + "3");
                FileManager.getInstance().uploadChatImage(driverCardBack, accountsTable.getAccount() + "4");
                break;
            case Constants.REFUSED: //重新认证
                if (ToolsHelper.isNull(accountsTable.getIdentityFrontId())) {
                    ToolsHelper.showStatus(mContext, false, "请选择身份证正面图片");
                    return;
                }
                if (ToolsHelper.isNull(accountsTable.getIdentityFrontId())) {
                    ToolsHelper.showStatus(mContext, false, "请选择身份证反面图片");
                    return;
                }
                if (ToolsHelper.isNull(accountsTable.getDrivingLicenseFrontId())) {
                    ToolsHelper.showStatus(mContext, false, "请选择驾驶证正本");
                    return;
                }
                if (ToolsHelper.isNull(accountsTable.getDrivingLicenseBackId())) {
                    ToolsHelper.showStatus(mContext, false, "请选择驾驶证副本");
                    return;
                }
                if (ToolsHelper.isNull(driverNo.getText().toString())) {
                    ToolsHelper.showStatus(mContext, false, "请输入行驶证档案编号");
                    return;
                }
                if (ToolsHelper.isNull(name.getText().toString())) {
                    ToolsHelper.showStatus(mContext, false, "请输入姓名");
                    return;
                }

                if(!m.matches()){
                    ToolsHelper.showStatus(mContext, false, "用户名不能含有数字");
                    return;
                }
                String card1 = idNo.getText().toString();
                Pattern pattern1 = Pattern.compile("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))");
                Matcher matcher1 = pattern1.matcher(card1);
                boolean flag1 = matcher1.matches();
                if (ToolsHelper.isNull(card1)) {
                    ToolsHelper.showStatus(mContext, false, "请输入身份证号");
                    return;
                } else {
                    if (!flag1) {
                        ToolsHelper.showStatus(mContext, false, "身份证格式错误");
                        return;
                    }
                }

                //上传图片
                if(flag_f){
                    if(accountsTable != null && accountsTable.getReviewType().equals(Constants.REFUSED)){
                        if(refuseReason.contains("ID_CARD") && refuseReason.contains("DRIVER_CARD")){
                            if(!ToolsHelper.isNull(idCard) && !ToolsHelper.isNull(idCardBack) && !ToolsHelper.isNull(driverCard) && !ToolsHelper.isNull(driverCardBack)){
                                LoadingDialog.showDialog(mContext, getString(R.string.profileactivity_uploading));
                                FileManager.getInstance().uploadChatImage(idCard, accountsTable.getAccount() + "1");
                                FileManager.getInstance().uploadChatImage(idCardBack, accountsTable.getAccount() + "2");
                                FileManager.getInstance().uploadChatImage(driverCard, accountsTable.getAccount() + "3");
                                FileManager.getInstance().uploadChatImage(driverCardBack, accountsTable.getAccount() + "4");
                            }else{
                                if(ToolsHelper.isNull(idCard)){
                                    ToolsHelper.showStatus(mContext, false, "请选择身份证正面图片");
                                }else if(ToolsHelper.isNull(idCardBack)){
                                    ToolsHelper.showStatus(mContext, false, "请选择身份证反面图片");
                                }else if(ToolsHelper.isNull(driverCard)){
                                    ToolsHelper.showStatus(mContext, false, "请选择驾驶证正本");
                                }else if(ToolsHelper.isNull(driverCardBack)){
                                    ToolsHelper.showStatus(mContext, false, "请选择驾驶证副本");
                                }
                            }
                        }else if(refuseReason.contains("ID_CARD") && !refuseReason.contains("DRIVER_CARD")){
                            if(!ToolsHelper.isNull(idCard) && !ToolsHelper.isNull(idCardBack)){
                                LoadingDialog.showDialog(mContext, getString(R.string.profileactivity_uploading));
                                FileManager.getInstance().uploadChatImage(idCard, accountsTable.getAccount() + "1");
                                FileManager.getInstance().uploadChatImage(idCardBack, accountsTable.getAccount() + "2");
                            }else{
                                if(ToolsHelper.isNull(idCard)){
                                    ToolsHelper.showStatus(mContext, false, "请选择身份证正面图片");
                                }else{
                                    ToolsHelper.showStatus(mContext, false, "请选择身份证反面图片");
                                }

                            }
                        }else if(!refuseReason.contains("ID_CARD") && refuseReason.contains("DRIVER_CARD")){
                            if(!ToolsHelper.isNull(driverCard) && !ToolsHelper.isNull(driverCardBack)){
                                LoadingDialog.showDialog(mContext, getString(R.string.profileactivity_uploading));
                                FileManager.getInstance().uploadChatImage(driverCard, accountsTable.getAccount() + "3");
                                FileManager.getInstance().uploadChatImage(driverCardBack, accountsTable.getAccount() + "4");
                            }else{
                                if (ToolsHelper.isNull(driverCard)) {
                                    ToolsHelper.showStatus(mContext, false, "请选择驾驶证正本");
                                    return;
                                }
                                if (ToolsHelper.isNull(driverCardBack)) {
                                    ToolsHelper.showStatus(mContext, false, "请选择驾驶证副本");
                                    return;
                                }
                            }
                        }
                    }


                } else {
                    if(accountsTable != null && accountsTable.getReviewType().equals(Constants.REFUSED)){
                        if(refuseReason.contains("ID_CARD") && refuseReason.contains("DRIVER_CARD")){
                            ToolsHelper.showStatus(mContext, false, "请选择身份证和驾驶证图片");
                        }else if(refuseReason.contains("ID_CARD") && !refuseReason.contains("DRIVER_CARD")){
                            ToolsHelper.showStatus(mContext, false, "请选择身份证正反面图片");
                        }else if(!refuseReason.contains("ID_CARD") && refuseReason.contains("DRIVER_CARD")){
                            ToolsHelper.showStatus(mContext, false, "请选择驾驶证正副本");
                        }
                    }
                }
                break;
        }
    }
    public void onEventMainThread(FileUploadedEvent event) {
        UploadModel uploadModel = null;
        if (null == event)
            return;
        uploadModel = GsonImplHelp.get().toObject(event.getResult(), UploadModel.class);
        String owner = event.getOwner();
        LoadingDialog.dismissDialog();
        if (owner.equals(accountsTable.getAccount() + "1")) {
            avatarFileTableId1 = uploadModel.getData().get(0).getNo();
            accountsTable.setIdentityFrontId(avatarFileTableId1);
        } else if (owner.equals(accountsTable.getAccount() + "2")) {
            avatarFileTableId2 = uploadModel.getData().get(0).getNo();
            accountsTable.setIdentityBackId(avatarFileTableId2);
        } else if (owner.equals(accountsTable.getAccount() + "3")) {
            avatarFileTableId3 = uploadModel.getData().get(0).getNo();
            accountsTable.setDrivingLicenseFrontId(avatarFileTableId3);
        } else if (owner.equals(accountsTable.getAccount() + "4")) {
            avatarFileTableId4 = uploadModel.getData().get(0).getNo();
            accountsTable.setDrivingLicenseBackId(avatarFileTableId4);
        }
        try {
            DbHelper.getDbUtils().update(accountsTable, "identityFrontId", "identityBackId", "drivingLicenseFrontId", "drivingLicenseBackId");
        } catch (Exception e) {
        }

        if (reviewType.equals(Constants.UNCOMMITTED)) {
            if(ToolsHelper.isNull(avatarFileTableId1) || ToolsHelper.isNull(avatarFileTableId2) || ToolsHelper.isNull(avatarFileTableId3)
                    || ToolsHelper.isNull(avatarFileTableId4)){
                return;
            }

        }
        if(accountsTable != null && accountsTable.getReviewType().equals(Constants.REFUSED)){
            if(accountsTable != null && accountsTable.getReviewType().equals(Constants.REFUSED)){
                if(refuseReason.contains("ID_CARD") && refuseReason.contains("DRIVER_CARD")){
                    if(ToolsHelper.isNull(avatarFileTableId1) || ToolsHelper.isNull(avatarFileTableId2) || ToolsHelper.isNull(avatarFileTableId3) || ToolsHelper.isNull(avatarFileTableId4)){
                        return;
                    }
                }else if(refuseReason.contains("ID_CARD") && !refuseReason.contains("DRIVER_CARD")){
                    if(ToolsHelper.isNull(avatarFileTableId1) || ToolsHelper.isNull(avatarFileTableId2)){
                        return;
                    }
                }else if(!refuseReason.contains("ID_CARD") && refuseReason.contains("DRIVER_CARD")){
                    if(ToolsHelper.isNull(avatarFileTableId3) || ToolsHelper.isNull(avatarFileTableId4)){
                        return;
                    }
                }
            }

        }
        commit();

    }

    public void commit(){
        LoadingDialog.showDialog(this, getString(R.string.profileactivity_dateuploading));
        PersonInfo info = new PersonInfo();
        info.setName(name.getText().toString());
        info.setIdentityNumber(idNo.getText().toString());
        info.setDrivingLicenseNumber(driverNo.getText().toString());
        if(accountsTable != null && accountsTable.getReviewType().equals(Constants.REFUSED)){
            if(refuseReason.contains("ID_CARD") && refuseReason.contains("DRIVER_CARD")){
                updatePicId(info,avatarFileTableId1,avatarFileTableId2,avatarFileTableId3,avatarFileTableId4);
            }else if(refuseReason.contains("ID_CARD") && !refuseReason.contains("DRIVER_CARD")){
                updatePicId(info,avatarFileTableId1,avatarFileTableId2,accountsTable.getDrivingLicenseFrontId(),accountsTable.getDrivingLicenseBackId());

            }else if(!refuseReason.contains("ID_CARD") && refuseReason.contains("DRIVER_CARD")){
                updatePicId(info,accountsTable.getIdentityFrontId(),accountsTable.getIdentityBackId(),avatarFileTableId3,avatarFileTableId4);
            }
        }else{
            updatePicId(info,avatarFileTableId1,avatarFileTableId2,avatarFileTableId3,avatarFileTableId4);
        }

        //提交认证信息
        ModuleHttpApi.identify(info);
    }

    private void updatePicId(PersonInfo info,String identityFrontId, String identityBackId, String drivingLicenseFrontId, String drivingLicenseBackId) {
        info.setIdentityFrontId(identityFrontId);
        info.setDrivingLicenseFrontId(drivingLicenseFrontId);
        info.setIdentityBackId(identityBackId);
        info.setDrivingLicenseBackId(drivingLicenseBackId);
    }

    /**
     * 请求成功处理
     * 提示保存
     *
     * @param event
     */
    public void onEventMainThread(HttpRequestEvent event) {
        LoadingDialog.dismissDialog();
        HttpResult result = event.getResult();
        String apiNo = result.getApiNo();
        if (!ModuleHttpApiKey.identify.equals(apiNo)) {
            return;
        }
        if (ModuleHttpApiKey.identify.equals(apiNo)) {
            ToolsHelper.showStatus(mContext, true, "提交成功");
            CoreHttpApi.enrolleesGet();
            finish();
        }

    }
    /**
     * 请求失败消息
     */
    public void onEventMainThread(HttpRequestErrorEvent event) {
        LoadingDialog.dismissDialog();
        HttpResult error = event.getResult();
        if (!ModuleHttpApiKey.identify.equals(error.getApiNo())) {
            return;
        }
        ToolsHelper.showHttpRequestErrorMsg(this, error);
    }


    /**
     * 初始化
     */
    private void init() {
        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.identifyingactivity_title));
        btnBack.setVisibility(View.VISIBLE);
//        mHelp.setText(getString(R.string.identifying_help));
        mHelp.setBackgroundResource(R.drawable.ic_help);
        mHelp.setVisibility(View.VISIBLE);
        LoadingDialog.showDialog(this,"请稍后...");
        CoreHttpApi.enrolleesGet(); //获取个人信息
    }
    public void onEventMainThread(AccountAvaliableEvent event) {
        LoadingDialog.dismissDialog();
        accountsTable = AccountManager.getInstance().findByNo();
        if(accountsTable != null){
            initData(accountsTable);
        }


    }

    /**
     * 初始化界面数据
     * @param accountsTable
     */
    private void initData(AccountsTable accountsTable) {
        reviewType = accountsTable.getReviewType();
        switch(accountsTable.getReviewType()){
            case Constants.UNCOMMITTED: //未提交
                initUncommittedUI(accountsTable);
                break;
            case Constants.REVIEWING: //审核中
                initChecking(accountsTable);
                break;
            case Constants.APPROVED: //审核通过
                initApprovedUI(accountsTable);
                break;
            case Constants.REFUSED: //审核拒绝
                initRefused(accountsTable);
                break;
        }
    }
    /**
     * 审核拒绝
     * @param accountsTable
     */
    private void initRefused(AccountsTable accountsTable) {
        refuseReason = accountsTable.getRefuseReason();
        if(refuseReason != null){
            idRefrushReasonList = new ArrayList<>();
            driverRefrushReasonList = new ArrayList<>();
            String[] split = refuseReason.split(";");
            if(split.length > 0){
                for(int i = 0; i < split.length; i++){
                    if(split[i].contains("ID_CARD")){
                        idRefrushReasonList.add(split[i]);
                    }else if(split[i].contains("DRIVER_CARD")){
                        driverRefrushReasonList.add(split[i]);
                    }
                }
            }

        }
        name.setFocusable(true);
        name.setFocusableInTouchMode(true);
        name.setText(accountsTable.getName());
        idNo.setFocusable(true);
        idNo.setFocusableInTouchMode(true);
        idNo.setText(accountsTable.getIdentityNumber());
        driverNo.setFocusable(true);
        driverNo.setFocusableInTouchMode(true);
        driverNo.setText(accountsTable.getDrivingLicenseNumber());
        ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, accountsTable.getIdentityFrontId(), idFront);
        ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, accountsTable.getIdentityBackId(), idBack);
        ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, accountsTable.getDrivingLicenseFrontId(), dirverFront);
        ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, accountsTable.getDrivingLicenseBackId(), dirverBack);
        submit.setText("重新认证");
        submit.setVisibility(View.VISIBLE);
        if(idRefrushReasonList.size() > 0 && driverRefrushReasonList.size() == 0){
            idCheckRefuseStatusShow(idRefrushReason());
        }else if(driverRefrushReasonList.size() > 0 && idRefrushReasonList.size() == 0){
            diverCardCheckRefuseStatusShow(driverRefrushReason());
        }else if(idRefrushReasonList.size() > 0 && driverRefrushReasonList.size() > 0){
            idAndDriverCheckRefuseStatusShow(idRefrushReason(),driverRefrushReason());
        }


    }
    /**
     * 身份证不合格拒绝原因显示
     */
    private String idRefrushReason() {
        String str = "身份证";
        for(int i = 0; i < idRefrushReasonList.size();i++){
            if(i == 0){
                switch (idRefrushReasonList.get(0)){
                    case "ID_CARD_NOT_CLEAR":
                        str += "不清晰";
                        break;
                    case "ID_CARD_IS_INVALID":
                        str += "无效";
                        break;
                    case "ID_CARD_IS_OVERDUE":
                        str += "过期";
                        break;
                }
            }else{
                switch (idRefrushReasonList.get(i)){
                    case "ID_CARD_NOT_CLEAR":
                        str += "且不清晰";
                        break;
                    case "ID_CARD_IS_INVALID":
                        str += "且无效";
                        break;
                    case "ID_CARD_IS_OVERDUE":
                        str += "且过期";
                        break;
                }
            }


        }
        return str;

    }

    /**
     * 驾驶证不合格拒绝原因显示
     */
    private String driverRefrushReason() {
        String str = "驾驶证";
        for(int i = 0; i < driverRefrushReasonList.size();i++){
            if(i == 0){
                switch (driverRefrushReasonList.get(0)){
                    case "DRIVER_CARD_NOT_CLEAR":
                        str += "不清晰";
                        break;
                    case "DRIVER_CARD_IS_INVALID":
                        str += "无效";
                        break;
                    case "DRIVER_CARD_IS_OVERDUE":
                        str += "过期";
                        break;
                }
            }else{
                switch (driverRefrushReasonList.get(i)){
                    case "DRIVER_CARD_NOT_CLEAR":
                        str += "且不清晰";
                        break;
                    case "DRIVER_CARD_IS_INVALID":
                        str += "且无效";
                        break;
                    case "DRIVER_CARD_IS_OVERDUE":
                        str += "且过期";
                        break;
                }
            }


        }
        return str;
    }

    /**
     * 审核中
     */
    private void initChecking(AccountsTable accountsTable) {
        reviewingOrApproved(accountsTable,R.drawable.bg_checking);
    }

    /**
     * 初始化未认证
     */
    private void initUncommittedUI(AccountsTable accountsTable) {
        name.setText(accountsTable.getName());
        idNo.setText(accountsTable.getIdentityNumber());
        driverNo.setText(accountsTable.getDrivingLicenseNumber());

    }
    /**
     * 初始化审核通过UI
     */
    private void initApprovedUI(AccountsTable accountsTable) {
        reviewingOrApproved(accountsTable,R.drawable.bg_checked);
    }
    /**
     * 驾驶证审核不通过，状态显示
     * @param reson 拒绝原因
     */
    private void diverCardCheckRefuseStatusShow(String reson) {
        idFrontIcon.setVisibility(View.GONE);
        idBackIcon.setVisibility(View.GONE);
        dirverFrontIcon.setVisibility(View.VISIBLE);
        dirverBackIcon.setVisibility(View.VISIBLE);
        idStatus.setVisibility(View.VISIBLE);
        idStatus.setImageResource(R.drawable.bg_checked);
        idRefrusedReson.setVisibility(View.GONE);
        driverStatus.setVisibility(View.VISIBLE);
        driverStatus.setImageResource(R.drawable.bg_refruse_check);
        driverRefruseReason.setVisibility(View.VISIBLE);
        driverRefruseReason.setText(reson);
    }
    /**
     * 身份证和驾驶证未通过审核，状态显示
     */
    private void idAndDriverCheckRefuseStatusShow(String idReson,String driverReason) {
        idFrontIcon.setVisibility(View.VISIBLE);
        idBackIcon.setVisibility(View.VISIBLE);
        idStatus.setVisibility(View.VISIBLE);
        idStatus.setImageResource(R.drawable.bg_refruse_check);
        idRefrusedReson.setVisibility(View.VISIBLE);
        idRefrusedReson.setText(idReson);
        dirverFrontIcon.setVisibility(View.VISIBLE);
        dirverBackIcon.setVisibility(View.VISIBLE);
        driverStatus.setVisibility(View.VISIBLE);
        driverStatus.setImageResource(R.drawable.bg_refruse_check);
        driverRefruseReason.setVisibility(View.VISIBLE);
        driverRefruseReason.setText(driverReason);
    }

    /**
     * 身份证审核不通过，状态显示
     * @param reson 拒绝原因
     */
    private void idCheckRefuseStatusShow(String reson) {
        idFrontIcon.setVisibility(View.VISIBLE);
        idBackIcon.setVisibility(View.VISIBLE);
        idStatus.setVisibility(View.VISIBLE);
        idStatus.setImageResource(R.drawable.bg_refruse_check);
        idRefrusedReson.setVisibility(View.VISIBLE);
        idRefrusedReson.setText(reson);
        dirverFrontIcon.setVisibility(View.GONE);
        dirverBackIcon.setVisibility(View.GONE);
        driverStatus.setVisibility(View.VISIBLE);
        driverStatus.setImageResource(R.drawable.bg_checked);
        driverRefruseReason.setVisibility(View.GONE);
    }
    /**
     * 审核中或审核通过状态显示
     */
    private void reviewingOrApproved(AccountsTable accountsTable,int res) {
        name.setFocusable(false);
        name.setFocusableInTouchMode(false);
        name.setText(accountsTable.getName());
        idNo.setFocusable(false);
        idNo.setFocusableInTouchMode(false);
        idNo.setText(accountsTable.getIdentityNumber());
        driverNo.setFocusable(false);
        driverNo.setFocusableInTouchMode(false);
        driverNo.setText(accountsTable.getDrivingLicenseNumber());
        idStatus.setVisibility(View.VISIBLE);
        idStatus.setImageResource(res);
        idRefrusedReson.setVisibility(View.GONE);
        ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, accountsTable.getIdentityFrontId(), idFront);
        idFrontIcon.setVisibility(View.GONE);
        ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, accountsTable.getIdentityBackId(), idBack);
        idBackIcon.setVisibility(View.GONE);
        driverStatus.setImageResource(res);
        driverRefruseReason.setVisibility(View.GONE);
        ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, accountsTable.getDrivingLicenseFrontId(), dirverFront);
        dirverFrontIcon.setVisibility(View.GONE);
        ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, accountsTable.getDrivingLicenseBackId(), dirverBack);
        dirverBackIcon.setVisibility(View.GONE);
        submit.setVisibility(View.VISIBLE);
        submit.setText("返回");
    }


}
