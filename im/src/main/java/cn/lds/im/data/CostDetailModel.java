package cn.lds.im.data;

import java.util.List;

/**
 * Created by gaihd on 17/07/20.
 */
public class CostDetailModel {

    /**
     * status : success
     * data : [{"changeAmount":0,"changeType":"ACCOUNT_RECHARGE","id":0,"createdBy":"string","createdDate":"2017-07-20T01:51:49.130Z","lastModifiedBy":"string","lastModifiedDate":"2017-07-20T01:51:49.130Z"}]
     * pageable : {"totalElements":0,"numberOfElements":0,"totalPages":0,"number":0,"first":false,"last":false,"size":0,"fromNumber":0,"toNumber":0}
     * errors : [{"field":"string","errmsg":"string","errcode":"string"}]
     * timestamp : 2017-07-20T01:51:49.130Z
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
         * changeAmount : 0
         * changeType : ACCOUNT_RECHARGE
         * id : 0
         * createdBy : string
         * createdDate : 2017-07-20T01:51:49.130Z
         * lastModifiedBy : string
         * lastModifiedDate : 2017-07-20T01:51:49.130Z
         */

        private String changeAmount;
        private String changeType;
        private String id;
        private String createdBy;
        private String createdDate;
        private String lastModifiedBy;
        private String lastModifiedDate;

        public String getChangeAmount() {
            return changeAmount;
        }

        public void setChangeAmount(String changeAmount) {
            this.changeAmount = changeAmount;
        }

        public String getChangeType() {
            return changeType;
        }

        public void setChangeType(String changeType) {
            this.changeType = changeType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getLastModifiedBy() {
            return lastModifiedBy;
        }

        public void setLastModifiedBy(String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
        }

        public String getLastModifiedDate() {
            return lastModifiedDate;
        }

        public void setLastModifiedDate(String lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
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