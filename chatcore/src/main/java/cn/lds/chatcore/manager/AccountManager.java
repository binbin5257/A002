package cn.lds.chatcore.manager;

import com.lidroid.xutils.db.sqlite.Selector;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.common.CacheHelper;
import cn.lds.chatcore.common.CoreHttpApi;
import cn.lds.chatcore.common.CoreHttpApiKey;
import cn.lds.chatcore.common.DbHelper;
import cn.lds.chatcore.common.GsonImplHelp;
import cn.lds.chatcore.common.LoadingDialog;
import cn.lds.chatcore.common.LogHelper;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.common.datacatch.CacheManager;
import cn.lds.chatcore.data.GetEnrolleesModel;
import cn.lds.chatcore.data.HttpResult;
import cn.lds.chatcore.data.LoginRequestModel;
import cn.lds.chatcore.data.LoginResponseModel;
import cn.lds.chatcore.db.AccountsTable;
import cn.lds.chatcore.enums.LoginType;
import cn.lds.chatcore.event.AccountAvaliableEvent;
import cn.lds.chatcore.event.HttpRequestEvent;
import cn.lds.chatcore.event.OrganizationChangEvent;
import cn.lds.chatcore.imtp.ImtpManager;
import de.greenrobot.event.EventBus;

public class AccountManager extends AbstractManager {

    protected static AccountManager instance;

    //private DbUtils dbUtils = DbHelper.getDbUtils();

    /* ping是否已经启动 */
    private boolean pingStarted = false;

    //定时ping保持session
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            CoreHttpApi.ping();
            VersionManager.getInstance().setSendApiFlag(true);
        }
    };


    // 登录者的登录信息
    private AccountsTable mAccount;
    // 是否登录判断 TODO:这个有问题哈
