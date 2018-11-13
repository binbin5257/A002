package cn.lds.im.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
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
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.WebViewActivityHelper;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.PersonInfo;
import cn.lds.chatcore.data.UploadModel;
import cn.lds.chatcore.db.AccountsTable;
import cn.lds.chatcore.enums.IdentityType;
import cn.lds.chatcore.event.AccountAvaliableEvent;
import cn.lds.chatcore.event.FileUploadErrorEvent;
import cn.lds.chatcore.event.FileUploadedEvent;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.manager.FileManager;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import de.greenrobot.event.EventBus;

/*
* 认证页面
* */
public class IdentifyingActivity extends BaseActivity {

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    // 帮助
    @ViewInject(R.id.top_menu_btn)
    protected Button mHelp;
    @ViewInject(R.id.top__iv)
    protected ImageView ivTop;
    //身份证正面、背面文字布局，用于弹框高度控制
    @ViewInject(R.id.llyt_head)
    protected LinearLayout llyt_head;

    //未提交、审核中、已认证状态要显示的布局
    @ViewInject(R.id.llyt_identify_submit)
    protected LinearLayout llyt_identify_submit;
    //审核中要显示的布局
    @ViewInject(R.id.llyt_identify_submiting)
    protected LinearLayout llyt_identify_submiting;
    //照片上面的小相机图片
    @ViewInject(R.id.identify_take_picture1)
    protected Button identify_take_picture1;
    @ViewInject(R.id.identify_take_picture2)
    protected Button identify_take_picture2;
    @ViewInject(R.id.identify_take_picture3)
    protected Button identify_take_picture3;
    @ViewInject(R.id.identify_take_picture4)
    protected Button identify_take_picture4;
    //身份证正面
    @ViewInject(R.id.identifying_iv1)
    protected ImageView identifying_iv1;
    //驾驶证正本
    @ViewInject(R.id.identifying_iv2)
    protected ImageView identifying_iv2;
    //身份证反面
    @ViewInject(R.id.identifying_iv3)
    protected ImageView identifying_iv3;
    //驾驶证副本
    @ViewInject(R.id.identifying_iv4)
    protected ImageView identifying_iv4;

    //档案编号
    @ViewInject(R.id.identifying_no_driving_tv)
    protected TextView identifying_no_driving_tv;
    //身份证
    @ViewInject(R.id.identifying_no_car_tv)
    protected TextView identifying_no_car_tv;
    //姓名
    @ViewInject(R.id.identifying_no_name_tv)
    protected EditText identifying_no_name_tv;

    //按钮
    @ViewInject(R.id.identifyingactivity_submit)
    protected TextView identifyingactivity_submit;

    // 身份证认证成功
    @ViewInject(R.id.identifying_idcard_passed)
    protected ImageView identifying_idcard_passed;
    // 驾驶证认证成功
    @ViewInject(R.id.identifying_dirvercard_passed)
    protected ImageView identifying_dirvercard_passed;

    protected static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    protected static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    protected static final int PHOTO_REQUEST_CUT = 384;// 结果
    protected static final int INPUT_NO_NAME = 7;// 姓名
    protected static final int INPUT_NO_CAR = 8;// 身份证号
    protected static final int INPUT_NO_DRIVING = 9;// 行驶证编号


    protected String imageFilePath;// 图片路径
    protected File temps = null;
    protected File tempFile;
    protected String idCard = null, idCardBack = null, driverCard = null, driverCardBack = null;
    protected File temps1 = null, temps2 = null, temps3 = null, temps4 = null;
    protected int isIv1;
    protected AccountsTable mAccountsTable;

    /* 头像在File表中的记录ID，只在修改头像时可用 */
    protected String avatarFileTableId1, avatarFileTableId2, avatarFileTableId3, avatarFileTableId4;
    protected String reviewtype;

    protected IdentifyingActivity activity;
    protected int layoutID = R.layout.activity_identifying;
    protected boolean flag_f = false;

