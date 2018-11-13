package cn.lds.im.data;

/**
 * Created by E0608 on 2018/1/4.
 */

public class ApproverModel {

    /**
     * status : success
     * data : {"id":null,"createdBy":null,"createdDate":null,"lastModifiedBy":null,"lastModifiedDate":null,"no":null,"name":null,"phone":null,"userDate":null,"approveTime":null,"approverId":3,"approverName":"司彬彬","approverPhone":"18840893841","refusalReason":null,"status":null,"description":null}
     */

    private String status;
    private DataBean data;

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

    public static class DataBean {
        /**
         * id : null
         * createdBy : null
         * createdDate : null
         * lastModifiedBy : null
         * lastModifiedDate : null
         * no : null
         * name : null
         * phone : null
         * userDate : null
         * approveTime : null
         * approverId : 3
         * approverName : 司彬彬
         * approverPhone : 18840893841
         * refusalReason : null
         * status : null
         * description : null
         */

        private Object id;
        private Object createdBy;
        private Object createdDate;
        private Object lastModifiedBy;
        private Object lastModifiedDate;
        private Object no;
        private Object name;
        private Object phone;
        private Object userDate;
        private Object approveTime;
        private int approverId;
        private String approverName;
        private String approverPhone;
        private Object refusalReason;
        private Object status;
        private Object description;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public Object getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Object createdBy) {
            this.createdBy = createdBy;
        }

        public Object getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Object createdDate) {
            this.createdDate = createdDate;
        }

        public Object getLastModifiedBy() {
            return lastModifiedBy;
        }

        public void setLastModifiedBy(Object lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
        }

        public Object getLastModifiedDate() {
            return lastModifiedDate;
        }

        public void setLastModifiedDate(Object lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
        }

        public Object getNo() {
            return no;
        }

        public void setNo(Object no) {
            this.no = no;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public Object getPhone() {
            return phone;
        }

        public void setPhone(Object phone) {
            this.phone = phone;
        }

        public Object getUserDate() {
            return userDate;
        }

        public void setUserDate(Object userDate) {
            this.userDate = userDate;
        }

        public Object getApproveTime() {
            return approveTime;
        }

        public void setApproveTime(Object approveTime) {
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

        public Object getRefusalReason() {
            return refusalReason;
        }

        public void setRefusalReason(Object refusalReason) {
            this.refusalReason = refusalReason;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }
    }
}
