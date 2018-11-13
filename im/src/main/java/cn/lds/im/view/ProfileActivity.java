package cn.lds.im.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

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
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.WheelViewUtil;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.PersonInfo;
import cn.lds.chatcore.data.UploadModel;
import cn.lds.chatcore.db.AccountsTable;
import cn.lds.chatcore.db.MasterTable;
import cn.lds.chatcore.enums.MasterType;
import cn.lds.chatcore.event.AccountAvaliableEvent;
import cn.lds.chatcore.event.FileUploadErrorEvent;
import cn.lds.chatcore.event.FileUploadedEvent;
import cn.lds.chatcore.event.HttpRequestErrorEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.manager.AccountManager;
import cn.lds.chatcore.manager.FileManager;
import cn.lds.chatcore.manager.ImageLoaderManager;
import cn.lds.chatcore.manager.MasterManager;
import cn.lds.chatcore.view.BaseActivity;
import cn.lds.im.common.ModuleHttpApi;
import cn.lds.im.common.ModuleHttpApiKey;
import cn.lds.im.view.widget.SelectedCorePopWindow;
import de.greenrobot.event.EventBus;

/**
 * 个人信息
 */
public class ProfileActivity extends BaseActivity {

    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView top_title_tv;
    @ViewInject(R.id.top_menu_btn_del)
    protected ImageView top_menu_btn_del;
    // 头像
    @ViewInject(R.id.iv_me_person_info_head)
    protected ImageView iv_me_person_info_head;
    @ViewInject(R.id.rl_me_person_info_head)
    protected RelativeLayout rlHead;
    // 昵称
    @ViewInject(R.id.txt_me_person_info_name)
    protected TextView txt_me_person_info_name;
    // 手机号
    @ViewInject(R.id.txt_me_person_info_phone)
    protected TextView txt_me_person_info_phone;
    // 邮箱
    @ViewInject(R.id.txt_me_person_info_email)
    protected TextView txt_me_person_info_email;
    // 性别
    @ViewInject(R.id.txt_me_person_info_sex)
    protected TextView txt_me_person_info_sex;
    // 认证
    @ViewInject(R.id.txt_me_person_info_identify)
    protected TextView txt_me_person_info_identify;
    @ViewInject(R.id.image_identify)
    protected ImageView image_identify;
    @ViewInject(R.id.main_view)
    protected RelativeLayout mainView;
    @ViewInject(R.id.rl_bind)
    protected RelativeLayout bindRlyt;
    @ViewInject(R.id.bind_status)
    protected TextView bind_status;
    @ViewInject(R.id.txt_me_person_info_company)
    protected TextView txt_me_person_info_company;
    @ViewInject(R.id.txt_me_person_info_department)
    protected TextView user_department_tv;
    @ViewInject(R.id.txt_me_person_no)
    protected TextView txt_me_person_no;
    @ViewInject(R.id.txt_me_person_info_discount)
    protected TextView user_discount_tv;

    @ViewInject(R.id.top__iv)
    protected ImageView ivTop;

    protected Bitmap bitmap;
    protected File tempFile;
    protected String avatarFilePath;// 头像路径

    // 联系人表
    protected AccountsTable mAccountsTable;
    public Context context;
    public Intent intent;

    protected static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    protected static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    protected static final int PHOTO_REQUEST_CUT = 384;// 结果
    protected static final int PERSONAL_NICK_NAME = 101;// 昵称
    protected static final int PERSONAL_PHONE = 102;// 手机号
    protected static final int PERSONAL_NAME = 103;// 手机号
    protected static final int PERSONAL_EMAIL = 104;// 邮箱
    protected static final int PERSONAL_GENDER = 105;// 性别
    protected static final int PERSONAL_AREA = 107;// 地区

    /* 头像在File表中的记录ID，只在修改头像时可用 */
    protected String avatarFileTableId;

    protected FileManager mFileManager;

    protected SelectedCorePopWindow popwindow;

    protected String sex = "";