    public void setActivity(IdentifyingActivity activity) {
        this.activity = activity;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(IdentifyingActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initConfig();
    }

    protected void initConfig() {
        CoreHttpApi.enrolleesGet();
        init();
    }

    protected void init() {

        mAccountsTable = AccountManager.getInstance().findByNo();

        topTitle.setVisibility(View.VISIBLE);
        topTitle.setText(getString(R.string.identifyingactivity_title));
        btnBack.setVisibility(View.VISIBLE);
        mHelp.setText(getString(R.string.identifying_help));
        mHelp.setVisibility(View.VISIBLE);

        if (null == mAccountsTable) {
            //个人信息表为空，重新获取一次个人信息
            LoadingDialog.showDialog(mContext, "正在加载数据");
            CoreHttpApi.enrolleesGet();
            return;
        }
        // 认证
        reviewtype = mAccountsTable.getReviewType();
        if (ToolsHelper.isNull(reviewtype)) {//未提交
            this.initByIdentityType(IdentityType.UNIDENTITY);
            reviewtype = Constants.UNCOMMITTED;
        } else {
            if (reviewtype.equals(Constants.UNCOMMITTED))
                this.initByIdentityType(IdentityType.UNIDENTITY);
            else if (reviewtype.equals(Constants.REVIEWING))
                this.initByIdentityType(IdentityType.IDENTITYING);
            else if (reviewtype.equals(Constants.APPROVED))
                this.initByIdentityType(IdentityType.SUCCESS);
            else if (reviewtype.equals(Constants.REFUSED))
                this.initByIdentityType(IdentityType.FAILURE);
        }
        identifying_no_driving_tv.setText(ToolsHelper.isNullString(mAccountsTable.getDrivingLicenseNumber()));
        identifying_no_name_tv.setText(ToolsHelper.isNullString(mAccountsTable.getName()));
        identifying_no_car_tv.setText(ToolsHelper.isNullString(mAccountsTable.getIdentityNumber()));
    }

    /**
     * 根据认证状态设置画面显示
     *
     * @param identityType
     */
    public void initByIdentityType(IdentityType identityType) {
        try {

            if (IdentityType.UNIDENTITY == identityType) {          // 未认证
                identify_take_picture1.setVisibility(View.VISIBLE);
                identify_take_picture2.setVisibility(View.VISIBLE);
                identify_take_picture3.setVisibility(View.VISIBLE);
                identify_take_picture4.setVisibility(View.VISIBLE);

                // 身份证认证成功
                identifying_idcard_passed.setVisibility(View.GONE);
                // 驾驶证认证成功
                identifying_dirvercard_passed.setVisibility(View.GONE);
                // 按钮文字
                identifyingactivity_submit.setText(getString(R.string.identifyingactivity_submit));
            } else if (IdentityType.IDENTITYING == identityType) {  // 审核中(目前审核中不能查看所以此功能无用)
                identify_take_picture1.setVisibility(View.GONE);
                identify_take_picture2.setVisibility(View.GONE);
                identify_take_picture3.setVisibility(View.GONE);
                identify_take_picture4.setVisibility(View.GONE);
                //图片
                ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, mAccountsTable.getIdentityFrontId(), identifying_iv1);
                ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, mAccountsTable.getDrivingLicenseFrontId(), identifying_iv2);
                ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, mAccountsTable.getIdentityBackId(), identifying_iv3);
                ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, mAccountsTable.getDrivingLicenseBackId(), identifying_iv4);

