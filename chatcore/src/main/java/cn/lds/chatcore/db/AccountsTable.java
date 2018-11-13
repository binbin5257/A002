package cn.lds.chatcore.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 登录人登录信息
 */
@Table(name = "accounts_info")
public class AccountsTable extends EntityBase {
    /**
     * 默认的字段ID
     */
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 一次性认证token
     */
    @Column(column = "nonce_token")
    private String nonceToken;

    /**
     * 用户编号，对应no字段
     */
    @Column(column = "account")
    private String account;

    /**
     * 名称
     */
    @Column(column = "name")
    private String name;
    /**
     * 行驶证编号
     */
    @Column(column = "drivingLicenseNumber")
    private String drivingLicenseNumber;
    /**
     * 身份证
     */
    @Column(column = "identityNumber")
    private String identityNumber;
    /**
     * 昵称
     */
    @Column(column = "nickname")
    private String nickname;

    /**
     * 手机号
     */
    @Column(column = "mobile")
    private String mobile;

    /**
     * 邮箱
     */
    @Column(column = "email")
    private String email;

    /**
     * 登录名
     */
    @Column(column = "login_id")
    private String loginId;

    /**
     * 手机App OpenId
     */
    @Column(column = "subscription_open_id")
    private String subscriptionOpenId;

    /////////////////////以下字段是个人详情返回的//////////////////////////
    /**
     * 用户ID
     */
    @Column(column = "client_user_id")
    private String clientUserId;

    /**
     * 头像
     */
    @Column(column = "avatar")
    private String avatar;

    /**
     * 生日
     */
    @Column(column = "birthday")
    private String birthday;

    /**
     * 性别
     */
    @Column(column = "gender")
    private String gender;

    /**
     * 国家
     */
    @Column(column = "country")
    private String country;

    /**
     * 省份
     */
    @Column(column = "province")
    private String province;

    /**
     * 城市
     */
    @Column(column = "city")
    private String city;

    /**
     * 签名
     */
    @Column(column = "desc")
    private String desc;

    /**
     * 我的二维码
     */
    @Column(column = "qrcode_id")
    private String qrcodeId;

    /**
     * 是否开启免打扰
     */
    @Column(column = "enablenodisturb")
    private boolean enableNoDisturb;

    /**
     * 免打扰开始时间，格式：HHmm
     */
    @Column(column = "starttimeofnodisturb")
    private String startTimeOfNoDisturb;

    /**
     * 免打扰结束时间，格式：HHmm
     */
    @Column(column = "endtimeofnodisturb")
    private String endTimeOfNoDisturb;

    /**
     * 加我为朋友时是否需要验证
     */
    @Column(column = "needfriendconfirmation")
    private boolean needFriendConfirmation;
    /**
     * 是否允许向我推荐通讯录朋友
     */
    @Column(column = "allowfindmobilecontacts")
    private boolean allowFindMobileContacts;
    /**
     * 是否允许通过登录名称找到我
     */
    @Column(column = "allowfindmebyloginid")
    private boolean allowFindMeByLoginId;
    /**
     * 是否允许通过手机号码找到我
     */
    @Column(column = "allowfindmebymobilenumber")
    private boolean allowFindMeByMobileNumber;
    /**
     * 认证状态
     */
    @Column(column = "reviewType")
    private String reviewType;
    /**
     * 身份证图片ID
     */
    @Column(column = "identityFrontId")
    private String identityFrontId;
    /**
     * 身份证图片背面ID
     */
    @Column(column = "identityBackId")
    private String identityBackId;

    /**
     * 驾驶证图片ID
     */
    @Column(column = "drivingLicenseFrontId")
    private String drivingLicenseFrontId;
    /**
     * 驾驶证图片背面ID
     */
    @Column(column = "drivingLicenseBackId")
    private String drivingLicenseBackId;
    /**
     * 账户余额
     */
    @Column(column = "accountAmount")
    private String accountAmount;
    /**
     * 押金余额
     */
    @Column(column = "foregiftAmount")
    private String foregiftAmount;
    /**
     * 需付押金总额
     */
    @Column(column = "obtainZmxyAuthorize")
    private int obtainZmxyAuthorize;

