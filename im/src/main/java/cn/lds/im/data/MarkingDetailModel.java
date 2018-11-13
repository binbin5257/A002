package cn.lds.im.data;

import java.util.List;

/**
 * 审批详情model
 * Created by E0608 on 2017/12/26.
 */

public class MarkingDetailModel {


    /**
     * status : success
     * data : {"approveTime":"2017-12-25T05:17:48.324Z","approverId":0,"approverName":"string","approverPhone":"string","description":"string","id":0,"name":"string","no":"string","phone":"string","refusalReason":"string","status":"APPOVING","userDate":"2017-12-25T05:17:48.324Z"}
     * errors : [{"field":"string","errmsg":"string","errcode":"string"}]
     * timestamp : 2017-12-25T05:17:48.324Z
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
         * approveTime : 2017-12-25T05:17:48.324Z
         * approverId : 0
         * approverName : string
         * approverPhone : string
         * description : string
         * id : 0
         * name : string
         * no : string
         * phone : string
         * refusalReason : string
         * status : APPOVING
         * userDate : 2017-12-25T05:17:48.324Z
         */

        private String approveTime;
        private int approverId;
        private String approverName;
        private String approverPhone;
        private String description;
        private int id;
        private String name;
        private String no;
        private String phone;
        private String refusalReason;
        private String status;
        private String userDate;
        private String startTime;
        private String endTime;
        private String expectedPlateLicenseNo;

        public String getExpectedPlateLicenseNo() {
            return expectedPlateLicenseNo;
        }

        public void setExpectedPlateLicenseNo(String expectedPlateLicenseNo) {
            this.expectedPlateLicenseNo = expectedPlateLicenseNo;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getApproveTime() {
            return approveTime;
        }

        public void setApproveTime(String approveTime) {
            this.approveTime = approveTime;
        }

        public int getApproverId() {
            return approverId;
        }

        public void setApproverId(int approverId) {
            this.approverId = approverId;
        }

        public String getApproverName() {
            return approverName;
        }

        public void setApproverName(String approverName) {
            this.approverName = approverName;
        }

        public String getApproverPhone() {
            return approverPhone;
        }

        public void setApproverPhone(String approverPhone) {
            this.approverPhone = approverPhone;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getRefusalReason() {
            return refusalReason;
        }

        public void setRefusalReason(String refusalReason) {
            this.refusalReason = refusalReason;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUserDate() {
            return userDate;
        }

        public void setUserDate(String userDate) {
            this.userDate = userDate;
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
