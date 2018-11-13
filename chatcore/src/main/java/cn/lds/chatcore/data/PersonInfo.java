package cn.lds.chatcore.data;

/**
 * Created by Administrator on 2016/6/21.
 */
public class PersonInfo {

    /**
     * accountAmount : 0
     * address : string
     * black : false
     * departmentId : 0
     * departmentName : string
     * drivingLicenseBackId : string
     * drivingLicenseFrontId : string
     * drivingLicenseNumber : string
     * email : string
     * foregiftAccountAmount : 0
     * foregiftQuota : 0
     * gender : MALE
     * headPortraitId : string
     * id : 0
     * identityBackId : string
     * identityFrontId : string
     * identityNumber : string
     * name : string
     * nickname : string
     * no : string
     * phone : string
     * reviewType : UNCOMMITTED
     */

    private int accountAmount;
    private String address;
    private boolean black;
    private int departmentId;
    private String departmentName;
    private String drivingLicenseBackId;
    private String drivingLicenseFrontId;
    private String drivingLicenseNumber;
    private String email;
    private int foregiftAccountAmount;
    private int foregiftQuota;
    private String gender;
    private String headPortraitId;
    private int id;
    private String identityBackId;
    private String identityFrontId;
    private String identityNumber;
    private String name;
    private String nickname;
    private String no;
    private String phone;
    private String reviewType;

    public int getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(int accountAmount) {
        this.accountAmount = accountAmount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isBlack() {
        return black;
    }

    public void setBlack(boolean black) {
        this.black = black;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDrivingLicenseBackId() {
        return drivingLicenseBackId;
    }

    public void setDrivingLicenseBackId(String drivingLicenseBackId) {
        this.drivingLicenseBackId = drivingLicenseBackId;
    }

    public String getDrivingLicenseFrontId() {
        return drivingLicenseFrontId;
    }

    public void setDrivingLicenseFrontId(String drivingLicenseFrontId) {
        this.drivingLicenseFrontId = drivingLicenseFrontId;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getForegiftAccountAmount() {
        return foregiftAccountAmount;
    }

    public void setForegiftAccountAmount(int foregiftAccountAmount) {
        this.foregiftAccountAmount = foregiftAccountAmount;
    }

    public int getForegiftQuota() {
        return foregiftQuota;
    }

    public void setForegiftQuota(int foregiftQuota) {
        this.foregiftQuota = foregiftQuota;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeadPortraitId() {
        return headPortraitId;
    }

    public void setHeadPortraitId(String headPortraitId) {
        this.headPortraitId = headPortraitId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentityBackId() {
        return identityBackId;
    }

    public void setIdentityBackId(String identityBackId) {
        this.identityBackId = identityBackId;
    }

    public String getIdentityFrontId() {
        return identityFrontId;
    }

    public void setIdentityFrontId(String identityFrontId) {
        this.identityFrontId = identityFrontId;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReviewType() {
        return reviewType;
    }

    public void setReviewType(String reviewType) {
        this.reviewType = reviewType;
    }
}
