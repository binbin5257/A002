package cn.lds.chatcore.data;

import java.util.List;

/**
 * Created by Administrator on 2016/6/22.
 */
public class GetEnrolleesModel {

    /**
     * status : success
     * data : {"accountAmount":0,"address":"string","black":false,"departmentId":0,"departmentName":"string","drivingLicenseBackId":"string","drivingLicenseFrontId":"string","drivingLicenseNumber":"string","email":"string","foregiftAccountAmount":0,"foregiftQuota":0,"gender":"MALE","headPortraitId":"string","id":0,"identityBackId":"string","identityFrontId":"string","identityNumber":"string","name":"string","nickname":"string","no":"string","phone":"string","reviewType":"UNCOMMITTED"}
     * errors : [{"field":"string","errmsg":"string","errcode":"string"}]
     * timestamp : 2017-07-20T01:51:49.145Z
     */

    private String status;
    private DataBean data;
    private String timestamp;
    private List<ErrorsBean> errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<ErrorsBean> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorsBean> errors) {
        this.errors = errors;
    }

    public static class DataBean {
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

        private String accountAmount;
        private String address;
        private boolean black;
        private String departmentId;
        private String drivingLicenseBackId;
        private String drivingLicenseFrontId;
        private String drivingLicenseNumber;
        private String email;
        private String foregiftAccountAmount;
        private String foregiftQuota;
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
        private boolean obtainZmxyAuthorize;
        private int score;
        private String refuseReasons;
        private String companyName;
        private String departmentName;
        private String userNo;
        private String discount;
        private boolean approver;

        public boolean isApprover() {
            return approver;
        }

        public void setApprover(boolean approver) {
            this.approver = approver;
        }

        public String getRefuseReasons() {
            return refuseReasons;
        }

        public void setRefuseReasons(String refuseReasons) {
            this.refuseReasons = refuseReasons;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
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

        public boolean isObtainZmxyAuthorize() {
            return obtainZmxyAuthorize;
        }

        public void setObtainZmxyAuthorize(boolean obtainZmxyAuthorize) {
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

        public String getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(String departmentId) {
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

        public String getForegiftAccountAmount() {
            return foregiftAccountAmount;
        }

        public void setForegiftAccountAmount(String foregiftAccountAmount) {
            this.foregiftAccountAmount = foregiftAccountAmount;
        }

        public String getForegiftQuota() {
            return foregiftQuota;
        }

        public void setForegiftQuota(String foregiftQuota) {
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

    public static class ErrorsBean {
        /**
         * field : string
         * errmsg : string
         * errcode : string
         */

        private String field;
        private String errmsg;
        private String errcode;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public String getErrcode() {
            return errcode;
        }

        public void setErrcode(String errcode) {
            this.errcode = errcode;
        }
    }
}