    protected UploadModel uploadModel = null;

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PERSONAL_GENDER:
                    // TODO: 15/12/28  处理选择性别后,调用接口
//                    ToolsHelper.showInfo(context, popwindow.genderName + "--" + popwindow.genderCode);
                    String newsex = popwindow.genderCode;
                    if (newsex.equals(sex))
                        return;
                    sex = newsex;
                    LoadingDialog.showDialog(mContext, getString(R.string.profileactivity_dateuploading));
                    PersonInfo info2 = new PersonInfo();
                    info2.setGender(newsex);
                    ModuleHttpApi.enrollees(info2, "gender", newsex);
                    break;
            }
        }
    };

    protected ProfileActivity activity;
    protected int layoutID = R.layout.activity_setting_person_info;
    private ArrayList<String> sexList;

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    public void setActivity(ProfileActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(ProfileActivity.class, this);
        if (null != activity) {
            ViewUtils.inject(activity);
        }
        initConfig();
    }

    protected void initConfig() {
        mFileManager = FileManager.getInstance();
        init();
    }

    /**
     * 初始化个人信息
     */
    public void init() {
        intent = new Intent();
        btnBack.setVisibility(View.VISIBLE);
        top_title_tv.setText(getString(R.string.profileactivity_title));
        top_menu_btn_del.setVisibility(View.GONE);

        context = ProfileActivity.this;

    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadingDialog.showDialog(this,"请稍后...");
        CoreHttpApi.enrolleesGet(); //获取个人信息

    }


    /**
     * 初始化画面信息
     */
    protected void refresh() {
        // 获取当前登录人信息
        mAccountsTable = AccountManager.getInstance().findByNo();
        String gender = "";
        if (null == mAccountsTable) {
            //个人信息表为空，重新获取一次个人信息
            CoreHttpApi.enrolleesGet();
            return;
        }


        MasterTable tableGender = MasterManager.getInstance().getTableByTypeAndKey(MasterType.Sexuality, mAccountsTable.getGender());

        if (tableGender != null) {
            gender = tableGender.getText();
            sex = tableGender.getKey();
        }

        if(mAccountsTable.getObtainZmxyAuthorize() == 1){
            bind_status.setText(String.valueOf(mAccountsTable.getScore()) + "分");
        }else{
            bind_status.setText(String.valueOf("未绑定"));
        }
        // 昵称
        txt_me_person_info_name.setText(ToolsHelper.toString(mAccountsTable.getName() == null ? "" : mAccountsTable.getName()));
        txt_me_person_info_company.setText(ToolsHelper.toString(mAccountsTable.getCompanyName()));
        user_department_tv.setText(ToolsHelper.toString(mAccountsTable.getDepartmentName()));
        txt_me_person_no.setText(ToolsHelper.toString(mAccountsTable.getUserNo()));
        user_discount_tv.setText(ToolsHelper.toString(Double.parseDouble(mAccountsTable.getDiscount())*10 + "折"));
        // 手机号
        String phone = mAccountsTable.getMobile();
        if (!ToolsHelper.isNull(phone))
            phone = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
        txt_me_person_info_phone.setText(phone);
        // 邮箱
        txt_me_person_info_email.setText(ToolsHelper.toString(mAccountsTable.getEmail() == null ? "请输入" : mAccountsTable.getEmail()));
        // 性别
        txt_me_person_info_sex.setText(ToolsHelper.toString(gender == "" ? "请选择" : gender));
        // 认证
        String reviewtype = mAccountsTable.getReviewType();
        if (ToolsHelper.isNull(reviewtype)) {
            txt_me_person_info_identify.setText(Constants.UNCOMMITTEDNAME);
        } else {
            switch (reviewtype) {
                case Constants.UNCOMMITTED://未认证
                    txt_me_person_info_identify.setText(Constants.UNCOMMITTEDNAME);
                    txt_me_person_info_identify.setTextColor(getResources().getColor(R.color.submenu_no_identification));
                    image_identify.setImageResource(R.drawable.bg_unidentified);
                    break;
                case Constants.REVIEWING://待审核
                    txt_me_person_info_identify.setText(Constants.REVIEWINGNAME);
                    txt_me_person_info_identify.setTextColor(getResources().getColor(R.color.submenu_no_identification));
                    image_identify.setImageResource(R.drawable.bg_unidentified);
                    break;
                case Constants.APPROVED://审核通过
                    txt_me_person_info_identify.setText(Constants.APPROVEDNAME);
                    txt_me_person_info_identify.setTextColor(getResources().getColor(R.color.submenu_identification));
                    image_identify.setImageResource(R.drawable.bg_identified);
                    break;
                case Constants.REFUSED://审核拒绝
                    txt_me_person_info_identify.setText(Constants.REFUSEDNAME);
                    txt_me_person_info_identify.setTextColor(getResources().getColor(R.color.submenu_no_identification));
                    image_identify.setImageResource(R.drawable.bg_unidentified);
                    break;
            }
        }
        //头像
        String headPicId = mAccountsTable.getAvatar();
        if (!ToolsHelper.isNull(headPicId))
            ImageLoaderManager.getInstance().displayImage(headPicId, iv_me_person_info_head);

    }

    /**
     * 返回按钮
     *
     * @param v
     */
    @OnClick({R.id.top_back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back_btn:
                Intent intent = new Intent();
                this.setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            this.setResult(RESULT_OK,intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 属性点击事件
     *
     * @param v
     */
    @OnClick({R.id.iv_me_person_info_head
            , R.id.iv_me_person_info_head_zhegai
            , R.id.rl_me_person_info_head
            , R.id.rl_me_person_info_name
            , R.id.rl_me_person_info_phone
            , R.id.rl_me_person_info_email
            , R.id.rl_me_person_info_sex
            , R.id.rl_me_person_info_identify
            , R.id.rl_bind
            , R.id.top_menu_btn_del})
    public void detailOnClick(View v) {
        switch (v.getId()) {
            // 头像
            case R.id.iv_me_person_info_head:
            case R.id.iv_me_person_info_head_zhegai:
                openPop();
//                intent.setClass(this, SwitchImageActivity.class);
//                startActivity(intent);
                break;
            case R.id.top_menu_btn_del:
                PopWindowHelper.getInstance().openCustomerservice(mContext).show(findViewById(R.id.top__iv));
                break;
            // 头像选择
            case R.id.rl_me_person_info_head:
                openPop();
                break;
            // 邮箱
            case R.id.rl_me_person_info_email:
                if (mAccountsTable == null) {
                    return;
                }
                intent.setClass(this, ProfileChangeActivity.class);
                intent.putExtra("title_name", "邮箱");
                intent.putExtra("type", ProfileChangeActivity.SINGLE_LINE_EDITTEXT);
                intent.putExtra("checkType", ProfileChangeActivity.isEmailCheck);
                intent.putExtra("input_num", 50);
                intent.putExtra("content", mAccountsTable.getEmail());

                startActivityForResult(intent, PERSONAL_EMAIL);
                break;
            // 性别
            case R.id.rl_me_person_info_sex:
                if (mAccountsTable == null) {
                    return;
                }

                selectSex();
                break;
            // 手机
            case R.id.rl_me_person_info_phone:

                break;
            // 姓名
            case R.id.rl_me_person_info_name:

                break;

            // 认证
            case R.id.rl_me_person_info_identify:
                intent.setClass(this, AuthenticationActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_bind:
                if (mAccountsTable == null) {
                    return;
                }
                if(!Constants.APPROVED.equals(mAccountsTable.getReviewType())){
                    ToolsHelper.showStatus(this,false,"身份认证后绑定芝麻信用");
                    return;
                }
                if(mAccountsTable.getObtainZmxyAuthorize() == 1){
                    ToolsHelper.showStatus(this,true,"您已经绑定芝麻信用");
                }else{
                    intent.setClass(this,ZhiMaCreditActivity.class);
                    startActivity(intent);
                }
//                intent.setClass(this,ZhiMaCreditActivity.class);
//                startActivity(intent);


                break;


        }
    }
    private void initSexData(){
        if(sexList == null){
            sexList = new ArrayList<>();
        }else{
            sexList.clear();
        }
        if(!TextUtils.isEmpty(sex)){
            if(sex.equals("MALE")){
                sexList.add("女");
                sexList.add("男");
            }else{
                sexList.add("男");
                sexList.add("女");

            }
        }else{
            sexList.add("女");
            sexList.add("男");
        }


    }

    /**
     * 选择性别
     */
    private void selectSex() {
        initSexData();
        WheelViewUtil.alertBottomWheelOption(ProfileActivity.this, sexList, new WheelViewUtil.OnWheelViewClick() {
            @Override
            public void onClick(View view, int postion) {
                String textSex = sexList.get(postion);
                if(textSex.equals("男")){
                    textSex = "MALE";
                }else{
                    textSex = "FEMALE";
                }
                if (textSex.equals(sex))
                    return;
                sex = textSex;
                LoadingDialog.showDialog(mContext, getString(R.string.profileactivity_dateuploading));
                PersonInfo info2 = new PersonInfo();
                info2.setGender(textSex);
                ModuleHttpApi.enrollees(info2, "gender", textSex);
            }
        });
    }


    /**
     * 弹出选择框
     */
    @SuppressWarnings("deprecation")
    protected void openPop() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_head, null);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        // 声明一个弹出框
        final PopupWindow popupWindow = new PopupWindow(rlHead, wm.getDefaultDisplay().getWidth(), wm
                .getDefaultDisplay().getHeight());
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.pop_black_bg));

        // 为弹出框设定自定义的布局
        popupWindow.setContentView(contentView);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(ivTop);

        LinearLayout bg = (LinearLayout) contentView.findViewById(R.id.pop_bg);
        TextView cancel = (TextView) contentView.findViewById(R.id.pop_cancel);
        TextView formCamera = (TextView) contentView.findViewById(R.id.pop_camera);
        TextView formMedia = (TextView) contentView.findViewById(R.id.pop_meida);
        formMedia.setVisibility(View.VISIBLE);
        bg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        formCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // 判断存储卡是否可以用，可用进行存储
                if (hasSdcard()) {
                    avatarFilePath = FileHelper.getTakeAvatarPath();
                    Intent caremaIntent = GraphicHelper.getPhotoIntent(avatarFilePath);
                    startActivityForResult(caremaIntent, PHOTO_REQUEST_CAMERA);
                }
                popupWindow.dismiss();
            }
        });
        formMedia.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                popupWindow.dismiss();
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    protected boolean hasSdcard() {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            return true;
        else
            return false;
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
                tempFile = new File(avatarFilePath);
                crop(Uri.fromFile(tempFile));
            } else {
                // LogUtil.i(TAG, "未找到存储卡，无法存储照片！");
            }

        } else if (requestCode == PHOTO_REQUEST_CUT && resultCode == RESULT_OK) {
            if (null == temps || ToolsHelper.isNull(temps.getPath())) {
                ToolsHelper.showStatus(mContext, false, getString(R.string.profileactivity_getdate_failed));
                return;
            }
            if (mAccountsTable == null) {
                return;
            }
//            LoadingDialog.showDialog(context, getString(R.string.profileactivity_uploading));
            //上传图片
            FileManager.getInstance().uploadChatImage(temps.getPath(), mAccountsTable.getAccount());

        } else if (4321 == requestCode && 1001 == resultCode) {
            //msg = data.getStringExtra("msg");
        }
        //处理上一页 点击返回按钮 遇到的data为空的处理
        if (data == null) {
            return;
        }
        switch (requestCode) {
            // 昵称
            case PERSONAL_NICK_NAME:
                String nickName = data.getStringExtra("data");
                if (nickName.equals(mAccountsTable.getNickname())) {
                    return;
                }
                txt_me_person_info_name.setText(nickName);
                break;
            // 手机号
            case PERSONAL_PHONE:
//                String phone = data.getStringExtra("data");
//                if (ToolsHelper.isNull(phone)) {
//                    ToolsHelper.showInfo(context, getString(R.string.forgetpasswordactivity_phonealert));
//                    return;
//                }
                txt_me_person_info_phone.setText("");
                break;
            // 邮箱
            case PERSONAL_EMAIL:
                String email = data.getStringExtra("data");
                if (ToolsHelper.isNull(email) || email.equals(mAccountsTable.getEmail())) {
                    return;
                }
//                LoadingDialog.showDialog(this, getString(R.string.profileactivity_dateuploading));
                PersonInfo info = new PersonInfo();
                info.setEmail(email);
                ModuleHttpApi.enrollees(info, "email", email);
                break;
            case PERSONAL_NAME:
                String name = data.getStringExtra("data");
                if (ToolsHelper.isNull(name) || name.equals(mAccountsTable.getName())) {
                    return;
                }
                LoadingDialog.showDialog(this, getString(R.string.profileactivity_dateuploading));
                PersonInfo info2 = new PersonInfo();
                info2.setName(name);
                ModuleHttpApi.enrollees(info2, "name", name);
                break;
            default:
                break;
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            LogHelper.e(ProfileActivity.class.getName(), e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            LogHelper.e(ProfileActivity.class.getName(), e);
        }
    }

    protected File temps = null;

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
        intents.putExtra("aspectX", 1);// 裁剪框比例
        intents.putExtra("aspectY", 1);
        intents.putExtra("outputX", 240);// 输出图片大小
        intents.putExtra("outputY", 240);
        intents.putExtra("outputFormat", "JPEG"); // 输入文件格式
        startActivityForResult(intents, PHOTO_REQUEST_CUT);
    }

    /**
     * 目前暂定为无用的监听
     * 头像已经下载下来了，准备显示
     *
     * @param event
     */