                identifying_no_driving_tv.setFocusable(false);
                identifying_no_driving_tv.setFocusableInTouchMode(false);
                identifying_no_name_tv.setFocusable(false);
                identifying_no_name_tv.setFocusableInTouchMode(false);
                identifying_no_car_tv.setFocusable(false);
                identifying_no_car_tv.setFocusableInTouchMode(false);
                identifyingactivity_submit.setVisibility(View.GONE);
//                llyt_identify_submiting.setVisibility(View.VISIBLE);
//                identifyingactivity_submit.setText(R.string.identifyingactivity_enter);
            } else if (IdentityType.SUCCESS == identityType) {       // 认证成功
                identify_take_picture1.setVisibility(View.GONE);
                identify_take_picture2.setVisibility(View.GONE);
                identify_take_picture3.setVisibility(View.GONE);
                identify_take_picture4.setVisibility(View.GONE);

//                llyt_identify_submit.setVisibility(View.VISIBLE);
//                llyt_identify_submiting.setVisibility(View.GONE);

                // 身份证认证成功
                identifying_idcard_passed.setVisibility(View.GONE);
                // 驾驶证认证成功
                identifying_dirvercard_passed.setVisibility(View.GONE);
                // 按钮文字
                identifyingactivity_submit.setText(getString(R.string.identifyingactivity_resubmit));
                identifyingactivity_submit.setVisibility(View.GONE);
                //图片
                ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, mAccountsTable.getIdentityFrontId(), identifying_iv1);
                ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, mAccountsTable.getDrivingLicenseFrontId(), identifying_iv2);
                ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, mAccountsTable.getIdentityBackId(), identifying_iv3);
                ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, mAccountsTable.getDrivingLicenseBackId(), identifying_iv4);

                identifying_no_driving_tv.setFocusable(false);
                identifying_no_driving_tv.setFocusableInTouchMode(false);
                identifying_no_name_tv.setFocusable(false);
                identifying_no_name_tv.setFocusableInTouchMode(false);
                identifying_no_car_tv.setFocusable(false);
                identifying_no_car_tv.setFocusableInTouchMode(false);

            } else if (IdentityType.FAILURE == identityType) { // 认证失败
                identify_take_picture1.setVisibility(View.VISIBLE);
                identify_take_picture2.setVisibility(View.VISIBLE);
                identify_take_picture3.setVisibility(View.VISIBLE);
                identify_take_picture4.setVisibility(View.VISIBLE);
//                llyt_identify_submit.setVisibility(View.VISIBLE);
//                llyt_identify_submiting.setVisibility(View.GONE);

                // 身份证认证成功
                identifying_idcard_passed.setVisibility(View.GONE);
                // 驾驶证认证成功
                identifying_dirvercard_passed.setVisibility(View.GONE);
                // 按钮文字
                identifyingactivity_submit.setText(getString(R.string.identifyingactivity_submit));
                //图片
                if (!ToolsHelper.isNull(mAccountsTable.getIdentityFrontId())) {
                    avatarFileTableId1 = mAccountsTable.getIdentityFrontId();
                    ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, mAccountsTable.getIdentityFrontId(), identifying_iv1);
                }
                if (!ToolsHelper.isNull(mAccountsTable.getDrivingLicenseFrontId())) {
                    avatarFileTableId2 = mAccountsTable.getDrivingLicenseFrontId();
                    ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, mAccountsTable.getDrivingLicenseFrontId(), identifying_iv2);

                }
                if (!ToolsHelper.isNull(mAccountsTable.getIdentityBackId())) {
                    avatarFileTableId3 = mAccountsTable.getIdentityBackId();
                    ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, mAccountsTable.getIdentityBackId(), identifying_iv3);

                }
                if (!ToolsHelper.isNull(mAccountsTable.getDrivingLicenseBackId())) {
                    avatarFileTableId4 = mAccountsTable.getDrivingLicenseBackId();
                    ImageLoaderManager.getInstance().displayImageForIdentifying(mContext, mAccountsTable.getDrivingLicenseBackId(), identifying_iv4);

                }

            }
        } catch (Exception ex) {
            LogHelper.e("", ex);
        }
    }

    /**
     * 页面点击事件
     *
     * @param view
     */

    @OnClick({R.id.top_back_btn
            , R.id.top_menu_btn
            , R.id.identify_take_picture1//照片上面的小相机，只有点击它才可以选择照片
            , R.id.identify_take_picture2
            , R.id.identify_take_picture3
            , R.id.identify_take_picture4
            , R.id.identifyingactivity_submit//提交按钮
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.top_menu_btn:
                WebViewActivityHelper.startWebViewActivity(mContext, MyApplication.getInstance().getSERVER_HOST() + "/static/authenticationHelp.html", getString(R.string.identifying_help_detail));
                break;
            case R.id.identify_take_picture1:
                if (reviewtype.equals(Constants.APPROVED))
                    return;
                else if (reviewtype.equals(Constants.REVIEWING))
                    return;
                openPop();
                isIv1 = 1;
                break;
            case R.id.identify_take_picture2:
                if (reviewtype.equals(Constants.APPROVED))
                    return;
                else if (reviewtype.equals(Constants.REVIEWING))
                    return;
                openPop();
                isIv1 = 2;
                break;
            case R.id.identify_take_picture3:
                if (reviewtype.equals(Constants.APPROVED))
                    return;
                else if (reviewtype.equals(Constants.REVIEWING))
                    return;
                openPop();
                isIv1 = 3;
                break;
            case R.id.identify_take_picture4:
                if (reviewtype.equals(Constants.APPROVED))
                    return;
                else if (reviewtype.equals(Constants.REVIEWING))
                    return;
                openPop();
                isIv1 = 4;
                break;
            case R.id.identifyingactivity_submit:
                switch (reviewtype) {
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
                        if (ToolsHelper.isNull(identifying_no_driving_tv.getText().toString())) {
                            ToolsHelper.showStatus(mContext, false, "请输入行驶证档案编号");
                            return;
                        }
                        if (ToolsHelper.isNull(identifying_no_name_tv.getText().toString())) {
                            ToolsHelper.showStatus(mContext, false, "请输入姓名");
                            return;
                        }
                        String card = identifying_no_car_tv.getText().toString();
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
                        FileManager.getInstance().uploadChatImage(idCard, mAccountsTable.getAccount() + "1");
                        FileManager.getInstance().uploadChatImage(driverCard, mAccountsTable.getAccount() + "2");
                        FileManager.getInstance().uploadChatImage(idCardBack, mAccountsTable.getAccount() + "3");
                        FileManager.getInstance().uploadChatImage(driverCardBack, mAccountsTable.getAccount() + "4");
                        break;
                    case Constants.REFUSED: //重新认证
                        if (ToolsHelper.isNull(avatarFileTableId1) && ToolsHelper.isNull(idCardBack)) {
                            ToolsHelper.showStatus(mContext, false, "请选择身份证正面图片");
                            return;
                        }
                        if (ToolsHelper.isNull(avatarFileTableId3) && ToolsHelper.isNull(idCardBack)) {
                            ToolsHelper.showStatus(mContext, false, "请选择身份证反面图片");
                            return;
                        }
                        if (ToolsHelper.isNull(avatarFileTableId2) && ToolsHelper.isNull(driverCard)) {
                            ToolsHelper.showStatus(mContext, false, "请选择驾驶证正本");
                            return;
                        }
                        if (ToolsHelper.isNull(avatarFileTableId4) && ToolsHelper.isNull(driverCardBack)) {
                            ToolsHelper.showStatus(mContext, false, "请选择驾驶证副本");
                            return;
                        }
                        if (ToolsHelper.isNull(identifying_no_driving_tv.getText().toString())) {
                            ToolsHelper.showStatus(mContext, false, "请输入行驶证档案编号");
                            return;
                        }
                        if (ToolsHelper.isNull(identifying_no_name_tv.getText().toString())) {
                            ToolsHelper.showStatus(mContext, false, "请输入姓名");
                            return;
                        }
                        String card1 = identifying_no_car_tv.getText().toString();
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
                        LoadingDialog.showDialog(mContext, getString(R.string.profileactivity_uploading));
                        //上传图片
                        if(flag_f){
                            if(!ToolsHelper.isNull(idCard)){
                                FileManager.getInstance().uploadChatImage(idCard, mAccountsTable.getAccount() + "1");
                            }
                            if(!ToolsHelper.isNull(driverCard)){
                                FileManager.getInstance().uploadChatImage(driverCard, mAccountsTable.getAccount() + "2");
                            }
                            if(!ToolsHelper.isNull(idCardBack)){
                                FileManager.getInstance().uploadChatImage(idCardBack, mAccountsTable.getAccount() + "3");
                            }
                            if(!ToolsHelper.isNull(driverCardBack)){
                                FileManager.getInstance().uploadChatImage(driverCardBack, mAccountsTable.getAccount() + "4");
                            }
                        } else {
                            commit();
                        }
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    /**
     * 弹出选择框
     */
    @SuppressWarnings("deprecation")
    protected void openPop() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_head, null);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        // 声明一个弹出框
        final PopupWindow popupWindow = new PopupWindow(llyt_head, wm.getDefaultDisplay().getWidth(), wm
                .getDefaultDisplay().getHeight());
        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pop_black_bg));

        // 为弹出框设定自定义的布局
        popupWindow.setContentView(contentView);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(ivTop);

        LinearLayout bg = (LinearLayout) contentView.findViewById(R.id.pop_bg);
        TextView cancel = (TextView) contentView.findViewById(R.id.pop_cancel);
        TextView formCamera = (TextView) contentView.findViewById(R.id.pop_camera);
        TextView formMedia = (TextView) contentView.findViewById(R.id.pop_meida);

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        formCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 判断存储卡是否可以用，可用进行存储
                if (hasSdcard()) {
                    imageFilePath = FileHelper.getTakeAvatarPath();
                    Intent caremaIntent = GraphicHelper.getPhotoIntent(imageFilePath);
                    startActivityForResult(caremaIntent, PHOTO_REQUEST_CAMERA);
                }
                popupWindow.dismiss();
            }
        });
        formMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                popupWindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
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
                tempFile = new File(imageFilePath);
                crop(Uri.fromFile(tempFile));
            } else {
                // LogUtil.i(TAG, "未找到存储卡，无法存储照片！");
            }

        } else if (requestCode == PHOTO_REQUEST_CUT && resultCode == RESULT_OK) {
            if (null == temps || ToolsHelper.isNull(temps.getPath())) {
                ToolsHelper.showStatus(mContext, false, getString(R.string.profileactivity_getdate_failed));
                return;
            }
            switch (isIv1) {
                case 1:
                    flag_f = true;
                    String path1 = temps.getPath();
                    temps1 = new File(BitmapHelper.compressPic(path1, 100));
                    idCard = temps1.getPath();
                    BitmapHelper.displayPic(identifying_iv1, idCard);
                    break;
                case 2:
                    flag_f = true;
                    String path2 = temps.getPath();
                    temps2 = new File(BitmapHelper.compressPic(path2, 100));
                    driverCard = temps2.getPath();
                    BitmapHelper.displayPic(identifying_iv2, driverCard);
                    break;
                case 3:
                    flag_f = true;
                    String path3 = temps.getPath();
                    temps3 = new File(BitmapHelper.compressPic(path3, 100));
                    idCardBack = temps3.getPath();
                    BitmapHelper.displayPic(identifying_iv3, idCardBack);
                    break;
                case 4:
                    flag_f = true;
                    String path4 = temps.getPath();
                    temps4 = new File(BitmapHelper.compressPic(path4, 100));
                    driverCardBack = temps4.getPath();
                    BitmapHelper.displayPic(identifying_iv4, driverCardBack);
                    break;
            }
        } else if (requestCode == INPUT_NO_NAME && resultCode == RESULT_OK) {
            String str = data.getStringExtra("data");
            identifying_no_name_tv.setText(str);
        } else if (requestCode == INPUT_NO_CAR && resultCode == RESULT_OK) {
            String str = data.getStringExtra("data");
            identifying_no_car_tv.setText(str);
        } else if (requestCode == INPUT_NO_DRIVING && resultCode == RESULT_OK) {
            String str = data.getStringExtra("data");
            identifying_no_driving_tv.setText(str);
        }
    }

    /**
     * 剪切图片
     *
     * @param uri
     * @function:
     * @author:Jerry
     * @date:2013-12-30
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

    protected boolean hasSdcard() {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            return true;
        else
            return false;
    }


    /**
     * 请求成功处理
     * 提示保存r
     *
     * @param event
     */
    public void onEventMainThread(HttpRequestEvent event) {

        HttpResult result = event.getResult();
        String apiNo = result.getApiNo();
        if (!ModuleHttpApiKey.identify.equals(apiNo)) {
            return;
        }
        if (ModuleHttpApiKey.identify.equals(apiNo)) {
            LoadingDialog.dismissDialog();
            ToolsHelper.showStatus(mContext, true, "提交成功");
            CoreHttpApi.enrolleesGet();
            finish();
        }

    }

    /**
     * 请求失败消息
     */
    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult error = event.getResult();
        if (!ModuleHttpApiKey.identify.equals(error.getApiNo())) {
            return;
        }
        LoadingDialog.dismissDialog();
        ToolsHelper.showHttpRequestErrorMsg(this, error);
    }


    public void onEventMainThread(FileUploadedEvent event) {
        UploadModel uploadModel = null;
        if (null == event)
            return;
        uploadModel = GsonImplHelp.get().toObject(event.getResult(), UploadModel.class);
        String url = event.getFilePath();
        String owner = event.getOwner();
//        if (!(idCard.equals(temps1.getPath()) || driverCard.equals(temps2.getPath())
//                || idCardBack.equals(temps3.getPath())
//                || driverCardBack.equals(temps4.getPath())
//        )) {
//            return;
//        }
        LoadingDialog.dismissDialog();
        if (owner.equals(mAccountsTable.getAccount() + "1")) {
            avatarFileTableId1 = uploadModel.getData().get(0).getNo();
            mAccountsTable.setIdentityFrontId(avatarFileTableId1);
        } else if (owner.equals(mAccountsTable.getAccount() + "2")) {
            avatarFileTableId2 = uploadModel.getData().get(0).getNo();
            mAccountsTable.setIdentityBackId(avatarFileTableId2);
        } else if (owner.equals(mAccountsTable.getAccount() + "3")) {
            avatarFileTableId3 = uploadModel.getData().get(0).getNo();
            mAccountsTable.setDrivingLicenseFrontId(avatarFileTableId3);
        } else if (owner.equals(mAccountsTable.getAccount() + "4")) {
            avatarFileTableId4 = uploadModel.getData().get(0).getNo();
            mAccountsTable.setDrivingLicenseBackId(avatarFileTableId4);
        }
        try {
            DbHelper.getDbUtils().update(mAccountsTable, "identityFrontId", "identityBackId", "drivingLicenseFrontId", "drivingLicenseBackId");
        } catch (Exception e) {
        }

        if (ToolsHelper.isNull(avatarFileTableId1) || ToolsHelper.isNull(avatarFileTableId2) || ToolsHelper.isNull(avatarFileTableId3)
                || ToolsHelper.isNull(avatarFileTableId4)) {
            return;
        }
        commit();

    }
    public void commit(){
        LoadingDialog.dismissDialog();
        LoadingDialog.showDialog(this, getString(R.string.profileactivity_dateuploading));
        PersonInfo info = new PersonInfo();
        info.setName(identifying_no_name_tv.getText().toString());
        info.setIdentityNumber(identifying_no_car_tv.getText().toString());
        info.setDrivingLicenseNumber(identifying_no_driving_tv.getText().toString());
        info.setIdentityFrontId(avatarFileTableId1);
        info.setDrivingLicenseFrontId(avatarFileTableId3);
        info.setIdentityBackId(avatarFileTableId2);
        info.setDrivingLicenseBackId(avatarFileTableId4);
        //提交认证信息
        ModuleHttpApi.identify(info);
    }

    public void onEventMainThread(FileUploadErrorEvent event) {
        String url = event.getFilePath();
        if (!url.equals(temps.getPath())) {
            return;
        }
        ToolsHelper.showStatus(mContext, false, "上传图片失败");
    }


    @Override
    protected void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            LogHelper.e(IdentifyingActivity.class.getName(), e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            LogHelper.e(IdentifyingActivity.class.getName(), e);
        }
    }

    /**
     * 跟新个人信息
     */
    public void onEventMainThread(AccountAvaliableEvent event) {
        LoadingDialog.dismissDialog();
        init();
    }

}