    /**
     * 需付押金总额
     */
    @Column(column = "foregiftQuota")
    private String foregiftQuota;

    /**
     * 需付押金总额
     */
    @Column(column = "score")
    private int score;
    /**
     * 认证失败原因
     */
    @Column(column = "refuseReasons")
    private String refuseReasons;

    /**
     * 公司
     */
    @Column(column = "companyName")
    private String companyName;
    /**
     * 部门
     */
    @Column(column = "departmentName")
    private String departmentName;
    /**
     * 员工编号
     */
    @Column(column = "userNo")
    private String userNo;
    /**
     * 折扣
     */
    @Column(column = "discount")
    private String discount;

    /**
     * 是否是审批人(0,不是；1是)
     */
    @Column(column = "approver")
    private int approver;



    public String getRefuseReasons() {
        return refuseReasons;
    }

    public void setRefuseReasons(String refuseReasons) {
        this.refuseReasons = refuseReasons;
    }

    public int getApprover() {
        return approver;
    }

    public void setApprover(int approver) {
        this.approver = approver;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getRefuseReason() {
        return refuseReasons;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReasons = refuseReason;
    }

    public int getObtainZmxyAuthorize() {
        return obtainZmxyAuthorize;
    }

    public void setObtainZmxyAuthorize(int obtainZmxyAuthorize) {
        this.obtainZmxyAuthorize = obtainZmxyAuthorize;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(String accountAmount) {
        this.accountAmount = accountAmount;
    }

    public String getForegiftAmount() {
        return foregiftAmount;
    }

    public void setForegiftAmount(String foregiftAmount) {
        this.foregiftAmount = foregiftAmount;
    }

    public String getForegiftQuota() {
        return foregiftQuota;
    }

    public void setForegiftQuota(String foregiftQuota) {
        this.foregiftQuota = foregiftQuota;
    }

    public String getReviewType() {
        return reviewType;
    }

    public void setReviewType(String reviewType) {
        this.reviewType = reviewType;
    }

    public String getIdentityFrontId() {
        return identityFrontId;
    }

    public void setIdentityFrontId(String identityFrontId) {
        this.identityFrontId = identityFrontId;
    }

    public String getDrivingLicenseFrontId() {
        return drivingLicenseFrontId;
    }

    public void setDrivingLicenseFrontId(String drivingLicenseFrontId) {
        this.drivingLicenseFrontId = drivingLicenseFrontId;
    }

    public String getIdentityBackId() {
        return identityBackId;
    }

    public void setIdentityBackId(String identityBackId) {
        this.identityBackId = identityBackId;
    }

    public String getDrivingLicenseBackId() {
        return drivingLicenseBackId;
    }

    public void setDrivingLicenseBackId(String drivingLicenseBackId) {
        this.drivingLicenseBackId = drivingLicenseBackId;
    }

    public AccountsTable() {
    }

    public AccountsTable(String account,
                         String loginId,
                         String nonceToken,
                         String name,
                         String nickname,
                         String email,
                         String mobile,
                         String subscriptionOpenId,
                         int score,
                         boolean obtainZmxyAuthorizes,
                         String refuseReason,
                         String companyName,
                         String departmentName,
                         String userNo,
                         String discount,
                         boolean approver) {
        this.account = account;
        this.loginId = loginId;
        this.nonceToken = nonceToken;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.mobile = mobile;
        this.subscriptionOpenId = subscriptionOpenId;
        this.score = score;
        this.refuseReasons = refuseReason;
        this.companyName = companyName;
        this.departmentName = departmentName;
        this.userNo = userNo;
        this.discount = discount;
        if(approver){
            this.approver = 1;
        }else{
            this.approver = 0;
        }
        if(obtainZmxyAuthorizes){
            this.obtainZmxyAuthorize = 1;
        }else{
            this.obtainZmxyAuthorize = 0;
        }

    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public boolean isAllowFindMeByLoginId() {
        return allowFindMeByLoginId;
    }

    public void setAllowFindMeByLoginId(boolean allowFindMeByLoginId) {
        this.allowFindMeByLoginId = allowFindMeByLoginId;
    }

    public boolean isAllowFindMeByMobileNumber() {
        return allowFindMeByMobileNumber;
    }

    public void setAllowFindMeByMobileNumber(boolean allowFindMeByMobileNumber) {
        this.allowFindMeByMobileNumber = allowFindMeByMobileNumber;
    }

    public boolean isAllowFindMobileContacts() {
        return allowFindMobileContacts;
    }

    public void setAllowFindMobileContacts(boolean allowFindMobileContacts) {
        this.allowFindMobileContacts = allowFindMobileContacts;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getClientUserId() {
        return clientUserId;
    }

    public void setClientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnableNoDisturb() {
        return enableNoDisturb;
    }

    public void setEnableNoDisturb(boolean enableNoDisturb) {
        this.enableNoDisturb = enableNoDisturb;
    }

    public String getEndTimeOfNoDisturb() {
        return endTimeOfNoDisturb;
    }

    public void setEndTimeOfNoDisturb(String endTimeOfNoDisturb) {
        this.endTimeOfNoDisturb = endTimeOfNoDisturb;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getQrcodeId() {
        return qrcodeId;
    }

    public void setQrcodeId(String qrcodeId) {
        this.qrcodeId = qrcodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNeedFriendConfirmation() {
        return needFriendConfirmation;
    }

    public void setNeedFriendConfirmation(boolean needFriendConfirmation) {
        this.needFriendConfirmation = needFriendConfirmation;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNonceToken() {
        return nonceToken;
    }

    public void setNonceToken(String nonceToken) {
        this.nonceToken = nonceToken;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStartTimeOfNoDisturb() {
        return startTimeOfNoDisturb;
    }

    public void setStartTimeOfNoDisturb(String startTimeOfNoDisturb) {
        this.startTimeOfNoDisturb = startTimeOfNoDisturb;
    }

    public String getSubscriptionOpenId() {
        return subscriptionOpenId;
    }

    public void setSubscriptionOpenId(String subscriptionOpenId) {
        this.subscriptionOpenId = subscriptionOpenId;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }


    public AccountsTable(String nonceToken,
                         String gender,
                         String account,
                         String name,
                         String mobile,
                         String email,
                         String avatar,
                         String reviewType,
                         String identityFrontId,
                         String identityBackId,
                         String drivingLicenseFrontId,
                         String drivingLicenseBackId,
                         String accountAmount,
                         String foregiftAmount,
                         String foregiftQuota,
                         String identityNumber,
                         String drivingLicenseNumber,
                         int score,
                         boolean obtainZmxyAuthorizes,
                         String refuseReason,
                         String companyName,
                         String departmentName,
                         String userNo,
                         String discount,
                         boolean approver) {
        this.nonceToken = nonceToken;
        this.account = account;
        this.gender = gender;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.avatar = avatar;
        this.reviewType = reviewType;
        this.identityFrontId = identityFrontId;
        this.identityBackId = identityBackId;
        this.drivingLicenseFrontId = drivingLicenseFrontId;
        this.drivingLicenseBackId = drivingLicenseBackId;
        this.accountAmount = accountAmount;
        this.foregiftAmount = foregiftAmount;
        this.foregiftQuota = foregiftQuota;
        this.identityNumber = identityNumber;
        this.drivingLicenseNumber = drivingLicenseNumber;
        this.score = score;
        this.refuseReasons = refuseReason;
        this.companyName = companyName;
        this.departmentName = departmentName;
        this.userNo = userNo;
        this.discount = discount;
        if(obtainZmxyAuthorizes){
            this.obtainZmxyAuthorize = 1;
        }else{
            this.obtainZmxyAuthorize = 0;
        }
        if(approver){
            this.approver = 1;
        }else{
            this.approver = 0;
        }

    }
}