//    public void onEventMainThread(FileAvailableEvent event) {
//        FilesTable filesTable = event.getFilesTable();
//
//        //已知头像存储ID
//        if (null != getAccount() && getAccount().equals(filesTable.getOwner())) {
//            ImageLoaderManager.getInstance().displayAvatar(filesTable.getFileStorageId(), iv_me_person_info_head);
//        }
//    }

    /**
     * 请求失败消息
     */
    public void onEventMainThread(HttpRequestErrorEvent event) {
        HttpResult error = event.getResult();
        if (!(ModuleHttpApiKey.enrollees.equals(error.getApiNo()) ||
                ModuleHttpApiKey.enrolleesGet.equals(error.getApiNo()))) {
            return;
        }
        ToolsHelper.showHttpRequestErrorMsg(this, error);
    }

    public void onEventMainThread(FileUploadErrorEvent event) {
        String owner = event.getOwner();
        String url = event.getFilePath();
        if (!mAccountsTable.getAccount().equals(owner) || !url.equals(temps.getPath()))
            return;
        LoadingDialog.dismissDialog();
    }


    /**
     * 请求成功处理
     * 提示保存
     *
     * @param event
     */
    public void onEventMainThread(HttpRequestEvent event) {

        HttpResult result = event.getResult();
        String apiNo = result.getApiNo();
        if (!(ModuleHttpApiKey.enrollees.equals(apiNo) ||
                ModuleHttpApiKey.enrolleesGet.equals(apiNo))) {
            return;
        }
        LoadingDialog.dismissDialog();
        if (ModuleHttpApiKey.enrollees.equals(apiNo)) {
            Map<String, String> map = result.getExtras();
            String key = map.get("key");
            String value = map.get("value");
            if ("name".equals(key)) {
                ToolsHelper.showStatus(mContext, true, getString(R.string.activity_setting_person_info_mingzi) + "修改成功");
                txt_me_person_info_name.setText(value);
            } else if ("email".equals(key)) {
                ToolsHelper.showStatus(mContext, true, getString(R.string.common_email) + "修改成功");
                txt_me_person_info_email.setText(value);
                //存入本地
                mAccountsTable.setEmail(value);
                CoreHttpApi.enrolleesGet(); //获取个人信息
            } else if ("gender".equals(key)) {
                ToolsHelper.showStatus(mContext, true, getString(R.string.activity_setting_person_info_xingbie) + "修改成功");
                if ("FEMALE".equals(value)) {
                    txt_me_person_info_sex.setText("女");
                } else if ("MALE".equals(value)) {
                    txt_me_person_info_sex.setText("男");
                }
            } else if ("headPortraitId".equals(key)) {
                ToolsHelper.showStatus(mContext, true, getString(R.string.activity_setting_person_info_touxiang) + "修改成功");

                BitmapHelper.displayPic(iv_me_person_info_head, temps.getPath());
                //存入本地
                mAccountsTable.setAvatar(avatarFileTableId);
                try {
                    DbHelper.getDbUtils().update(mAccountsTable, "avatar");
                } catch (Exception e) {
                }
            }
            mAccountsTable = AccountManager.getInstance().findByNo();
        } else if (ModuleHttpApiKey.enrolleesGet.equals(apiNo)) {
            refresh();
        }
        LoadingDialog.dismissDialog();
    }

    public void onEventMainThread(FileUploadedEvent event) {
        if (null != event) {
            uploadModel = GsonImplHelp.get().toObject(event.getResult(), UploadModel.class);
        }
        if (null != uploadModel) {
            avatarFileTableId = uploadModel.getData().get(0).getNo();
        }
        String url = event.getFilePath();
        String owner = event.getOwner();
        if (!mAccountsTable.getAccount().equals(owner) || !url.equals(temps.getPath())) {
            return;
        }


        //提交头像id
        PersonInfo info = new PersonInfo();
        info.setHeadPortraitId(avatarFileTableId);
        ModuleHttpApi.enrollees(info, "headPortraitId", avatarFileTableId);
    }

    /**
     * 跟新个人信息
     */
    public void onEventMainThread(AccountAvaliableEvent event) {
        LoadingDialog.dismissDialog();
        refresh();
    }

}