//    private boolean isLogin = false;
    private boolean isLogin = false;
    /**
     * 清除登录者信息
     */
    public void clearAccount() {
        this.mAccount = null;
    }

    public static AccountManager getInstance() {
        if (instance == null) {
            try {
                instance = new AccountManager();
                MyApplication.getInstance().addManager(instance);
                EventBus.getDefault().register(instance);
            } catch (Exception ex) {
                LogHelper.e("初始化Manager", ex);
            }
        }
        return instance;
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return isLogin;
    }


    /**
     * 根据编号查找登录账号
     *
     * @return
     */
    public AccountsTable findByNo() {
        String account = CacheHelper.getAccount();
        if (ToolsHelper.isNull(account))
            return null;
        AccountsTable table = null;
        try {
            table = DbHelper.getDbUtils().findFirst(Selector.from(AccountsTable.class).where("account", "=", account));
        } catch (Exception e) {
            LogHelper.e(this.getClass().getName(), e);
        }
        return table;
    }

    /**
     * 新增加登录信息
     */
    public AccountsTable add(String account,
                             String loginId,
                             String nonceToken,
                             String name,
                             String nickname,
                             String email,
                             String mobile,
                             String subscriptionOpenId,
                             int score,
                             boolean obtainZmxyAuthorize,
                             String refuseReason,
                             String companyName,
                             String departmentName,
                             String userNo,
                             String discount,
                             boolean approver) {
        AccountsTable table = findByNo();
        try {
            if (null == table) {
                table = new AccountsTable(account,
                        loginId,
                        nonceToken,
                        name,
                        nickname,
                        email,
                        mobile,
                        subscriptionOpenId,
                        score,
                        obtainZmxyAuthorize,
                        refuseReason,
                        companyName,
                        departmentName,
                        userNo,
                        discount,
                        approver);
                DbHelper.getDbUtils().save(table);
            } else {
                table.setNonceToken(nonceToken);
                table.setName(name);
                table.setNickname(nickname);
                table.setEmail(email);
                //table.setMobile(mobile);
                table.setSubscriptionOpenId(subscriptionOpenId);
                table.setScore(score);
                table.setObtainZmxyAuthorize(obtainZmxyAuthorize ? 1 : 0);
                table.setApprover(approver ? 1:0);
                DbHelper.getDbUtils().update(table, "nonce_token", "name", "nickname", "email", "subscription_open_id","score","obtainZmxyAuthorize","approver");
            }

        } catch (Exception e) {
            LogHelper.e(this.getClass().getName(), e);
        }

        return table;
    }

    /**
     * 使用一次性Token登录
     */
    public void autoLoginWithNonceToken() {
        String nonceToken = CacheHelper.getAccessToken();
        if (!ToolsHelper.isNull(nonceToken)) {
            LoginRequestModel loginRequestModel = new LoginRequestModel();
            loginRequestModel.setLoginType(LoginType.nonceToken);
            loginRequestModel.setNonceToken(nonceToken);
            CoreHttpApi.login(loginRequestModel, true);
        } else {
           // ToolsHelper.showStatus(MyApplication.getInstance().getApplicationContext(), false, MyApplication.getInstance().getApplicationContext().getString(R.string.toolshelper_err));
        }
    }


    /**
     * 注销
     */
    public void logout() {

        isLogin = false;
        // 清除登录用户信息
        AccountManager.getInstance().clearAccount();
    }


    public void onEventBackgroundThread(HttpRequestEvent event) {
        HttpResult httpResult = event.getResult();
        String apiNo = httpResult.getApiNo();

        if (!(CoreHttpApiKey.login.equals(apiNo)
                || CoreHttpApiKey.enrolleesGet.equals(apiNo)
                || CoreHttpApiKey.enrollees.equals(apiNo)

        ))
            return;

        switch (apiNo) {
            case CoreHttpApiKey.login:
                A001(httpResult);
                break;
            case CoreHttpApiKey.enrolleesGet:
                enrolleesGet(httpResult);
                break;
            case CoreHttpApiKey.enrollees:
                enrollees(httpResult);
                break;

            default:
                break;
        }

        //TODO: suncf_todo 将返回的结果插入数据库
    }

    private void enrollees(HttpResult httpResult) {
        Map<String, String> map = httpResult.getExtras();
        if (null == map) {
            //刷新个人数据
            CoreHttpApi.enrolleesGet();
        } else {
            String key = map.get("key");
            String value = map.get("value");
            AccountsTable table = findByNo();
            if (null == table) {
                LoadingDialog.dismissDialog();
                return;
            }
            if ("name".equals(key)) {
                table.setName(value);
            } else if ("email".equals(key)) {
                table.setEmail(value);
            } else if ("gender".equals(key)) {
                table.setGender(value);
            }
            try {
                DbHelper.getDbUtils().update(table, key);
            } catch (Exception e) {
                LogHelper.e(this.getClass().getName(), e);
            }
        }
        LoadingDialog.dismissDialog();
    }

    /**
     * 初始化 个人信息
     *
     * @param httpResult
     */
    private void enrolleesGet(HttpResult httpResult) {
        GetEnrolleesModel m = GsonImplHelp.get().toObject(httpResult.getResult(), GetEnrolleesModel.class);
        GetEnrolleesModel.DataBean mData = m.getData();
        if (null != mData) {
            this.add(mData);
            CacheHelper.setLoginid(mData.getPhone());
            MyApplication.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new AccountAvaliableEvent());
                }
            });
        }
    }

    private void add(GetEnrolleesModel.DataBean mData) {
        AccountsTable table = findByNo();
        try {
            if (null == table) {
                table = new AccountsTable(
                        CacheHelper.getAccessToken(),
                        mData.getGender(),
                        CacheHelper.getAccount(),
                        mData.getName(),
                        mData.getPhone(),
                        mData.getEmail(),
                        mData.getHeadPortraitId(),
                        mData.getReviewType(),
                        mData.getIdentityFrontId(),
                        mData.getIdentityBackId(),
                        mData.getDrivingLicenseFrontId(),
                        mData.getDrivingLicenseBackId(),
                        mData.getAccountAmount(),
                        mData.getForegiftAccountAmount(),
                        mData.getForegiftQuota(),
                        mData.getIdentityNumber(),
                        mData.getDrivingLicenseNumber(),
                        mData.getScore(),
                        mData.isObtainZmxyAuthorize(),
                        mData.getRefuseReason(),
                        mData.getCompanyName(),
                        mData.getDepartmentName(),
                        mData.getUserNo(),
                        mData.getDiscount(),
                        mData.isApprover()
                );
                DbHelper.getDbUtils().save(table);
            } else {
                table.setNonceToken(CacheHelper.getAccessToken());
                table.setName(mData.getName());
                table.setEmail(mData.getEmail());
                table.setGender(mData.getGender());//存入性别
                table.setMobile(mData.getPhone());//存入手机
                table.setReviewType(mData.getReviewType());//存入认证类型
                table.setIdentityFrontId(mData.getIdentityFrontId());//存入身份证图片ID
                table.setDrivingLicenseFrontId(mData.getDrivingLicenseFrontId());//存入驾驶证图片ID
                table.setIdentityBackId(mData.getIdentityBackId());//存入身份证图片背面ID
                table.setDrivingLicenseBackId(mData.getDrivingLicenseBackId());//存入驾驶证图片背面ID
                table.setAccountAmount(mData.getAccountAmount());
                table.setForegiftAmount(mData.getForegiftAccountAmount());
                table.setForegiftQuota(mData.getForegiftQuota());
                table.setAvatar(mData.getHeadPortraitId());
                table.setIdentityNumber(mData.getIdentityNumber());
                table.setDrivingLicenseNumber(mData.getDrivingLicenseNumber());
                table.setScore(mData.getScore());
                table.setRefuseReason(mData.getRefuseReason());
                table.setCompanyName(mData.getCompanyName());
                table.setDepartmentName(mData.getDepartmentName());
                table.setUserNo(mData.getUserNo());
                table.setDiscount(mData.getDiscount());
                if(mData.isApprover()){
                    table.setApprover(1);
                }else{
                    table.setApprover(0);
                }

                if(mData.isObtainZmxyAuthorize()){
                    table.setObtainZmxyAuthorize(1);
                }else{
                    table.setObtainZmxyAuthorize(0);
                }
                DbHelper.getDbUtils().update(table, "id", "nonce_token", "name", "email", "mobile",
                        "gender", "reviewType", "identityFrontId","identityBackId",
                        "drivingLicenseFrontId","drivingLicenseBackId", "accountAmount",
                        "foregiftAmount",
                        "foregiftQuota",
                        "avatar", "identityNumber",
                        "drivingLicenseNumber",
                        "obtainZmxyAuthorize",
                        "score",
                        "refuseReasons",
                        "companyName",
                        "departmentName",
                        "userNo",
                        "discount",
                        "approver"
                        );
            }
        } catch (Exception e) {
            LogHelper.e(this.getClass().getName(), e);
        }
    }

    /**
     * 更新用户审核状态
     */
    public void updateReviewType(String reviewType) {
        AccountsTable table = findByNo();
        try {
            if (null == table)
                return;
            table.setReviewType(reviewType);//存入认证类型
            DbHelper.getDbUtils().update(table, "reviewType");
        } catch (Exception e) {
            LogHelper.e(this.getClass().getName(), e);
        }
    }


