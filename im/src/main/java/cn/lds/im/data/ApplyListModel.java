package cn.lds.im.data;

import java.util.List;

/**
 * Created by E0608 on 2017/12/26.
 */

public class ApplyListModel<T> {

    /**
     * status : success
     * data : [{"approveTime":"2017-12-25T05:17:48.308Z","approverId":0,"approverName":"string","approverPhone":"string","description":"string","id":0,"name":"string","no":"string","phone":"string","refusalReason":"string","status":"APPOVING","userDate":"2017-12-25T05:17:48.308Z"}]
     * pageable : {"totalElements":0,"numberOfElements":0,"totalPages":0,"number":0,"first":false,"last":false,"size":0,"fromNumber":0,"toNumber":0}
     * errors : [{"field":"string","errmsg":"string","errcode":"string"}]
     * timestamp : 2017-12-25T05:17:48.308Z
     */

    private String status;
    private PageableBean pageable;
    private String timestamp;
    private List<DataBean> data;
    private List<ErrorsBean> errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PageableBean getPageable() {
        return pageable;
    }

    public void setPageable(PageableBean pageable) {
        this.pageable = pageable;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<ErrorsBean> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorsBean> errors) {
        this.errors = errors;
    }

    public static class PageableBean {
        /**
         * totalElements : 0
         * numberOfElements : 0
         * totalPages : 0
         * number : 0
         * first : false
         * last : false
         * size : 0
         * fromNumber : 0
         * toNumber : 0
         */

        private int totalElements;
        private int numberOfElements;
        private int totalPages;
        private int number;
        private boolean first;
        private boolean last;
        private int size;
        private int fromNumber;
        private int toNumber;

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public boolean isFirst() {
            return first;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }

        public boolean isLast() {
            return last;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getFromNumber() {
            return fromNumber;
        }

        public void setFromNumber(int fromNumber) {
            this.fromNumber = fromNumber;
        }

        public int getToNumber() {
            return toNumber;
        }

        public void setToNumber(int toNumber) {
            this.toNumber = toNumber;
        }
    }

    public static class DataBean {
        /**
         * approveTime : 2017-12-25T05:17:48.308Z
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
         * userDate : 2017-12-25T05:17:48.308Z
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
        private long startTime;
        private long endTime;

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
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