//    /**
//     * 使用一次性token自动登录
//     */
//    public void autoLoginWithNonceToken() {
//        LoginRequestModel loginRequestModel = new LoginRequestModel();
//        loginRequestModel.setLoginType(LoginType.nonceToken);
//        loginRequestModel.setNonceToken(nonceToken);
//        CoreHttpApi.login(loginRequestModel, false);
//    }


    /**
     * 登录/自动登录 login
     *
     * @param httpResult
     */
    private void A001(HttpResult httpResult) {

        LoginResponseModel loginResponseModel = GsonImplHelp.get().toObject(httpResult.getResult(), LoginResponseModel.class);
        MyApplication.confirmStatus = loginResponseModel.getData().isConfirmStatus();
        try {
            String account = loginResponseModel.getData().getPrincipal();
            String loginId = "";
            String nonceToken = loginResponseModel.getData().getDetails().getOnceToken();
            String approverName = loginResponseModel.getData().getDetails().getApproverName();
            String approverPhone = loginResponseModel.getData().getDetails().getApproverPhone();
            String approverId = loginResponseModel.getData().getDetails().getApproverId();
            String mobile = "";

            List<LoginResponseModel.DataBean.OrganizationsBean> organizationsBeen = loginResponseModel.getData().getOrganizations();

            Map<String, String> extras = httpResult.getExtras();
            LoginType loginType = LoginType.valueOf(extras.get("loginType"));
            boolean background = Boolean.valueOf(extras.get("background"));
            loginId = httpResult.getExtras().get("loginId");
            switch (loginType) {
                case id_pass:
                    //TODO：loginId+密码登录后续处理
                    CacheHelper.setLoginid(loginId);
                    break;
                case mobile_pass:
                    //TODO：mobile+密码登录后续处理
                    CacheHelper.setLoginid(mobile);
                    break;
                case mobile_captcha:
                    //TODO：mobile+短信验证码登录后续处理
                    CacheHelper.setLoginid(mobile);
                    break;
                case nonceToken:
                    //TODO：一次性token自动登录后续处理
                    CacheHelper.setLoginid(loginId);
                    break;
                default:
                    CacheHelper.setLoginid(mobile);
                    break;
            }
            // 缓存access token
            CacheHelper.setAccessToken(nonceToken);
            if (null != organizationsBeen && organizationsBeen.size() > 0) {
                JSONArray j = new JSONArray();
                for (LoginResponseModel.DataBean.OrganizationsBean o : organizationsBeen) {
                    j.put(GsonImplHelp.get().toJson(o));
                }

                CacheHelper.setOrganizationList(j);
            } else {
                CacheHelper.setOrganizationList(new JSONArray());
                CacheHelper.setOrganizationId("");
            }
            CacheHelper.setAccount(account);
            CacheHelper.setApproverName(approverName);
            CacheHelper.setApproverPhone(approverPhone);
            CacheHelper.setApproverId(approverId);

            isLogin = true;
            //非后台登录，立即创建数据库对象
            if (!background) {
                DbHelper.initDbUtils(account);
            }
            CacheManager.resetCache();


            //非后台登录，立即请求必要数据
            if (!background) {
            } else {
                ImtpManager.getInstance().registerDevice(true);
            }

            MyApplication.getInstance().requestEssentialData();
        } catch (Exception e) {
            LogHelper.d("A001", e);
        }
        MyApplication.essential.setAccountDetailsAvailable(true);

        EventBus.getDefault().post(new OrganizationChangEvent());

    }




}
